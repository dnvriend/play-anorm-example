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

import play.api.libs.json.{ Format, Json }
import play.api.mvc.{ Result, Results }

import scala.language.implicitConversions
import scalaz._
import Scalaz._

object DisjunctionOps extends Results {
  def tryCatch[A](block: => A): Disjunction[String, A] =
    Disjunction.fromTryCatchNonFatal(block).leftMap(_.toString)

  implicit def fromEither(either: Disjunction[String, Unit]): Result =
    either.map(value => NoContent).valueOr(messages => BadRequest(messages))

  implicit def fromOption[A: Format](option: Disjunction[String, Option[A]]): Result =
    option
      .map(maybeValue => maybeValue.map(value => Ok(Json.toJson(value))) | NotFound)
      .valueOr(messages => BadRequest(messages))

  implicit def fromList[A: Format](xs: Disjunction[String, List[A]]): Result =
    xs.map(xs => Ok(Json.toJson(xs))).valueOr(messages => BadRequest(messages))

  implicit def fromAny[A: Format](any: Disjunction[String, A]): Result =
    any.map(value => Ok(Json.toJson(value))).valueOr(messages => BadRequest(messages))
}

