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

package com.github.dnvriend.component.personregister

import play.api.libs.json.{ Format, Json }
import anorm.{ Macro, RowParser }

object Person {
  implicit val format: Format[Person] = Json.format[Person]
  val namedParser: RowParser[Person] = Macro.namedParser[Person]
}

final case class Person(name: String, age: Int, id: Option[Long] = None)

