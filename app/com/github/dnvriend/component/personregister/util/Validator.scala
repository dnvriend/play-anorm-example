/*
 * Copyright 2016 Dennis Vriend
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.dnvriend.component.personregister.util

import scalaz._
import Scalaz._

object Validator {
  def intValidator(fieldName: String, value: Int): ValidationNel[String, Int] =
    Option(value).filter(_ >= 0).toSuccessNel(s"Field '$fieldName' with value '$value' must be gte zero")

  def idValidator(fieldName: String, value: Long): ValidationNel[String, Long] =
    Option(value).filter(_ >= 0).toSuccessNel(s"Field '$fieldName' with value '$value' must be gte zero")

  def stringValidator(fieldName: String, value: String): ValidationNel[String, String] =
    Option(value).map(_.trim).filterNot(_.isEmpty).toSuccessNel(s"Field '$fieldName' with value '$value' must not be empty")
}

