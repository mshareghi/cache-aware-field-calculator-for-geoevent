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

package com.esri.geoevent.processor.cacheawarefieldcalculator.expression;

import java.util.List;
import java.util.Stack;

import com.esri.ges.core.geoevent.GeoEvent;

/**
 * A {@link Token} for Numbers
 * 
 */
public class ObjectToken extends CalculationToken
{
  private final Object value;

  /**
   * construct a new {@link ObjectToken}
   * 
   * @param value
   *          the value of the Object as a {@link String}
   */
  ObjectToken(Object value)
  {
    super(value.toString());
    this.value = value;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj instanceof ObjectToken)
    {
      final ObjectToken t = (ObjectToken) obj;
      return t.getValue().equals(this.getValue());
    }
    return false;
  }

  @Override
  public int hashCode()
  {
    return getValue().hashCode();
  }

  @Override
  void mutateStackForCalculation(Stack<Object> stack, GeoEvent geoEvent)
  {
    stack.push(value instanceof String ? ((String)value).replaceAll("'", "") : value);
  }

  @Override
  void mutateStackForInfixTranslation(Stack<Token> operatorStack, List<Token> tokens)
  {
    tokens.add(this);
  }
}