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

public class RPNExpression implements Calculable
{
  final List<Token> tokens;
  final GeoEvent geoEvent;

  public RPNExpression(List<Token> tokens, final GeoEvent geoEvent)
  {
    super();
    this.tokens = tokens;
    this.geoEvent = geoEvent;
  }
  
  /**
   * calculate the result of the expression
   * 
   * @return the result of the calculation
   * @throws IllegalArgumentException
   *           if the variables are invalid
   */
  public Object calculate() throws IllegalArgumentException
  {
    final Stack<Object> stack = new Stack<Object>();
    for (final Token t : tokens)
      ((CalculationToken)t).mutateStackForCalculation(stack, geoEvent);
    return stack.pop();
  }
  
  public String getExpression()
  {
    StringBuilder sb = new StringBuilder("");
    for (Token token : tokens)
      sb.append(token.getValue()).append(" ");
    return sb.toString().trim();
  }
}