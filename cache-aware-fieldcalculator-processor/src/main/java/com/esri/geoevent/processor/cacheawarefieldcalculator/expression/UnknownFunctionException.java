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

/**
 * Exception for handling unknown Functions.
 * 
 * @see FunctionToken
 */
public class UnknownFunctionException extends Exception
{
  private static final long serialVersionUID = 1L;

  /**
   * construct a new {@link UnknownFunctionException}
   * 
   * @param functionName
   *          the function name which could not be found
   */
  public UnknownFunctionException(String functionName)
  {
    super("Unknown function: " + functionName);
  }
}