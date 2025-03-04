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
import com.ideal.linked.toposoid.knowledgebase.regist.rdb.model.NonSentenceSectionsRecord
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
class NonSentenceSectionsControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting with BeforeAndAfterAll {

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
    testUtils.deleteAll("non_sentence_sections")
  }

  "HomeController POST(add, searchByDocument)" should {
    "returns an appropriate response" in new WithApplication(){

      val documentId = UUID.random.toString
      val controller: NonSentenceSectionsController = inject[NonSentenceSectionsController]
      val nonSentenceSectionsRecord:NonSentenceSectionsRecord = NonSentenceSectionsRecord(
        nonSentenceType = 0,
        documentId = documentId,
        pageNo = 1,
        nonSentence = "Edward Witten, Quantum field theory and the Jones polynomial, Commun. Math. Phys. 121,351-399 (1989) (euclid.cmp/1104178138).",
      )

      val fr = FakeRequest(POST, "/addNonSentenceSections")
        .withHeaders("Content-type" -> "application/json", TRANSVERSAL_STATE.str -> transversalState)
        .withJsonBody(Json.toJson(nonSentenceSectionsRecord))
      val result = call(controller.add(), fr)
      status(result) mustBe OK

      val nonSentenceSectionsRecord2: NonSentenceSectionsRecord = NonSentenceSectionsRecord(
        nonSentenceType = 0,
        documentId = documentId,
        pageNo = -1,
        nonSentence = ""
      )

      val fr2 = FakeRequest(POST, "/searchNonSentenceSectionsByDocumentId")
        .withHeaders("Content-type" -> "application/json", TRANSVERSAL_STATE.str -> transversalState)
        .withJsonBody(Json.toJson(nonSentenceSectionsRecord2))
      val result2 = call(controller.searchByDocumentId(), fr2)
      status(result2) mustBe OK

      val jsonResult: String = contentAsJson(result2).toString()
      val nonSentenceSectionsRecord3: NonSentenceSectionsRecord = Json.parse(jsonResult).as[NonSentenceSectionsRecord]

      assert(nonSentenceSectionsRecord.nonSentenceType == nonSentenceSectionsRecord3.nonSentenceType)
      assert(nonSentenceSectionsRecord.documentId == nonSentenceSectionsRecord3.documentId)
      assert(nonSentenceSectionsRecord.pageNo == nonSentenceSectionsRecord3.pageNo)
      assert(nonSentenceSectionsRecord.nonSentence == nonSentenceSectionsRecord3.nonSentence)

    }
  }
}