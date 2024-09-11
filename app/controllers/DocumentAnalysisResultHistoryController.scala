package controllers

import com.ideal.linked.toposoid.common.{TRANSVERSAL_STATE, ToposoidUtils, TransversalState}
import com.typesafe.scalalogging.LazyLogging
import dao.DocumentAnalysisResultHistoryDao
import model.Tables.DocumentAnalysisResultHistoryRow
import play.api.libs.json.Json
import play.api.mvc.{BaseController, ControllerComponents}
import play.api.libs.json.{Json, OWrites, Reads}

import javax.inject.Inject
import java.time.LocalDateTime
import scala.concurrent.ExecutionContext

case class DocumentAnalysisResultRecord(documentId:String, originalFilename:String,totalSeparatedNumber:Int)
object DocumentAnalysisResultRecord {
  implicit val jsonWrites: OWrites[DocumentAnalysisResultRecord] = Json.writes[DocumentAnalysisResultRecord]
  implicit val jsonReads: Reads[DocumentAnalysisResultRecord] = Json.reads[DocumentAnalysisResultRecord]
}

class DocumentAnalysisResultHistoryController @Inject()(documentAnalysisResultHistoryDao:DocumentAnalysisResultHistoryDao, val controllerComponents: ControllerComponents)(implicit ec: ExecutionContext) extends BaseController  with LazyLogging{

  def add()  = Action(parse.json) { request =>
    val transversalState = Json.parse(request.headers.get(TRANSVERSAL_STATE.str).get).as[TransversalState]
    try {
      val json = request.body
      val documentAnalysisResult: DocumentAnalysisResultRecord = Json.parse(json.toString).as[DocumentAnalysisResultRecord]
      val documentAnalysisResultHistory = DocumentAnalysisResultHistoryRow(
        id = 0,
        userId = transversalState.userId,
        documentId = documentAnalysisResult.documentId,
        originalFilename = documentAnalysisResult.originalFilename,
        totalSeparatedNumber = documentAnalysisResult.totalSeparatedNumber,
        createdAt = LocalDateTime.now,
        updatedAt = LocalDateTime.now)
      documentAnalysisResultHistoryDao.add(documentAnalysisResultHistory)
      Ok(Json.obj("status" -> "Ok", "message" -> ""))
    } catch {
      case e: Exception => {
        logger.error(ToposoidUtils.formatMessageForLogger(e.toString, transversalState.userId), e)
        BadRequest(Json.obj("status" -> "Error", "message" -> e.toString()))
      }
    }
  }

  def searchByDocumentId = Action(parse.json) { request =>
    val transversalState = Json.parse(request.headers.get(TRANSVERSAL_STATE.str).get).as[TransversalState]
    try {
      val json = request.body
      val documentAnalysisResult: DocumentAnalysisResultRecord = Json.parse(json.toString).as[DocumentAnalysisResultRecord]
      val result = documentAnalysisResultHistoryDao.searchByDocumentId(documentAnalysisResult.documentId).head
      val convertDocumentAnalysisResult = DocumentAnalysisResultRecord(
        documentId = result.documentId,
        originalFilename = result.originalFilename,
        totalSeparatedNumber = result.totalSeparatedNumber)
      Ok(Json.toJson(convertDocumentAnalysisResult))
    } catch {
      case e: Exception => {
        logger.error(ToposoidUtils.formatMessageForLogger(e.toString, transversalState.userId), e)
        BadRequest(Json.obj("status" -> "Error", "message" -> e.toString()))
      }
    }
  }

}
