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

package controllers

import com.ideal.linked.toposoid.common.{TRANSVERSAL_STATE, ToposoidUtils, TransversalState}
import com.ideal.linked.toposoid.knowledgebase.regist.rdb.model.{KnowledgeRegisterHistoryRecord, NonSentenceSectionsRecord}
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
      val records = nonSentenceSectionsDao.searchByDocumentId(nonSentenceSectionsRecord.documentId).toList
      val results:List[NonSentenceSectionsRecord] = records.map(x => {
        NonSentenceSectionsRecord(
          nonSentenceType = x.nonSentenceType,
          documentId = x.documentId,
          pageNo = x.pageNo,
          nonSentence = x.nonSentence)
      })
      Ok(Json.toJson(results))
    } catch {
      case e: Exception => {
        logger.error(ToposoidUtils.formatMessageForLogger(e.toString, transversalState.userId), e)
        BadRequest(Json.obj("status" -> "Error", "message" -> e.toString()))
      }
    }
  }

}
