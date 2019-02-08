/*
  Copyright 2017 Esri

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.​

  For additional information, contact:
  Environmental Systems Research Institute, Inc.
  Attn: Contracts Dept
  380 New York Street
  Redlands, California, USA 92373

  email: contracts@esri.com
*/

package com.esri.geoevent.processor.cacheawarefieldcalculator;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.esri.geoevent.processor.cacheawarefieldcalculator.expression.Function;
import com.esri.geoevent.processor.cacheawarefieldcalculator.expression.InvalidFunctionException;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import com.esri.geoevent.processor.cacheawarefieldcalculator.expression.ExpressionBuilder;
import com.esri.ges.core.component.ComponentException;
import com.esri.ges.core.geoevent.DefaultFieldDefinition;
import com.esri.ges.core.geoevent.FieldDefinition;
import com.esri.ges.core.geoevent.FieldException;
import com.esri.ges.core.geoevent.FieldType;
import com.esri.ges.core.geoevent.GeoEvent;
import com.esri.ges.core.geoevent.GeoEventDefinition;
import com.esri.ges.core.geoevent.GeoEventPropertyName;
import com.esri.ges.core.geoevent.Tag;
import com.esri.ges.core.validation.ValidationException;
import com.esri.ges.framework.i18n.BundleLogger;
import com.esri.ges.framework.i18n.BundleLoggerFactory;
import com.esri.ges.manager.geoeventdefinition.GeoEventDefinitionManager;
import com.esri.ges.manager.geoeventdefinition.GeoEventDefinitionManagerException;
import com.esri.ges.manager.tag.TagManager;
import com.esri.ges.messaging.GeoEventCreator;
import com.esri.ges.processor.CacheEnabledGeoEventProcessor;
import com.esri.ges.processor.GeoEventProcessorDefinition;
import com.esri.ges.util.Converter;
import com.esri.ges.util.Validator;

public class CacheAwareFieldCalculator extends CacheEnabledGeoEventProcessor implements ServiceTrackerCustomizer
{
	private static final BundleLogger	LOGGER		= BundleLoggerFactory.getLogger(CacheAwareFieldCalculator.class);
	private static final DateFormat STANDARD_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	private Map<String, String>				edMapper	= new ConcurrentHashMap<String, String>();
	private GeoEventDefinitionManager	geoEventDefinitionManager;
	private ServiceTracker						geoEventDefinitionManagerTracker;
	private GeoEventCreator						geoEventCreator;
	private ResultDestination					resultDestination;
	private String										expression;
	private String										fieldName;
	private FieldType									fieldType;
	private FieldDefinition						fieldDefinition;
	private String										geoEventDefinitionName;
	private TagManager								tagManager;
	private String										fieldTagName;

	private static final Function PARSE_DATE_CUSTOM_FUNCTION;


	static {
		Function function;
		try {
			function = new Function("parseDate", 2)
			{
				@Override
				public Object applyFunction(Object... args)
				{
					if(args != null && args.length == 2)
					{
						final String dateString = (String) args[0];

						try {
							final DateFormat dateFormat =
								(args[1] == null || ((String)args[1]).isEmpty()) ?
									STANDARD_DATE_FORMAT :
									new SimpleDateFormat((String)args[1]);

							return dateFormat.parse(dateString);
						} catch (ParseException e) {
							// ignore
						}
					}
					return null;
				}
			};
		} catch (InvalidFunctionException ignored) {
			function = null;
		}
		PARSE_DATE_CUSTOM_FUNCTION = function;
	}

	protected CacheAwareFieldCalculator(GeoEventProcessorDefinition definition) throws ComponentException
	{
		super(definition);
		if (geoEventDefinitionManagerTracker == null)
			geoEventDefinitionManagerTracker = new ServiceTracker(definition.getBundleContext(), GeoEventDefinitionManager.class.getName(), this);
		geoEventDefinitionManagerTracker.open();
	}

	@Override
	public void afterPropertiesSet()
	{
		expression = getProperty("expression").getValueAsString().trim();
		fieldName = null;
		resultDestination = Validator.valueOfIgnoreCase(ResultDestination.class, getProperty("resultDestination").getValueAsString(), null);
		geoEventMutator = false;
		if (resultDestination != null)
			if (ResultDestination.NEW_FIELD.equals(resultDestination))
			{
				fieldName = getProperty("newFieldName").getValueAsString().trim();
				fieldTagName = Validator.compactSpaces(getProperty("newFieldTag").getValueAsString()).trim();
			}
			else if (ResultDestination.EXISTING_FIELD.equals(resultDestination))
			{
				fieldName = getProperty("existingFieldName").getValueAsString().trim();
				geoEventMutator = true;
			}
		fieldType = Validator.valueOfIgnoreCase(FieldType.class, getProperty("newFieldType").getValueAsString().trim(), null);
		geoEventDefinitionName = getProperty("newGeoEventDefinitionName").getValueAsString().trim();
		fieldDefinition = null;
	}

	private Object calculate(final GeoEvent geoEvent) throws Exception
	{
		FieldDefinition fd = (ResultDestination.EXISTING_FIELD.equals(resultDestination)) ? geoEvent.getGeoEventDefinition().getFieldDefinition(fieldName) : fieldDefinition;
		if (fd != null)
		{
			final Object result = new ExpressionBuilder(geoEvent, geoEventCache, expression)
				.withCustomFunction(PARSE_DATE_CUSTOM_FUNCTION)
				.build()
				.calculate();

			switch (fd.getType())
			{
				case Date:
					return Converter.convertToDate(result);
				case Double:
					return Converter.convertToDouble(result);
				case Float:
					return Converter.convertToFloat(result);
				case Integer:
					return Converter.convertToInteger(result);
				case Long:
					return Converter.convertToLong(result);
				case Short:
					return Converter.convertToShort(result);
				case Boolean:
					return Converter.convertToBoolean(result);
				default:
					return result.toString();
			}
		}
		return null;
	}

