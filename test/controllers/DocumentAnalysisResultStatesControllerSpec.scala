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
import com.ideal.linked.toposoid.knowledgebase.regist.rdb.model.DocumentAnalysisResultStatesRecord
import dao.DocumentAnalysisResultStatesDao
import model.Tables.DocumentAnalysisResultStatesRow
import org.scalatest.BeforeAndAfterAll
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.Mode
import play.api.inject.Injector
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test._
import play.test.WithApplication

import java.time.LocalDateTime
import scala.concurrent.ExecutionContext

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 *
 * For more information, see https://www.playframework.com/documentation/latest/ScalaTestingWithScalaTest
 */
class DocumentAnalysisResultStatesControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting with BeforeAndAfterAll {

  val as = ActorSystem()
  implicit val materializer = Materializer(as)

  lazy val appBuilder: GuiceApplicationBuilder = new GuiceApplicationBuilder().in(Mode.Test)
  lazy val injector: Injector = appBuilder.injector()
  lazy val testUtils: TestUtils = injector.instanceOf[TestUtils]
  lazy val documentAnalysisResultStatesDao:DocumentAnalysisResultStatesDao = injector.instanceOf[DocumentAnalysisResultStatesDao]

  val transversalState: String = Json.toJson(TransversalState(userId = "test-user", username = "guest", roleId = 0, csrfToken = "")).toString()


  override def beforeAll(): Unit = {
    testUtils.createTable()
    implicit val ec = ExecutionContext.global
    documentAnalysisResultStatesDao.add(DocumentAnalysisResultStatesRow(id = 0, name = "success", createdAt = LocalDateTime.now, updatedAt = LocalDateTime.now))(ec)
    documentAnalysisResultStatesDao.add(DocumentAnalysisResultStatesRow(id = 0, name = "failure", createdAt = LocalDateTime.now, updatedAt = LocalDateTime.now))(ec)
  }

  override def afterAll(): Unit = {
    testUtils.deleteAll("document_analysis_result_states")
  }

  "KnowledgeRegisterStatesController POST(getAll)" should {
    "returns an appropriate response" in new WithApplication(){

      val controller: DocumentAnalysisResultStatesController = inject[DocumentAnalysisResultStatesController]
      val fr = FakeRequest(POST, "/getAllKnowledgeRegisterStates")
        .withHeaders("Content-type" -> "application/json", TRANSVERSAL_STATE.str -> transversalState)
        .withJsonBody(Json.toJson("{}"))
      val result = call(controller.getAll(), fr)
      status(result) mustBe OK

      val jsonResult: String = contentAsJson(result).toString
      val documentAnalysisResultStatesRecord:List[DocumentAnalysisResultStatesRecord] = Json.parse(jsonResult).as[List[DocumentAnalysisResultStatesRecord]]
      documentAnalysisResultStatesRecord.foreach(x => {
        x.stateId match {
          case 1 => assert(x.name.equals("success"))
          case 2 => assert(x.name.equals("failure"))
        }
      })
    }
  }
}