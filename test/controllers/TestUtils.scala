/*
 * Copyright 2021 Linked Ideal LLC.[https://linked-ideal.com/]
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

package controllers

import model.Tables.{DocumentAnalysisResultHistory, DocumentAnalysisResultStates, KnowledgeRegisterHistory, KnowledgeRegisterStates, NonSentenceSections, NonSentenceTypes}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.Inject
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.io.Source

class TestUtils @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile]{

  import profile.api._

  def deleteAll(tableName:String)={
    Await.result(db.run(sqlu"TRUNCATE TABLE toposoiddb.#$tableName"),Duration.Inf)
  }

  def createTable() = {
    val schema = DocumentAnalysisResultHistory.schema ++ DocumentAnalysisResultStates.schema ++ KnowledgeRegisterHistory.schema ++ KnowledgeRegisterStates.schema ++ NonSentenceSections.schema ++ NonSentenceTypes.schema
    Await.result(db.run(schema.dropIfExists),Duration.Inf)
    Await.result(db.run(schema.create),Duration.Inf)
  }
}
