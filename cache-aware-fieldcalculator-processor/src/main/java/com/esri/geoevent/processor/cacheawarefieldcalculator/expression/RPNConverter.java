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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.esri.ges.core.geoevent.GeoEvent;

public abstract class RPNConverter
{

  static RPNExpression toRPNExpression(String infix, GeoEvent geoEvent, Map<String, Function> customFunctions, Map<String, Operator> operators) throws UnknownFunctionException, UnparsableExpressionException
  {
    final List<Token> tokens = new ArrayList<Token>();
    final Stack<Token> operatorStack = new Stack<Token>();
    for (final Token token : new Tokenizer(geoEvent.getGeoEventDefinition(), customFunctions, operators).getTokens(infix))
      token.mutateStackForInfixTranslation(operatorStack, tokens);
    // all tokens read, put the rest of the operations on the output;
    while (operatorStack.size() > 0)
      tokens.add(operatorStack.pop());
    return new RPNExpression(tokens, geoEvent);
  }
}