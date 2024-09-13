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

package controllers

import com.ideal.linked.toposoid.common.{TRANSVERSAL_STATE, ToposoidUtils, TransversalState}
import com.ideal.linked.toposoid.knowledgebase.regist.rdb.model.DocumentAnalysisResultRecord
import com.typesafe.scalalogging.LazyLogging
import dao.DocumentAnalysisResultHistoryDao
import model.Tables.DocumentAnalysisResultHistoryRow
import play.api.libs.json.Json
import play.api.mvc.{BaseController, ControllerComponents}
import play.api.libs.json.{Json, OWrites, Reads}

import javax.inject.Inject
import java.time.LocalDateTime
import scala.concurrent.ExecutionContext

/*
case class DocumentAnalysisResultRecord(documentId:String, originalFilename:String,totalSeparatedNumber:Int)
object DocumentAnalysisResultRecord {
  implicit val jsonWrites: OWrites[DocumentAnalysisResultRecord] = Json.writes[DocumentAnalysisResultRecord]
  implicit val jsonReads: Reads[DocumentAnalysisResultRecord] = Json.reads[DocumentAnalysisResultRecord]
}
*/
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
