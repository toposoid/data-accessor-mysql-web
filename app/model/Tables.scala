package model
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends Tables {
  val profile = profiles.MySQLProfile
}

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.)
    Each generated XXXXTable trait is mixed in this trait hence allowing access to all the TableQuery lazy vals.
  */
trait Tables extends TablesRoot with DocumentAnalysisResultHistoryTable with KnowledgeRegisterHistoryTable with KnowledgeRegisterStatesTable {
  val profile: slick.jdbc.JdbcProfile
  import profile.api._
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = DocumentAnalysisResultHistory.schema ++ KnowledgeRegisterHistory.schema ++ KnowledgeRegisterStates.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

}