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
import com.ideal.linked.toposoid.knowledgebase.regist.rdb.model.{DocumentAnalysisResultHistoryRecord, KnowledgeRegisterHistoryCount, KnowledgeRegisterHistoryRecord}
import com.typesafe.scalalogging.LazyLogging
import dao.KnowledgeRegisterHistoryDao
import play.api.libs.json.{Json, OWrites, Reads}
import play.api.mvc.{BaseController, ControllerComponents}
import model.Tables.KnowledgeRegisterHistoryRow

import java.time.LocalDateTime
import javax.inject.Inject
import scala.concurrent.ExecutionContext

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
      val records = knowledgeRegisterHistoryDao.searchByDocumentId(knowledgeRegisterHistoryRecord.documentId).toList
      val results:List[KnowledgeRegisterHistoryRecord] = records.map(x => {
        KnowledgeRegisterHistoryRecord(
          stateId = x.stateId,
          documentId = x.documentId,
          sequentialNumber = x.sequentialNumber,
          propositionId = x.propositionId,
          sentences = x.sentences,
          json = x.json)
      })
      Ok(Json.toJson(results))
    } catch {
      case e: Exception => {
        logger.error(ToposoidUtils.formatMessageForLogger(e.toString, transversalState.userId), e)
        BadRequest(Json.obj("status" -> "Error", "message" -> e.toString()))
      }
    }
  }

  def getCountByDocumentId()= Action(parse.json) { request =>
    val transversalState = Json.parse(request.headers.get(TRANSVERSAL_STATE.str).get).as[TransversalState]
    try {
      val json = request.body
      val knowledgeRegisterHistoryCount: KnowledgeRegisterHistoryCount = Json.parse(json.toString).as[KnowledgeRegisterHistoryCount]
      val count:Int = knowledgeRegisterHistoryDao.getCountByDocumentId(knowledgeRegisterHistoryCount.documentId)
      Ok(Json.toJson(KnowledgeRegisterHistoryCount(documentId = knowledgeRegisterHistoryCount.documentId, count = count)))
    } catch {
      case e: Exception => {
        logger.error(ToposoidUtils.formatMessageForLogger(e.toString, transversalState.userId), e)
        BadRequest(Json.obj("status" -> "Error", "message" -> e.toString()))
      }
    }
  }


  def searchByPropositionId = Action(parse.json) { request =>
    val transversalState = Json.parse(request.headers.get(TRANSVERSAL_STATE.str).get).as[TransversalState]
    try {
      val json = request.body
      val knowledgeRegisterHistoryRecord: KnowledgeRegisterHistoryRecord = Json.parse(json.toString).as[KnowledgeRegisterHistoryRecord]
      val records = knowledgeRegisterHistoryDao.searchByPropositionId(knowledgeRegisterHistoryRecord.propositionId).toList
      val results: List[KnowledgeRegisterHistoryRecord] = records.map(x => {
        KnowledgeRegisterHistoryRecord(
          stateId = x.stateId,
          documentId = x.documentId,
          sequentialNumber = x.sequentialNumber,
          propositionId = x.propositionId,
          sentences = x.sentences,
          json = x.json)
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
