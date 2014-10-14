package com.esri.geoevent.processor.cacheawarefieldcalculator;

import com.esri.ges.core.property.PropertyDefinition;
import com.esri.ges.core.property.PropertyType;
import com.esri.ges.processor.GeoEventProcessorDefinitionBase;

public class CacheAwareFieldCalculatorDefinition extends GeoEventProcessorDefinitionBase
{
  public CacheAwareFieldCalculatorDefinition()
  {
    try
    {
      propertyDefinitions.put("expression", new PropertyDefinition("expression", PropertyType.String, "", "Expression", "Expression", true, false));
      propertyDefinitions.put("resultDestination", new PropertyDefinition("resultDestination", PropertyType.String, "Existing Field", "Result Destination", "Result Destination", true, false, "Existing Field", "New Field"));
      // Case 1: Overwriting Existing Field
      propertyDefinitions.put("existingFieldName", new PropertyDefinition("existingFieldName", PropertyType.String, "", "Existing Field Name", "Existing Field Name", "resultDestination=Existing Field", false, false));
      // Case 2: Creating New Field
      propertyDefinitions.put("newFieldName", new PropertyDefinition("newFieldName", PropertyType.String, "", "New Field Name", "New Field Name", "resultDestination=New Field", false, false));
      propertyDefinitions.put("newFieldType", new PropertyDefinition("newFieldType", PropertyType.String, "", "New Field Type", "New Field Type", "resultDestination=New Field", false, false, "Boolean", "Date", "Double", "Float", "Geometry", "Integer", "Long", "Short", "String"));
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
  public String getLabel()
  {
    return "Cache-Aware Field Calculator";
  }
  
  @Override
  public String getDescription()
  {
    return "Calculates new data values from existing data values in GeoEvents using a mathematical (or text manipulation) expression, and enriches the GeoEvents with the results. Allows access to previous GeoEvent using syntax previousGeoEvent('fieldname')";
  }
}