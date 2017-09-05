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

import java.util.ArrayList;
import java.util.List;

import com.esri.ges.core.property.LabeledValue;
import com.esri.ges.core.property.PropertyDefinition;
import com.esri.ges.core.property.PropertyType;
import com.esri.ges.processor.GeoEventProcessorDefinitionBase;

public class CacheAwareFieldCalculatorDefinition extends GeoEventProcessorDefinitionBase
{
  public CacheAwareFieldCalculatorDefinition()
  {
    try
    {
    	List<LabeledValue> resultDestinationAllowableValues = new ArrayList<>();
    	resultDestinationAllowableValues.add(new LabeledValue("Existing Field", "Existing Field"));
    	resultDestinationAllowableValues.add(new LabeledValue("New Field", "New Field"));
      propertyDefinitions.put("expression", new PropertyDefinition("expression", PropertyType.String, "", "Expression", "Expression", true, false));

      propertyDefinitions.put("resultDestination", new PropertyDefinition("resultDestination", PropertyType.String, "Existing Field", "Result Destination", "Result Destination", true, false, resultDestinationAllowableValues));
      // Case 1: Overwriting Existing Field
      propertyDefinitions.put("existingFieldName", new PropertyDefinition("existingFieldName", PropertyType.String, "", "Existing Field Name", "Existing Field Name", "resultDestination=Existing Field", false, false));
      // Case 2: Creating New Field
      propertyDefinitions.put("newFieldName", new PropertyDefinition("newFieldName", PropertyType.String, "", "New Field Name", "New Field Name", "resultDestination=New Field", false, false));
      
      List<LabeledValue> newFieldTypeAllowableValues = new ArrayList<>();
      newFieldTypeAllowableValues.add(new LabeledValue("Boolean", "Boolean"));
      newFieldTypeAllowableValues.add(new LabeledValue("Date", "Date"));
      newFieldTypeAllowableValues.add(new LabeledValue("Double", "Double"));
      newFieldTypeAllowableValues.add(new LabeledValue("Float", "Float"));
      newFieldTypeAllowableValues.add(new LabeledValue("Geometry", "Geometry"));
      newFieldTypeAllowableValues.add(new LabeledValue("Integer", "Integer"));
      newFieldTypeAllowableValues.add(new LabeledValue("Long", "Long"));
      newFieldTypeAllowableValues.add(new LabeledValue("Short", "Short"));
      newFieldTypeAllowableValues.add(new LabeledValue("String", "String"));
      propertyDefinitions.put("newFieldType", new PropertyDefinition("newFieldType", PropertyType.String, "", "New Field Type", "New Field Type", "resultDestination=New Field", false, false, newFieldTypeAllowableValues));
      propertyDefinitions.put("newFieldTag", new PropertyDefinition("newFieldTag", PropertyType.String, "", "New Field Tag", "New Field Tag", "resultDestination=New Field", false, false));
      propertyDefinitions.put("newGeoEventDefinitionName", new PropertyDefinition("newGeoEventDefinitionName", PropertyType.String, "FieldCalculator", "Resulting GeoEvent Definition Name", "Resulting GeoEvent Definition Name", "resultDestination=New Field", false, false));
    }
    catch(Exception e)
    {
      ;
    }
  }
  
  @Override
  public String getName()
  {
    return "CacheAwareFieldCalculator";
  }
  
  @Override
  public String getDomain()
  {
  	return "com.esri.geoevent.processor";
  }
  
  @Override
  public String getVersion()
  {
  	return "10.5.0";
  }
  
  @Override
  public String getLabel()
  {
    return "${com.esri.geoevent.processor.cache-aware-fieldcalculator-processor.PROCESSOR_LABEL}";
  }
  
  @Override
  public String getDescription()
  {
    return "${com.esri.geoevent.processor.cache-aware-fieldcalculator-processor.PROCESSOR_DESC}";
  }
}