	@Override
	public GeoEvent process(GeoEvent geoEvent) throws Exception
	{
		if (geoEvent != null && geoEventDefinitionManager != null)
		{
			Object result = calculate(geoEvent);
			switch (resultDestination)
			{
				case NEW_FIELD:
				{
					GeoEventDefinition edOut = lookup(geoEvent.getGeoEventDefinition());
					GeoEvent geoEventOut = geoEventCreator.create(edOut.getGuid(), new Object[] { geoEvent.getAllFields(), result });
					geoEventOut.setProperty(GeoEventPropertyName.TYPE, "message");
					geoEventOut.setProperty(GeoEventPropertyName.OWNER_ID, getId());
					geoEventOut.setProperty(GeoEventPropertyName.OWNER_URI, definition.getUri());
					for (Map.Entry<GeoEventPropertyName, Object> property : geoEvent.getProperties())
						if (!geoEventOut.hasProperty(property.getKey()))
							geoEventOut.setProperty(property.getKey(), property.getValue());
					return geoEventOut;
				}
				case EXISTING_FIELD:
				{
					try
					{
						geoEvent.setField(fieldName, result);
					}
					catch (FieldException error)
					{
						LOGGER.error("SET_FIELD_ERROR", (result != null ? result.toString() : "null"), fieldName, geoEvent.getGeoEventDefinition().getName(), error.getMessage());
					}
					break;
				}
			}
		}
		return geoEvent;
	}

	synchronized private GeoEventDefinition lookup(GeoEventDefinition edIn) throws Exception
	{
		GeoEventDefinition edOut = edMapper.containsKey(edIn.getGuid()) ? geoEventDefinitionManager.getGeoEventDefinition(edMapper.get(edIn.getGuid())) : null;
		if (edOut == null)
		{
			edOut = edIn.augment(Arrays.asList(fieldDefinition));
			edOut.setName(geoEventDefinitionName);
			edOut.setOwner(getId());
			geoEventDefinitionManager.addTemporaryGeoEventDefinition(edOut, geoEventDefinitionName.isEmpty());
			edMapper.put(edIn.getGuid(), edOut.getGuid());
		}
		return edOut;
	}

	@Override
	public synchronized void validate() throws ValidationException
	{
		super.validate();
		List<String> errors = new ArrayList<String>();
		if (resultDestination == null)
			errors.add("Result Destination is unknown.");
		if (ResultDestination.NEW_FIELD.equals(resultDestination))
		{
			try
			{
				fieldDefinition = new DefaultFieldDefinition(fieldName, fieldType);
				tag(fieldDefinition);
			}
			catch (Exception e)
			{
				errors.add(e.getMessage());
			}
		}
		else if (ResultDestination.EXISTING_FIELD.equals(resultDestination))
			if (fieldName.isEmpty())
				errors.add(LOGGER.translate("VALIDATION_ERROR_FIELD_DOESNT_EXIST"));
		if (errors.size() > 0)
		{
			StringBuffer sb = new StringBuffer();
			for (String message : errors)
				sb.append(message).append("\n");
			throw new ValidationException(LOGGER.translate("VALIDATION_ERROR", sb.toString()));
		}
	}

	private void tag(FieldDefinition fd)
	{
		if (!fieldTagName.isEmpty() && Validator.isValidFieldDefinitionName(fieldTagName))
		{
			Tag tag = tagManager.getTag(fieldTagName);
			if (tag != null)
			{
				List<String> types = tag.getTypes();
				if (types != null && types.indexOf(fd.getType().toString()) != -1)
					fd.addTag(tag.getName());
			}
		}
	}

	@Override
	public boolean isGeoEventMutator()
	{
		return geoEventMutator;
	}

	@Override
	public void shutdown()
	{
		super.shutdown();
		clearGeoEventDefinitionMapper();
		geoEventDefinitionManagerTracker.close();
	}

	synchronized private void clearGeoEventDefinitionMapper()
	{
		if (!edMapper.isEmpty())
		{
			for (String guid : edMapper.values())
			{
				try
				{
					geoEventDefinitionManager.deleteGeoEventDefinition(guid);
				}
				catch (GeoEventDefinitionManagerException e)
				{
					;
				}
			}
			edMapper.clear();
		}
	}

	@Override
	public Object addingService(ServiceReference reference)
	{
		Object service = definition.getBundleContext().getService(reference);
		if (service instanceof GeoEventDefinitionManager)
			this.geoEventDefinitionManager = (GeoEventDefinitionManager) service;
		return service;
	}

	@Override
	public void modifiedService(ServiceReference reference, Object service)
	{
		;
	}

	@Override
	public void removedService(ServiceReference reference, Object service)
	{
		if (service instanceof GeoEventDefinitionManager)
		{
			clearGeoEventDefinitionMapper();
			this.geoEventDefinitionManager = null;
		}
	}

	public void setGeoEventCreator(GeoEventCreator geoEventCreator)
	{
		this.geoEventCreator = geoEventCreator;
	}

	public void setTagManager(TagManager tagManager)
	{
		this.tagManager = tagManager;
	}

	@Override
	public boolean isCacheRequired()
	{
		return new ExpressionBuilder(null, geoEventCache, expression).isCacheRequired();
	}
}
