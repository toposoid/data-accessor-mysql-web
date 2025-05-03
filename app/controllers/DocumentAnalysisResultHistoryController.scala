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
      val records = documentAnalysisResultHistoryDao.searchByDocumentIdAndStateId(input.documentId, 1).toList
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
