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

package dao

import model.Tables.{DocumentAnalysisResultHistory, DocumentAnalysisResultHistoryRow}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.Inject
import scala.concurrent.{Await, ExecutionContext}
import scala.concurrent.duration.Duration

class DocumentAnalysisResultHistoryDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile]{

  import profile.api._

  /** register */
  def add(u: DocumentAnalysisResultHistoryRow)(implicit ec: ExecutionContext): Unit = {
    Await.result(db.run(DocumentAnalysisResultHistory += u), Duration.Inf)
  }

  def searchByDocumentIdAndStateId(documentId: String, stateId:Long)(implicit ec: ExecutionContext): Seq[DocumentAnalysisResultHistoryRow] = {
    val search = db.run(DocumentAnalysisResultHistory.filter( x => x.documentId === documentId && x.stateId === stateId).result)
    Await.result(search, Duration.Inf)
  }

}
