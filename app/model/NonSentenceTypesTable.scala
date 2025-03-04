package model

import java.time.LocalDateTime
// AUTO-GENERATED Slick data model for table NonSentenceTypes
trait NonSentenceTypesTable {

  self:TablesRoot  =>

  import profile.api._
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}
  /** Entity class storing rows of table NonSentenceTypes
   *  @param id Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey
   *  @param name Database column name SqlType(VARCHAR), Length(255,true)
   *  @param createdAt Database column created_at SqlType(DATETIME)
   *  @param updatedAt Database column updated_at SqlType(DATETIME) */
  case class NonSentenceTypesRow(id: Long, name: String, createdAt: LocalDateTime, updatedAt: LocalDateTime)
  /** GetResult implicit for fetching NonSentenceTypesRow objects using plain SQL queries */
  implicit def GetResultNonSentenceTypesRow(implicit e0: GR[Long], e1: GR[String], e2: GR[LocalDateTime]): GR[NonSentenceTypesRow] = GR{
    prs => import prs._
    NonSentenceTypesRow.tupled((<<[Long], <<[String], <<[LocalDateTime], <<[LocalDateTime]))
  }
  /** Table description of table non_sentence_types. Objects of this class serve as prototypes for rows in queries. */
  class NonSentenceTypes(_tableTag: Tag) extends profile.api.Table[NonSentenceTypesRow](_tableTag, Some("toposoiddb"), "non_sentence_types") {
    def * = (id, name, createdAt, updatedAt).<>(NonSentenceTypesRow.tupled, NonSentenceTypesRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(name), Rep.Some(createdAt), Rep.Some(updatedAt))).shaped.<>({r=>import r._; _1.map(_=> NonSentenceTypesRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(VARCHAR), Length(255,true) */
    val name: Rep[String] = column[String]("name", O.Length(255,varying=true))
    /** Database column created_at SqlType(DATETIME) */
    val createdAt: Rep[LocalDateTime] = column[LocalDateTime]("created_at")
    /** Database column updated_at SqlType(DATETIME) */
    val updatedAt: Rep[LocalDateTime] = column[LocalDateTime]("updated_at")
  }
  /** Collection-like TableQuery object for table NonSentenceTypes */
  lazy val NonSentenceTypes = new TableQuery(tag => new NonSentenceTypes(tag))
}
