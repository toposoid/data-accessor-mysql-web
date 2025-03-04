package model

import java.time.LocalDateTime
// AUTO-GENERATED Slick data model for table NonSentenceSections
trait NonSentenceSectionsTable {

  self:TablesRoot  =>

  import profile.api._
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}
  /** Entity class storing rows of table NonSentenceSections
   *  @param id Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey
   *  @param documentId Database column document_id SqlType(VARCHAR), Length(512,true)
   *  @param pageNo Database column page_no SqlType(INT)
   *  @param nonSentenceType Database column non_sentence_type SqlType(BIGINT)
   *  @param nonSentence Database column non_sentence SqlType(TEXT)
   *  @param createdAt Database column created_at SqlType(DATETIME)
   *  @param updatedAt Database column updated_at SqlType(DATETIME) */
  case class NonSentenceSectionsRow(id: Long, documentId: String, pageNo: Int, nonSentenceType: Long, nonSentence: String, createdAt: LocalDateTime, updatedAt: LocalDateTime)
  /** GetResult implicit for fetching NonSentenceSectionsRow objects using plain SQL queries */
  implicit def GetResultNonSentenceSectionsRow(implicit e0: GR[Long], e1: GR[String], e2: GR[Int], e3: GR[LocalDateTime]): GR[NonSentenceSectionsRow] = GR{
    prs => import prs._
    NonSentenceSectionsRow.tupled((<<[Long], <<[String], <<[Int], <<[Long], <<[String], <<[LocalDateTime], <<[LocalDateTime]))
  }
  /** Table description of table non_sentence_sections. Objects of this class serve as prototypes for rows in queries. */
  class NonSentenceSections(_tableTag: Tag) extends profile.api.Table[NonSentenceSectionsRow](_tableTag, Some("toposoiddb"), "non_sentence_sections") {
    def * = (id, documentId, pageNo, nonSentenceType, nonSentence, createdAt, updatedAt).<>(NonSentenceSectionsRow.tupled, NonSentenceSectionsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(documentId), Rep.Some(pageNo), Rep.Some(nonSentenceType), Rep.Some(nonSentence), Rep.Some(createdAt), Rep.Some(updatedAt))).shaped.<>({r=>import r._; _1.map(_=> NonSentenceSectionsRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column document_id SqlType(VARCHAR), Length(512,true) */
    val documentId: Rep[String] = column[String]("document_id", O.Length(512,varying=true))
    /** Database column page_no SqlType(INT) */
    val pageNo: Rep[Int] = column[Int]("page_no")
    /** Database column non_sentence_type SqlType(BIGINT) */
    val nonSentenceType: Rep[Long] = column[Long]("non_sentence_type")
    /** Database column non_sentence SqlType(TEXT) */
    val nonSentence: Rep[String] = column[String]("non_sentence")
    /** Database column created_at SqlType(DATETIME) */
    val createdAt: Rep[LocalDateTime] = column[LocalDateTime]("created_at")
    /** Database column updated_at SqlType(DATETIME) */
    val updatedAt: Rep[LocalDateTime] = column[LocalDateTime]("updated_at")

    /** Index over (documentId) (database name non_sentence_sections_document_id_idx) */
    val index1 = index("non_sentence_sections_document_id_idx", documentId)
    /** Index over (nonSentenceType) (database name non_sentence_sections_non_sentence_type_idx) */
    val index2 = index("non_sentence_sections_non_sentence_type_idx", nonSentenceType)
  }
  /** Collection-like TableQuery object for table NonSentenceSections */
  lazy val NonSentenceSections = new TableQuery(tag => new NonSentenceSections(tag))
}
