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

public abstract class Operator
{
  final boolean leftAssociative;
  final String symbol;
  final int precedence;
  final int operandCount;

  /**
   * Create a new {@link Operator} for two operands
   * 
   * @param symbol
   *          the symbol to be used in expressions to identify this operation
   * @param leftAssociative
   *          true is the operation is left associative
   * @param precedence
   *          the precedence of the operation
   */
  protected Operator(final String symbol, final boolean leftAssociative, final int precedence)
  {
    super();
    this.leftAssociative = leftAssociative;
    this.symbol = symbol;
    this.precedence = precedence;
    this.operandCount = 2;
  }

  /**
   * Create a new {@link Operator}
   * 
   * @param symbol
   *          the symbol to be used in expressions to identify this operation
   * @param leftAssociative
   *          true is the operation is left associative
   * @param precedence
   *          the precedence of the operation
   * @param operandCount
   *          the number of operands of the operation. A value of 1 means the
   *          operation takes one operand. Any other value means the operation
   *          takes 2 arguments.
   */
  protected Operator(final String symbol, final boolean leftAssociative, final int precedence, final int operandCount)
  {
    super();
    this.leftAssociative = leftAssociative;
    this.symbol = symbol;
    this.precedence = precedence;
    this.operandCount = operandCount == 1 ? 1 : 2;
  }

  /**
   * Create a left associative {@link Operator} with precedence value of 1
   * that uses two operands
   * 
   * @param symbol
   *          the {@link String} to use a symbol for this operation
   */
  protected Operator(final String symbol)
  {
    super();
    this.leftAssociative = true;
    this.symbol = symbol;
    this.precedence = 1;
    this.operandCount = 2;
  }

  /**
   * Create a left associative {@link Operator} for two operands
   * 
   * @param symbol
   *          the {@link String} to use a symbol for this operation
   * @param precedence
   *          the precedence of the operation
   */
  protected Operator(final String symbol, final int precedence)
  {
    super();
    this.leftAssociative = true;
    this.symbol = symbol;
    this.precedence = precedence;
    this.operandCount = 2;
  }

  /**
   * Apply the custom operation on the two operands and return the result as an
   * Object An example implementation for a multiplication could look like this:
   * 
   * <pre>
   * <code>{@code}
   *       Object applyOperation(Object[] values) {
   *           return values[0]*values[1];
   *       }
   * </pre>
   * 
   * </code>
   * 
   * @param values
   *          the operands for the operation. If the {@link Operator} uses
   *          only one operand such as a factorial the operation has to be
   *          applied to the first element of the values array. If the
   *          {@link Operator} uses two operands the operation has to be
   *          applied to the first two items in the values array, with special
   *          care given to the operator associativity. The operand to the left
   *          of the symbol is the first element in the array while the operand
   *          to the right is the second element of the array.
   * @return the result of the operation
   */
  protected abstract Object applyOperation(Object[] values);
}