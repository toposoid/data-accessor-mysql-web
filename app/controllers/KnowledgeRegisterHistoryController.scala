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
import com.ideal.linked.toposoid.knowledgebase.regist.rdb.model.KnowledgeRegisterHistoryRecord
import com.typesafe.scalalogging.LazyLogging
import dao.KnowledgeRegisterHistoryDao
import play.api.libs.json.{Json, OWrites, Reads}
import play.api.mvc.{BaseController, ControllerComponents}
import model.Tables.KnowledgeRegisterHistoryRow

import java.time.LocalDateTime
import javax.inject.Inject
import scala.concurrent.ExecutionContext

/*
case class KnowledgeRegisterHistoryRecord(stateId:Long, documentId:String, sequentialNumber:Int, propositionId:String,sentences:String, json:String)
object KnowledgeRegisterHistoryRecord {
  implicit val jsonWrites: OWrites[KnowledgeRegisterHistoryRecord] = Json.writes[KnowledgeRegisterHistoryRecord]
  implicit val jsonReads: Reads[KnowledgeRegisterHistoryRecord] = Json.reads[KnowledgeRegisterHistoryRecord]
}
*/

class KnowledgeRegisterHistoryController @Inject()(knowledgeRegisterHistoryDao:KnowledgeRegisterHistoryDao, val controllerComponents: ControllerComponents)(implicit ec: ExecutionContext) extends BaseController  with LazyLogging{
  def add() = Action(parse.json) { request =>
    val transversalState = Json.parse(request.headers.get(TRANSVERSAL_STATE.str).get).as[TransversalState]
    try {
      val json = request.body
      val knowledgeRegisterHistoryRecord: KnowledgeRegisterHistoryRecord = Json.parse(json.toString).as[KnowledgeRegisterHistoryRecord]
      val knowledgeRegisterHistoryRow = KnowledgeRegisterHistoryRow(
        id = 0,
        userId = transversalState.userId,
        stateId = knowledgeRegisterHistoryRecord.stateId,
        documentId = knowledgeRegisterHistoryRecord.documentId,
        sequentialNumber = knowledgeRegisterHistoryRecord.sequentialNumber,
        propositionId = knowledgeRegisterHistoryRecord.propositionId,
        sentences = knowledgeRegisterHistoryRecord.sentences,
        json = knowledgeRegisterHistoryRecord.json,
        createdAt = LocalDateTime.now,
        updatedAt = LocalDateTime.now)

      knowledgeRegisterHistoryDao.add(knowledgeRegisterHistoryRow)
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
      val knowledgeRegisterHistoryRecord: KnowledgeRegisterHistoryRecord = Json.parse(json.toString).as[KnowledgeRegisterHistoryRecord]
      val result = knowledgeRegisterHistoryDao.searchByDocumentId(knowledgeRegisterHistoryRecord.documentId).head
      val convertKnowledgeRegisterHistoryRecord = KnowledgeRegisterHistoryRecord(
        stateId = result.stateId,
        documentId = result.documentId,
        sequentialNumber = result.sequentialNumber,
        propositionId = result.propositionId,
        sentences = result.sentences,
        json = result.json)
      Ok(Json.toJson(convertKnowledgeRegisterHistoryRecord))
    } catch {
      case e: Exception => {
        logger.error(ToposoidUtils.formatMessageForLogger(e.toString, transversalState.userId), e)
        BadRequest(Json.obj("status" -> "Error", "message" -> e.toString()))
      }
    }
  }

}
