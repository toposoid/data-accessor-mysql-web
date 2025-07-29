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
   *  @param userId Database column user_id SqlType(VARCHAR), Length(512,true)
   *  @param stateId Database column state_id SqlType(BIGINT)
   *  @param documentId Database column document_id SqlType(VARCHAR), Length(512,true)
   *  @param originalFilename Database column original_filename SqlType(TEXT)
   *  @param totalSeparatedNumber Database column total_separated_number SqlType(INT)
   *  @param createdAt Database column created_at SqlType(DATETIME)
   *  @param updatedAt Database column updated_at SqlType(DATETIME) */
  case class DocumentAnalysisResultHistoryRow(id: Long, userId: String, stateId: Long, documentId: String, originalFilename: String, totalSeparatedNumber: Int, createdAt: LocalDateTime, updatedAt: LocalDateTime)
  /** GetResult implicit for fetching DocumentAnalysisResultHistoryRow objects using plain SQL queries */
  implicit def GetResultDocumentAnalysisResultHistoryRow(implicit e0: GR[Long], e1: GR[String], e2: GR[Int], e3: GR[LocalDateTime]): GR[DocumentAnalysisResultHistoryRow] = GR{
    prs => import prs._
    DocumentAnalysisResultHistoryRow.tupled((<<[Long], <<[String], <<[Long], <<[String], <<[String], <<[Int], <<[LocalDateTime], <<[LocalDateTime]))
  }
  /** Table description of table document_analysis_result_history. Objects of this class serve as prototypes for rows in queries. */
  class DocumentAnalysisResultHistory(_tableTag: Tag) extends profile.api.Table[DocumentAnalysisResultHistoryRow](_tableTag, Some("toposoiddb"), "document_analysis_result_history") {
    def * = (id, userId, stateId, documentId, originalFilename, totalSeparatedNumber, createdAt, updatedAt).<>(DocumentAnalysisResultHistoryRow.tupled, DocumentAnalysisResultHistoryRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(userId), Rep.Some(stateId), Rep.Some(documentId), Rep.Some(originalFilename), Rep.Some(totalSeparatedNumber), Rep.Some(createdAt), Rep.Some(updatedAt))).shaped.<>({r=>import r._; _1.map(_=> DocumentAnalysisResultHistoryRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column user_id SqlType(VARCHAR), Length(512,true) */
    val userId: Rep[String] = column[String]("user_id", O.Length(512,varying=true))
    /** Database column state_id SqlType(BIGINT) */
    val stateId: Rep[Long] = column[Long]("state_id")
    /** Database column document_id SqlType(VARCHAR), Length(512,true) */
    val documentId: Rep[String] = column[String]("document_id", O.Length(512,varying=true))
    /** Database column original_filename SqlType(TEXT) */
    val originalFilename: Rep[String] = column[String]("original_filename")
    /** Database column total_separated_number SqlType(INT) */
    val totalSeparatedNumber: Rep[Int] = column[Int]("total_separated_number")
    /** Database column created_at SqlType(DATETIME) */
    val createdAt: Rep[LocalDateTime] = column[LocalDateTime]("created_at")
    /** Database column updated_at SqlType(DATETIME) */
    val updatedAt: Rep[LocalDateTime] = column[LocalDateTime]("updated_at")

    /** Index over (documentId) (database name document_analysis_result_history_document_id_idx) */
    val index1 = index("document_analysis_result_history_document_id_idx", documentId)
    /** Index over (userId) (database name document_analysis_result_history_user_id_idx) */
    val index2 = index("document_analysis_result_history_user_id_idx", userId)
  }
  /** Collection-like TableQuery object for table DocumentAnalysisResultHistory */
  lazy val DocumentAnalysisResultHistory = new TableQuery(tag => new DocumentAnalysisResultHistory(tag))
}
