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

import akka.actor.ActorSystem
import akka.stream.Materializer
import com.ideal.linked.toposoid.common.{TRANSVERSAL_STATE, TransversalState}
import com.ideal.linked.toposoid.knowledgebase.regist.rdb.model.DocumentAnalysisResultHistoryRecord
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
        stateId = 0,
        documentId = documentId,
        originalFilename = "test",
        totalSeparatedNumber = 1
      )

      val fr = FakeRequest(POST, "/addDocumentAnalysisResultHistory")
        .withHeaders("Content-type" -> "application/json", TRANSVERSAL_STATE.str -> transversalState)
        .withJsonBody(Json.toJson(documentAnalysisResultRecord))
      val result = call(controller.add(), fr)
      status(result) mustBe OK

      val documentAnalysisResultRecord2: DocumentAnalysisResultHistoryRecord = DocumentAnalysisResultHistoryRecord(
        stateId = 0,
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

    }
  }
}