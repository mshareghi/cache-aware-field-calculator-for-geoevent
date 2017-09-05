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

public class ParenthesisToken extends Token
{
  ParenthesisToken(String value)
  {
    super(value);
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj instanceof ParenthesisToken)
    {
      final ParenthesisToken t = (ParenthesisToken) obj;
      return t.getValue().equals(this.getValue());
    }
    return false;
  }

  @Override
  public int hashCode()
  {
    return getValue().hashCode();
  }

  /**
   * check the direction of the parenthesis
   * 
   * @return true if it's a left parenthesis (open) false if it is a right
   *         parenthesis (closed)
   */
  boolean isOpen()
  {
    return getValue().equals("(") || getValue().equals("[") || getValue().equals("{");
  }

  @Override
  void mutateStackForInfixTranslation(Stack<Token> operatorStack, List<Token> tokens)
  {
    if (this.isOpen())
      operatorStack.push(this);
    else
    {
      Token next;
      while ((next = operatorStack.peek()) instanceof OperatorToken || next instanceof FunctionToken ||(next instanceof ParenthesisToken && !((ParenthesisToken) next).isOpen()))
        tokens.add(operatorStack.pop());
      operatorStack.pop();
    }
  }
}