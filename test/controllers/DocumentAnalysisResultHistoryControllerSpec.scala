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

import akka.actor.ActorSystem
import akka.stream.Materializer
import com.ideal.linked.toposoid.common.{TRANSVERSAL_STATE, TransversalState}
import com.ideal.linked.toposoid.knowledgebase.regist.rdb.model.{DocumentAnalysisResultHistoryRecord, KnowledgeRegisterHistoryCount}
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.libs.json.Json
import play.api.test._
import play.api.test.Helpers._
import play.test.WithApplication
import io.jvm.uuid.UUID
import org.scalatest.BeforeAndAfterAll
import play.api.Mode
import play.api.inject.Injector
import play.api.inject.guice.GuiceApplicationBuilder

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 *
 * For more information, see https://www.playframework.com/documentation/latest/ScalaTestingWithScalaTest
 */
class DocumentAnalysisResultHistoryControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting with BeforeAndAfterAll{

  val as = ActorSystem()
  implicit val materializer = Materializer(as)

  lazy val appBuilder: GuiceApplicationBuilder = new GuiceApplicationBuilder().in(Mode.Test)
  lazy val injector: Injector = appBuilder.injector()
  lazy val testUtils: TestUtils = injector.instanceOf[TestUtils]

  val transversalState: String = Json.toJson(TransversalState(userId = "test-user", username = "guest", roleId = 0, csrfToken = "")).toString()


  override def beforeAll(): Unit = {
    testUtils.createTable()
  }

  override def afterAll(): Unit = {
    testUtils.deleteAll("document_analysis_result_history")
  }

  "DocumentAnalysisResultHistoryController POST(add, searchByDocument)" should {
    "returns an appropriate response" in new WithApplication(){
      val documentId = UUID.random.toString
      val controller: DocumentAnalysisResultHistoryController = inject[DocumentAnalysisResultHistoryController]
      val documentAnalysisResultRecord:DocumentAnalysisResultHistoryRecord = DocumentAnalysisResultHistoryRecord(
        stateId = 5,
        documentId = documentId,
        originalFilename = "test",
        totalSeparatedNumber = 3
      )

      val fr = FakeRequest(POST, "/addDocumentAnalysisResultHistory")
        .withHeaders("Content-type" -> "application/json", TRANSVERSAL_STATE.str -> transversalState)
        .withJsonBody(Json.toJson(documentAnalysisResultRecord))
      val result = call(controller.add(), fr)
      status(result) mustBe OK

      val documentAnalysisResultRecord2: DocumentAnalysisResultHistoryRecord = DocumentAnalysisResultHistoryRecord(
        stateId = 5,
        documentId = documentId,
        originalFilename = "",
        totalSeparatedNumber = 0
      )

      val fr2 = FakeRequest(POST, "/searchDocumentAnalysisResultHistoryByDocumentId")
        .withHeaders("Content-type" -> "application/json", TRANSVERSAL_STATE.str -> transversalState)
        .withJsonBody(Json.toJson(documentAnalysisResultRecord2))
      val result2 = call(controller.searchByDocumentIdAndStateId(), fr2)
      status(result2) mustBe OK

      val jsonResult: String = contentAsJson(result2).toString()
      val documentAnalysisResultRecord3: List[DocumentAnalysisResultHistoryRecord] = Json.parse(jsonResult).as[List[DocumentAnalysisResultHistoryRecord]]

      assert(documentAnalysisResultRecord.documentId == documentAnalysisResultRecord3.head.documentId)
      assert(documentAnalysisResultRecord.stateId == documentAnalysisResultRecord3.head.stateId)
      assert(documentAnalysisResultRecord.originalFilename == documentAnalysisResultRecord3.head.originalFilename)
      assert(documentAnalysisResultRecord.totalSeparatedNumber == documentAnalysisResultRecord3.head.totalSeparatedNumber)

      val input = KnowledgeRegisterHistoryCount(documentId = documentAnalysisResultRecord.documentId, count = 0)
      val fr3 = FakeRequest(POST, "/getKnowledgeRegisterHistoryTotalCountByDocumentId")
        .withHeaders("Content-type" -> "application/json", TRANSVERSAL_STATE.str -> transversalState)
        .withJsonBody(Json.toJson(input))
      val result3 = call(controller.getTotalCountByDocumentId(), fr3)
      status(result3) mustBe OK

      val jsonResult3: String = contentAsJson(result3).toString()
      val knowledgeRegisterHistoryCount:KnowledgeRegisterHistoryCount  = Json.parse(jsonResult3).as[KnowledgeRegisterHistoryCount]
      assert(knowledgeRegisterHistoryCount.count == 3)

    }
  }


  "DocumentAnalysisResultHistoryController Multi POST(add, searchLatestStateByDocumentId)" should {
    "returns an appropriate response" in new WithApplication() {
      val documentId = UUID.random.toString
      val controller: DocumentAnalysisResultHistoryController = inject[DocumentAnalysisResultHistoryController]
      val documentAnalysisResultRecords: List[DocumentAnalysisResultHistoryRecord] =
        List(
          DocumentAnalysisResultHistoryRecord(
            stateId = 1,
            documentId = documentId,
            originalFilename = "test",
            totalSeparatedNumber = 1),
          DocumentAnalysisResultHistoryRecord(
            stateId = 3,
            documentId = documentId,
            originalFilename = "test",
            totalSeparatedNumber = 1),
          DocumentAnalysisResultHistoryRecord(
            stateId = 5,
            documentId = documentId,
            originalFilename = "test",
            totalSeparatedNumber = 3)
        )

      documentAnalysisResultRecords.foreach(x => {
        val fr = FakeRequest(POST, "/addDocumentAnalysisResultHistory")
          .withHeaders("Content-type" -> "application/json", TRANSVERSAL_STATE.str -> transversalState)
          .withJsonBody(Json.toJson(x))
        val result = call(controller.add(), fr)
        status(result) mustBe OK
      })

      val documentAnalysisResultRecord: DocumentAnalysisResultHistoryRecord = DocumentAnalysisResultHistoryRecord(
        stateId = 0,
        documentId = documentId,
        originalFilename = "",
        totalSeparatedNumber = 0
      )

      val fr2 = FakeRequest(POST, "/searchLatestDocumentAnalysisStateByDocumentId")
        .withHeaders("Content-type" -> "application/json", TRANSVERSAL_STATE.str -> transversalState)
        .withJsonBody(Json.toJson(documentAnalysisResultRecord))
      val result2 = call(controller.searchLatestStateByDocumentId, fr2)
      status(result2) mustBe OK

      val jsonResult: String = contentAsJson(result2).toString()
      val documentAnalysisResultRecord3: List[DocumentAnalysisResultHistoryRecord] = Json.parse(jsonResult).as[List[DocumentAnalysisResultHistoryRecord]]

      assert(documentAnalysisResultRecord3.size == 1)
      assert(documentAnalysisResultRecord3.head.documentId.equals(documentId))
      assert(documentAnalysisResultRecord3.head.stateId == 5 )
    }
  }
}