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

public abstract class ArrayUtil
{
  public static Object[] reverse(Object[] data)
  {
    int left = 0;
    int right = data.length - 1;
    while (left < right)
    {
      // swap the values at the left and right indices
      Object temp = data[left];
      data[left] = data[right];
      data[right] = temp;
      // move the left and right index pointers in toward the center
      left++;
      right--;
    }
    return data;
  }
}