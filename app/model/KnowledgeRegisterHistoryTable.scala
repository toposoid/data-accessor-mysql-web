package model

import java.time.LocalDateTime
// AUTO-GENERATED Slick data model for table KnowledgeRegisterHistory
trait KnowledgeRegisterHistoryTable {

  self:TablesRoot  =>

  import profile.api._
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}
  /** Entity class storing rows of table KnowledgeRegisterHistory
   *  @param id Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey
   *  @param userId Database column user_id SqlType(VARCHAR), Length(1024,true)
   *  @param stateId Database column state_id SqlType(BIGINT)
   *  @param documentId Database column document_id SqlType(VARCHAR), Length(1024,true)
   *  @param sequentialNumber Database column sequential_number SqlType(INT)
   *  @param propositionId Database column proposition_id SqlType(VARCHAR), Length(1024,true)
   *  @param sentences Database column sentences SqlType(TEXT)
   *  @param json Database column json SqlType(TEXT)
   *  @param createdAt Database column created_at SqlType(DATETIME)
   *  @param updatedAt Database column updated_at SqlType(DATETIME) */
  case class KnowledgeRegisterHistoryRow(id: Long, userId: String, stateId: Long, documentId: String, sequentialNumber: Int, propositionId: String, sentences: String, json: String, createdAt: LocalDateTime, updatedAt: LocalDateTime)
  /** GetResult implicit for fetching KnowledgeRegisterHistoryRow objects using plain SQL queries */
  implicit def GetResultKnowledgeRegisterHistoryRow(implicit e0: GR[Long], e1: GR[String], e2: GR[Int], e3: GR[LocalDateTime]): GR[KnowledgeRegisterHistoryRow] = GR{
    prs => import prs._
    KnowledgeRegisterHistoryRow.tupled((<<[Long], <<[String], <<[Long], <<[String], <<[Int], <<[String], <<[String], <<[String], <<[LocalDateTime], <<[LocalDateTime]))
  }
  /** Table description of table knowledge_register_history. Objects of this class serve as prototypes for rows in queries. */
  class KnowledgeRegisterHistory(_tableTag: Tag) extends profile.api.Table[KnowledgeRegisterHistoryRow](_tableTag, Some("toposoiddb"), "knowledge_register_history") {
    def * = (id, userId, stateId, documentId, sequentialNumber, propositionId, sentences, json, createdAt, updatedAt).<>(KnowledgeRegisterHistoryRow.tupled, KnowledgeRegisterHistoryRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(userId), Rep.Some(stateId), Rep.Some(documentId), Rep.Some(sequentialNumber), Rep.Some(propositionId), Rep.Some(sentences), Rep.Some(json), Rep.Some(createdAt), Rep.Some(updatedAt))).shaped.<>({r=>import r._; _1.map(_=> KnowledgeRegisterHistoryRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get, _10.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column user_id SqlType(VARCHAR), Length(1024,true) */
    val userId: Rep[String] = column[String]("user_id", O.Length(1024,varying=true))
    /** Database column state_id SqlType(BIGINT) */
    val stateId: Rep[Long] = column[Long]("state_id")
    /** Database column document_id SqlType(VARCHAR), Length(1024,true) */
    val documentId: Rep[String] = column[String]("document_id", O.Length(1024,varying=true))
    /** Database column sequential_number SqlType(INT) */
    val sequentialNumber: Rep[Int] = column[Int]("sequential_number")
    /** Database column proposition_id SqlType(VARCHAR), Length(1024,true) */
    val propositionId: Rep[String] = column[String]("proposition_id", O.Length(1024,varying=true))
    /** Database column sentences SqlType(TEXT) */
    val sentences: Rep[String] = column[String]("sentences")
    /** Database column json SqlType(TEXT) */
    val json: Rep[String] = column[String]("json")
    /** Database column created_at SqlType(DATETIME) */
    val createdAt: Rep[LocalDateTime] = column[LocalDateTime]("created_at")
    /** Database column updated_at SqlType(DATETIME) */
    val updatedAt: Rep[LocalDateTime] = column[LocalDateTime]("updated_at")
  }
  /** Collection-like TableQuery object for table KnowledgeRegisterHistory */
  lazy val KnowledgeRegisterHistory = new TableQuery(tag => new KnowledgeRegisterHistory(tag))
}
