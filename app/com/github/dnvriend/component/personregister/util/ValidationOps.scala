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

import play.api.data.validation.ValidationError
import play.api.libs.json.{ Format, JsPath, JsResult }
import play.api.mvc.{ AnyContent, Request }

import scalaz._
import Scalaz._

object ValidationOps {
  implicit class ValImplicits[A](val that: ValidationNel[String, A]) extends AnyVal {
    def toDisjunction: Disjunction[String, A] = that.disjunction.leftMap(_.toList.mkString(","))
    def toDisjunctionNel: Disjunction[NonEmptyList[String], A] = that.disjunction
  }

  def jsResultToValidationNel[A](jsResult: JsResult[A]): ValidationNel[String, A] = {
    def validationToString(path: JsPath, xs: Seq[ValidationError]): String = {
      val pathString = path.toString
      val errorsString = xs.flatMap(_.messages).mkString(",")
      s"'$pathString', '$errorsString'"
    }
    def validationErrorsToString(xs: Seq[(JsPath, Seq[ValidationError])]): String =
      xs.map((validationToString _).tupled).mkString(",")

    jsResult.asEither
      .validation
      .leftMap(validationErrorsToString)
      .leftMap(_.wrapNel)
  }

  implicit class JsResultOps[A](val that: JsResult[A]) extends AnyVal {
    def toValidationNel: ValidationNel[String, A] =
      jsResultToValidationNel(that)
  }

  implicit class OptionJsResultOps[A](val that: Option[JsResult[A]]) extends AnyVal {
    def toValidationNel: ValidationNel[String, A] =
      that.map(jsResultToValidationNel).getOrElse("No JsResult to validate".failureNel[A])
  }

  implicit class RequestOps(val that: Request[AnyContent]) extends AnyVal {
    def toValidationNel[A: Format]: ValidationNel[String, A] =
      that.body.asJson.map(_.validate[A]).toValidationNel
  }
}

