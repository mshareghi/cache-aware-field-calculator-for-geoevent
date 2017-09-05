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

import com.esri.ges.core.component.ComponentException;
import com.esri.ges.manager.tag.TagManager;
import com.esri.ges.messaging.GeoEventCreator;
import com.esri.ges.messaging.Messaging;
import com.esri.ges.processor.GeoEventProcessor;
import com.esri.ges.processor.GeoEventProcessorServiceBase;

public class CacheAwareFieldCalculatorService extends GeoEventProcessorServiceBase
{
  private GeoEventCreator geoEventCreator;
  private TagManager tagManager;
  
  public CacheAwareFieldCalculatorService()
  {
    definition = new CacheAwareFieldCalculatorDefinition();
  }
  
  @Override
  public GeoEventProcessor create() throws ComponentException
  {
    CacheAwareFieldCalculator fieldCalculator = new CacheAwareFieldCalculator(definition);
    fieldCalculator.setGeoEventCreator(geoEventCreator);
    fieldCalculator.setTagManager(tagManager);
    return fieldCalculator;
  }
  
  public void setMessaging(Messaging messaging)
  {
    this.geoEventCreator = messaging.createGeoEventCreator();
  }
  
  public void setTagManager(TagManager tagManager)
  {
    this.tagManager = tagManager;
  }
}