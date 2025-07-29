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

  def searchLatestStateByDocumentId(documentId: String)(implicit ec: ExecutionContext): Seq[DocumentAnalysisResultHistoryRow] = {
    val search = db.run(DocumentAnalysisResultHistory.filter(x => x.documentId === documentId).sortBy( x => (x.id.desc)).result)
    Await.result(search, Duration.Inf)
  }


}
