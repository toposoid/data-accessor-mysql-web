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

package model

import java.time.LocalDateTime
// AUTO-GENERATED Slick data model for table DocumentAnalysisResultHistory
trait DocumentAnalysisResultHistoryTable {

  self:TablesRoot  =>

  import profile.api._
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}
  /** Entity class storing rows of table DocumentAnalysisResultHistory
   *  @param id Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey
   *  @param userId Database column user_id SqlType(VARCHAR), Length(1024,true)
   *  @param documentId Database column document_id SqlType(VARCHAR), Length(1024,true)
   *  @param originalFilename Database column original_filename SqlType(TEXT)
   *  @param totalSeparatedNumber Database column total_separated_number SqlType(INT)
   *  @param createdAt Database column created_at SqlType(DATETIME)
   *  @param updatedAt Database column updated_at SqlType(DATETIME) */
  case class DocumentAnalysisResultHistoryRow(id: Long, userId: String, documentId: String, originalFilename: String, totalSeparatedNumber: Int, createdAt: LocalDateTime, updatedAt: LocalDateTime)
  /** GetResult implicit for fetching DocumentAnalysisResultHistoryRow objects using plain SQL queries */
  implicit def GetResultDocumentAnalysisResultHistoryRow(implicit e0: GR[Long], e1: GR[String], e2: GR[Int], e3: GR[LocalDateTime]): GR[DocumentAnalysisResultHistoryRow] = GR{
    prs => import prs._
    DocumentAnalysisResultHistoryRow.tupled((<<[Long], <<[String], <<[String], <<[String], <<[Int], <<[LocalDateTime], <<[LocalDateTime]))
  }
  /** Table description of table document_analysis_result_history. Objects of this class serve as prototypes for rows in queries. */
  class DocumentAnalysisResultHistory(_tableTag: Tag) extends profile.api.Table[DocumentAnalysisResultHistoryRow](_tableTag, Some("toposoiddb"), "document_analysis_result_history") {
    def * = (id, userId, documentId, originalFilename, totalSeparatedNumber, createdAt, updatedAt).<>(DocumentAnalysisResultHistoryRow.tupled, DocumentAnalysisResultHistoryRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(userId), Rep.Some(documentId), Rep.Some(originalFilename), Rep.Some(totalSeparatedNumber), Rep.Some(createdAt), Rep.Some(updatedAt))).shaped.<>({r=>import r._; _1.map(_=> DocumentAnalysisResultHistoryRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column user_id SqlType(VARCHAR), Length(1024,true) */
    val userId: Rep[String] = column[String]("user_id", O.Length(1024,varying=true))
    /** Database column document_id SqlType(VARCHAR), Length(1024,true) */
    val documentId: Rep[String] = column[String]("document_id", O.Length(1024,varying=true))
    /** Database column original_filename SqlType(TEXT) */
    val originalFilename: Rep[String] = column[String]("original_filename")
    /** Database column total_separated_number SqlType(INT) */
    val totalSeparatedNumber: Rep[Int] = column[Int]("total_separated_number")
    /** Database column created_at SqlType(DATETIME) */
    val createdAt: Rep[LocalDateTime] = column[LocalDateTime]("created_at")
    /** Database column updated_at SqlType(DATETIME) */
    val updatedAt: Rep[LocalDateTime] = column[LocalDateTime]("updated_at")
  }
  /** Collection-like TableQuery object for table DocumentAnalysisResultHistory */
  lazy val DocumentAnalysisResultHistory = new TableQuery(tag => new DocumentAnalysisResultHistory(tag))
}
