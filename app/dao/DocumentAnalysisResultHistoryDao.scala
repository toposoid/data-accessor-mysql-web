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

  def searchByDocumentId(documentId: String)(implicit ec: ExecutionContext): Seq[DocumentAnalysisResultHistoryRow] = {
    val search = db.run(DocumentAnalysisResultHistory.filter(_.documentId === documentId).result)
    Await.result(search, Duration.Inf)
  }

}
