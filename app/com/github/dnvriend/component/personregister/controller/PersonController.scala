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

package com.github.dnvriend.component.personregister.controller

import javax.inject.Inject

import com.github.dnvriend.component.personregister.Person
import com.github.dnvriend.component.personregister.repository.PersonRepository
import com.github.dnvriend.component.personregister.util.DisjunctionOps._
import com.github.dnvriend.component.personregister.util.ValidationOps._
import com.github.dnvriend.component.personregister.util.Validator
import io.swagger.annotations._
import play.api.mvc._

import scalaz._
import Scalaz._

@Api(value = "/api/person")
class PersonController @Inject() (personRepository: PersonRepository) extends Controller {

  @ApiOperation(value = "Get all person", response = classOf[Person], httpMethod = "GET")
  @ApiResponses(Array(new ApiResponse(code = 200, message = "Return a list of person")))
  def getAll(@ApiParam(value = "Fetch number of items") limit: Int, @ApiParam(value = "Fetch from offset") offset: Int): Action[AnyContent] = Action(for {
    (limit, offset) <- (Validator.intValidator("limit", limit) tuple Validator.intValidator("offset", offset)).toDisjunction
    xs <- tryCatch(personRepository.getAll(limit, offset))
  } yield xs)

  @ApiOperation(value = "Get person by id", response = classOf[Person], httpMethod = "GET")
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "Returns person when found"),
    new ApiResponse(code = 404, message = "Returns 404 when not found")
  ))
  def getById(@ApiParam(value = "id of person") id: Long): Action[AnyContent] = Action(for {
    id <- Validator.idValidator("id", id).toDisjunction
    person <- tryCatch(personRepository.getById(id))
  } yield person)

  @ApiOperation(value = "Save person", response = classOf[Person], httpMethod = "POST")
  @ApiResponses(Array(new ApiResponse(code = 200, message = "Returns the stored person with id")))
  def save(): Action[AnyContent] = Action(request => for {
    person <- request.toValidationNel[Person].toDisjunction
    id <- tryCatch(personRepository.save(person.name, person.age))
  } yield person.copy(id = id))

  @ApiOperation(value = "Update person by id", response = classOf[Person], httpMethod = "PUT")
  @ApiResponses(Array(new ApiResponse(code = 200, message = "Returns the updated person")))
  def updateById(@ApiParam(value = "id of person") id: Long): Action[AnyContent] = Action(request => for {
    id <- Validator.idValidator("id", id).toDisjunction
    person <- request.toValidationNel[Person].toDisjunction
    _ <- tryCatch(personRepository.updateById(id, person.name, person.age))
  } yield person)

  @ApiOperation(value = "Delete person by id", httpMethod = "DELETE")
  @ApiResponses(Array(new ApiResponse(code = 204, message = "Returns no content")))
  def deleteById(@ApiParam(value = "id of person") id: Long): Action[AnyContent] = Action(for {
    id <- Validator.idValidator("id", id).toDisjunction
    _ <- tryCatch(personRepository.deleteById(id))
  } yield ())
}

