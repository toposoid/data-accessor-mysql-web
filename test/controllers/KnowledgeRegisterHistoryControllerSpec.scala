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
import com.ideal.linked.toposoid.knowledgebase.regist.rdb.model.{DocumentAnalysisResultHistoryRecord, KnowledgeRegisterHistoryCount, KnowledgeRegisterHistoryRecord}
import io.jvm.uuid.UUID
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

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 *
 * For more information, see https://www.playframework.com/documentation/latest/ScalaTestingWithScalaTest
 */
class KnowledgeRegisterHistoryControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting with BeforeAndAfterAll {

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
    testUtils.deleteAll("knowledge_register_history")
  }

  "HomeController POST(add, searchByDocument)" should {
    "returns an appropriate response" in new WithApplication(){

      val documentId = UUID.random.toString
      val propositionId = UUID.random.toString
      val controller: KnowledgeRegisterHistoryController = inject[KnowledgeRegisterHistoryController]
      val knowledgeRegisterHistoryRecord:KnowledgeRegisterHistoryRecord = KnowledgeRegisterHistoryRecord(
        stateId = 0,
        documentId = documentId,
        sequentialNumber = 1,
        propositionId = propositionId,
        sentences = "これはテストです。",
        json = """  {
                 |    "premiseList": [],
                 |    "premiseLogicRelation": [],
                 |    "claimList": [
                 |      {
                 |        "sentence": "これは'テストです。",
                 |        "lang": "ja_JP",
                 |        "extentInfoJson": "{"hoge":"fuga"}",
                 |        "isNegativeSentence": false,
                 |        "knowledgeForImages": []
                 |      }
                 |    ],
                 |    "claimLogicRelation": []
                 |  }
                 |""".stripMargin
      )

      val fr = FakeRequest(POST, "/addKnowledgeRegisterHistory")
        .withHeaders("Content-type" -> "application/json", TRANSVERSAL_STATE.str -> transversalState)
        .withJsonBody(Json.toJson(knowledgeRegisterHistoryRecord))
      val result = call(controller.add(), fr)
      status(result) mustBe OK

      val knowledgeRegisterHistoryRecord2: KnowledgeRegisterHistoryRecord = KnowledgeRegisterHistoryRecord(
        stateId = 0,
        documentId = documentId,
        sequentialNumber = -1,
        propositionId = "",
        sentences = "",
        json = ""
      )

      val fr2 = FakeRequest(POST, "/searchKnowledgeRegisterHistoryByDocumentId")
        .withHeaders("Content-type" -> "application/json", TRANSVERSAL_STATE.str -> transversalState)
        .withJsonBody(Json.toJson(knowledgeRegisterHistoryRecord2))
      val result2 = call(controller.searchByDocumentId(), fr2)
      status(result2) mustBe OK

      val jsonResult: String = contentAsJson(result2).toString()
      val knowledgeRegisterHistoryRecord3 = Json.parse(jsonResult).as[List[KnowledgeRegisterHistoryRecord]]

      assert(knowledgeRegisterHistoryRecord.stateId == knowledgeRegisterHistoryRecord3.head.stateId)
      assert(knowledgeRegisterHistoryRecord.documentId == knowledgeRegisterHistoryRecord3.head.documentId)
      assert(knowledgeRegisterHistoryRecord.sequentialNumber == knowledgeRegisterHistoryRecord3.head.sequentialNumber)
      assert(knowledgeRegisterHistoryRecord.propositionId == knowledgeRegisterHistoryRecord3.head.propositionId)
      assert(knowledgeRegisterHistoryRecord.sentences == knowledgeRegisterHistoryRecord3.head.sentences)
      assert(knowledgeRegisterHistoryRecord.json == knowledgeRegisterHistoryRecord3.head.json)

      val knowledgeRegisterHistoryRecord4: KnowledgeRegisterHistoryRecord = KnowledgeRegisterHistoryRecord(
        stateId = 0,
        documentId = "",
        sequentialNumber = -1,
        propositionId = propositionId,
        sentences = "",
        json = "")

      val fr3 = FakeRequest(POST, "/searchKnowledgeRegisterHistoryByPropositionId")
        .withHeaders("Content-type" -> "application/json", TRANSVERSAL_STATE.str -> transversalState)
        .withJsonBody(Json.toJson(knowledgeRegisterHistoryRecord4))
      val result3 = call(controller.searchByPropositionId(), fr3)
      status(result3) mustBe OK

      val jsonResult2: String = contentAsJson(result3).toString()
      val knowledgeRegisterHistoryRecord5 = Json.parse(jsonResult2).as[List[KnowledgeRegisterHistoryRecord]]

      assert(knowledgeRegisterHistoryRecord.stateId == knowledgeRegisterHistoryRecord5.head.stateId)
      assert(knowledgeRegisterHistoryRecord.documentId == knowledgeRegisterHistoryRecord5.head.documentId)
      assert(knowledgeRegisterHistoryRecord.sequentialNumber == knowledgeRegisterHistoryRecord5.head.sequentialNumber)
      assert(knowledgeRegisterHistoryRecord.propositionId == knowledgeRegisterHistoryRecord5.head.propositionId)
      assert(knowledgeRegisterHistoryRecord.sentences == knowledgeRegisterHistoryRecord5.head.sentences)
      assert(knowledgeRegisterHistoryRecord.json == knowledgeRegisterHistoryRecord5.head.json)

      val fr4 = FakeRequest(POST, "/getKnowledgeRegisterHistoryCountByDocumentId")
        .withHeaders("Content-type" -> "application/json", TRANSVERSAL_STATE.str -> transversalState)
        .withJsonBody(Json.toJson(KnowledgeRegisterHistoryCount(documentId = documentId, count = 0)))
      val result4 = call(controller.getCountByDocumentId(), fr4)
      status(result4) mustBe OK

      val jsonResult4: String = contentAsJson(result4).toString()
      val knowledgeRegisterHistoryCount:KnowledgeRegisterHistoryCount = Json.parse(jsonResult4).as[KnowledgeRegisterHistoryCount]
      assert(knowledgeRegisterHistoryCount.count == 1)

    }
  }
}