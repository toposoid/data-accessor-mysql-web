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
import com.ideal.linked.toposoid.knowledgebase.regist.rdb.model.{DocumentAnalysisResultHistoryRecord, KnowledgeRegisterHistoryCount}
import com.typesafe.scalalogging.LazyLogging
import dao.DocumentAnalysisResultHistoryDao
import model.Tables.DocumentAnalysisResultHistoryRow
import play.api.libs.json.Json
import play.api.mvc.{BaseController, ControllerComponents}
import play.api.libs.json.{Json, OWrites, Reads}

import javax.inject.Inject
import java.time.LocalDateTime
import scala.concurrent.ExecutionContext


class DocumentAnalysisResultHistoryController @Inject()(documentAnalysisResultHistoryDao:DocumentAnalysisResultHistoryDao, val controllerComponents: ControllerComponents)(implicit ec: ExecutionContext) extends BaseController  with LazyLogging{

  def add()  = Action(parse.json) { request =>
    val transversalState = Json.parse(request.headers.get(TRANSVERSAL_STATE.str).get).as[TransversalState]
    try {
      val json = request.body
      val documentAnalysisResult: DocumentAnalysisResultHistoryRecord = Json.parse(json.toString).as[DocumentAnalysisResultHistoryRecord]
      val documentAnalysisResultHistory = DocumentAnalysisResultHistoryRow(
        id = 0,
        stateId = documentAnalysisResult.stateId,
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

  def searchByDocumentIdAndStateId = Action(parse.json) { request =>
    val transversalState = Json.parse(request.headers.get(TRANSVERSAL_STATE.str).get).as[TransversalState]
    try {
      val json = request.body
      val documentAnalysisResult: DocumentAnalysisResultHistoryRecord = Json.parse(json.toString).as[DocumentAnalysisResultHistoryRecord]
      val records = documentAnalysisResultHistoryDao.searchByDocumentIdAndStateId(documentAnalysisResult.documentId, documentAnalysisResult.stateId).toList
      val results:List[DocumentAnalysisResultHistoryRecord] = records.map(x => {
        DocumentAnalysisResultHistoryRecord(
          stateId = x.stateId,
          documentId = x.documentId,
          originalFilename = x.originalFilename,
          totalSeparatedNumber = x.totalSeparatedNumber)
      })
      Ok(Json.toJson(results))
    } catch {
      case e: Exception => {
        logger.error(ToposoidUtils.formatMessageForLogger(e.toString, transversalState.userId), e)
        BadRequest(Json.obj("status" -> "Error", "message" -> e.toString()))
      }
    }
  }

  def searchLatestStateByDocumentId = Action(parse.json) { request =>
    val transversalState = Json.parse(request.headers.get(TRANSVERSAL_STATE.str).get).as[TransversalState]
    try {
      val json = request.body
      val documentAnalysisResult: DocumentAnalysisResultHistoryRecord = Json.parse(json.toString).as[DocumentAnalysisResultHistoryRecord]
      val records = documentAnalysisResultHistoryDao.searchLatestStateByDocumentId(documentAnalysisResult.documentId).toList
      val results: List[DocumentAnalysisResultHistoryRecord] = records.size match {
        case 0 => List.empty[DocumentAnalysisResultHistoryRecord]
        case _ => {
          val rec = records.head
          List(
            DocumentAnalysisResultHistoryRecord(
              stateId = rec.stateId,
              documentId = rec.documentId,
              originalFilename = rec.originalFilename,
              totalSeparatedNumber = rec.totalSeparatedNumber)
          )
        }
      }
      Ok(Json.toJson(results))
    } catch {
      case e: Exception => {
        logger.error(ToposoidUtils.formatMessageForLogger(e.toString, transversalState.userId), e)
        BadRequest(Json.obj("status" -> "Error", "message" -> e.toString()))
      }
    }
  }


  def getTotalCountByDocumentId() = Action(parse.json) { request =>
    val transversalState = Json.parse(request.headers.get(TRANSVERSAL_STATE.str).get).as[TransversalState]
    try {
      val json = request.body
      val input: KnowledgeRegisterHistoryCount = Json.parse(json.toString).as[KnowledgeRegisterHistoryCount]
      //The total number cannot be determined until the file analysis is complete.
      val records = documentAnalysisResultHistoryDao.searchByDocumentIdAndStateId(input.documentId, 5).toList
      val results: List[DocumentAnalysisResultHistoryRecord] = records.map(x => {
        DocumentAnalysisResultHistoryRecord(
          stateId = x.stateId,
          documentId = x.documentId,
          originalFilename = x.originalFilename,
          totalSeparatedNumber = x.totalSeparatedNumber)
      })
      val knowledgeRegisterHistoryCount =  results.size match {
        case 0 => KnowledgeRegisterHistoryCount(documentId = input.documentId, count = 0)
        case _ => KnowledgeRegisterHistoryCount(documentId = results.head.documentId, count = results.head.totalSeparatedNumber)
      }
      Ok(Json.toJson(knowledgeRegisterHistoryCount))
    } catch {
      case e: Exception => {
        logger.error(ToposoidUtils.formatMessageForLogger(e.toString, transversalState.userId), e)
        BadRequest(Json.obj("status" -> "Error", "message" -> e.toString()))
      }
    }
  }


}
