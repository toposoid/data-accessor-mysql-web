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
import com.ideal.linked.toposoid.knowledgebase.regist.rdb.model.NonSentenceSectionsRecord
import com.typesafe.scalalogging.LazyLogging
import dao.NonSentenceSectionsDao
import model.Tables.NonSentenceSectionsRow
import play.api.libs.json.Json
import play.api.mvc.{BaseController, ControllerComponents}

import java.time.LocalDateTime
import javax.inject.Inject
import scala.concurrent.ExecutionContext

/*
case class NonSentenceSectionsRecord(stateId:Long, documentId:String, sequentialNumber:Int, propositionId:String,sentences:String, json:String)
object NonSentenceSectionsRecord {
  implicit val jsonWrites: OWrites[NonSentenceSectionsRecord] = Json.writes[NonSentenceSectionsRecord]
  implicit val jsonReads: Reads[NonSentenceSectionsRecord] = Json.reads[NonSentenceSectionsRecord]
}
*/

class NonSentenceSectionsController @Inject()(nonSentenceSectionsDao:NonSentenceSectionsDao, val controllerComponents: ControllerComponents)(implicit ec: ExecutionContext) extends BaseController  with LazyLogging{
  def add() = Action(parse.json) { request =>
    val transversalState = Json.parse(request.headers.get(TRANSVERSAL_STATE.str).get).as[TransversalState]
    try {
      val json = request.body
      val nonSentenceSectionsRecord: NonSentenceSectionsRecord = Json.parse(json.toString).as[NonSentenceSectionsRecord]
      val nonSentenceSectionsRow = NonSentenceSectionsRow(
        id = 0,
        nonSentenceType = nonSentenceSectionsRecord.nonSentenceType,
        documentId = nonSentenceSectionsRecord.documentId,
        pageNo = nonSentenceSectionsRecord.pageNo,
        nonSentence = nonSentenceSectionsRecord.nonSentence,
        createdAt = LocalDateTime.now,
        updatedAt = LocalDateTime.now)

      nonSentenceSectionsDao.add(nonSentenceSectionsRow)
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
      val nonSentenceSectionsRecord: NonSentenceSectionsRecord = Json.parse(json.toString).as[NonSentenceSectionsRecord]
      val result = nonSentenceSectionsDao.searchByDocumentId(nonSentenceSectionsRecord.documentId).head
      val convertNonSentenceSectionsRecord = NonSentenceSectionsRecord(
        nonSentenceType = result.nonSentenceType,
        documentId = result.documentId,
        pageNo = result.pageNo,
        nonSentence = result.nonSentence)
      Ok(Json.toJson(convertNonSentenceSectionsRecord))
    } catch {
      case e: Exception => {
        logger.error(ToposoidUtils.formatMessageForLogger(e.toString, transversalState.userId), e)
        BadRequest(Json.obj("status" -> "Error", "message" -> e.toString()))
      }
    }
  }

}
