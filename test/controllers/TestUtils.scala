/*
 * Copyright (C) 2025  Linked Ideal LLC.[https://linked-ideal.com/]
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
