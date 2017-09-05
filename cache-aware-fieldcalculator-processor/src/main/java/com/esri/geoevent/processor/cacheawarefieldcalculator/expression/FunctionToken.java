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

public class FunctionToken extends CalculationToken
{
  final String functionName;
  final Function function;

  FunctionToken(String value, Function function) throws UnknownFunctionException
  {
    super(value);
    if (value == null)
      throw new UnknownFunctionException(value);
    try
    {
      this.functionName = function.name;
      this.function = function;
    }
    catch (IllegalArgumentException e)
    {
      throw new UnknownFunctionException(value);
    }
  }

  String getName()
  {
    return functionName;
  }

  @Override
  public boolean equals(Object obj)
  {
    return (obj instanceof FunctionToken) ? functionName.equals(((FunctionToken) obj).functionName) : false;
  }

  @Override
  public int hashCode()
  {
    return functionName.hashCode();
  }

  @Override
  void mutateStackForCalculation(Stack<Object> stack, GeoEvent geoEvent)
  {
    Object[] args = new Object[function.argc];
    for (int i=0; i < function.argc; i++)
      args[i] = stack.isEmpty() ? null : stack.pop();
    stack.push(function.applyFunction(ArrayUtil.reverse(args)));
  }
  
  @Override
  void mutateStackForInfixTranslation(Stack<Token> operatorStack, List<Token> tokens)
  {
    operatorStack.push(this);
  }
}