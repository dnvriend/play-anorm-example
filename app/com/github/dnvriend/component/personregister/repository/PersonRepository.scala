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

package com.github.dnvriend.component.personregister.repository

import javax.inject.Inject

import anorm._
import com.github.dnvriend.component.personregister.Person
import play.api.db.Database

class PersonRepository @Inject() (db: Database) {
  def getAll(limit: Int, offset: Int): List[Person] = db.withConnection { implicit conn =>
    SQL"""SELECT * FROM "PERSON" LIMIT $limit OFFSET $offset""".as(Person.namedParser.*)
  }

  def getById(id: Long): Option[Person] = db.withConnection { implicit conn =>
    SQL"""SELECT * FROM "PERSON" WHERE id=$id""".as(Person.namedParser.singleOpt)
  }

  def save(name: String, age: Int): Option[Long] = db.withConnection { implicit conn =>
    SQL"""INSERT INTO "PERSON" ("name", "age") VALUES ($name, $age)""".executeInsert()
  }

  def updateById(id: Long, name: String, age: Int): Int = db.withConnection { implicit conn =>
    SQL"""UPDATE "PERSON" SET "name"=$name, "age"=$age WHERE "id"=$id""".executeUpdate
  }

  def deleteById(id: Long): Int = db.withConnection { implicit conn =>
    SQL"""DELETE "PERSON" WHERE "id"=$id""".executeUpdate()
  }
}

