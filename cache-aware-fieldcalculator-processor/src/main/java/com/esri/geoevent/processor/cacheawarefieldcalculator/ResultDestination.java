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

import com.esri.ges.util.Validator;

public enum ResultDestination
{
  EXISTING_FIELD, NEW_FIELD;
  
  @Override
  public String toString()
  {
    switch(this)
    {
      case EXISTING_FIELD:
        return "Existing Field";
      case NEW_FIELD:
        return "New Field";
      default:
        return "";
    }
  }
  
  public static ResultDestination fromString(String s) throws IllegalArgumentException
  {
    String name = Validator.compactSpaces(s).trim();
    if (!name.isEmpty())
    {
      if ("Existing Field".equals(name))
        return ResultDestination.EXISTING_FIELD;
      if ("New Field".equals(name))
        return ResultDestination.NEW_FIELD;
    }
    throw new IllegalArgumentException("Illegal result destination '" + name + "' specified.");
  }
}