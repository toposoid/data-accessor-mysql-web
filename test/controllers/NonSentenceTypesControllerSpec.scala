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
import com.ideal.linked.toposoid.knowledgebase.regist.rdb.model.NonSentenceTypesRecord
import dao.NonSentenceTypesDao
import model.Tables.NonSentenceTypesRow
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
class NonSentenceTypesControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting with BeforeAndAfterAll {

  val as = ActorSystem()
  implicit val materializer = Materializer(as)

  lazy val appBuilder: GuiceApplicationBuilder = new GuiceApplicationBuilder().in(Mode.Test)
  lazy val injector: Injector = appBuilder.injector()
  lazy val testUtils: TestUtils = injector.instanceOf[TestUtils]
  lazy val nonSentenceTypesDao:NonSentenceTypesDao = injector.instanceOf[NonSentenceTypesDao]

  val transversalState: String = Json.toJson(TransversalState(userId = "test-user", username = "guest", roleId = 0, csrfToken = "")).toString()


  override def beforeAll(): Unit = {
    testUtils.createTable()
    implicit val ec = ExecutionContext.global
    nonSentenceTypesDao.add(NonSentenceTypesRow(id = 0, name = "unspecified", createdAt = LocalDateTime.now, updatedAt = LocalDateTime.now))(ec)
    nonSentenceTypesDao.add(NonSentenceTypesRow(id = 0, name = "references", createdAt = LocalDateTime.now, updatedAt = LocalDateTime.now))(ec)
    nonSentenceTypesDao.add(NonSentenceTypesRow(id = 0, name = "table of contents", createdAt = LocalDateTime.now, updatedAt = LocalDateTime.now))(ec)
  }

  override def afterAll(): Unit = {
    testUtils.deleteAll("non_sentence_types")
  }

  "NonSentenceTypesController POST(getAll)" should {
    "returns an appropriate response" in new WithApplication(){

      val controller: NonSentenceTypesController = inject[NonSentenceTypesController]
      val fr = FakeRequest(POST, "/getAllNonSentenceTypes")
        .withHeaders("Content-type" -> "application/json", TRANSVERSAL_STATE.str -> transversalState)
        .withJsonBody(Json.toJson("{}"))
      val result = call(controller.getAll(), fr)
      status(result) mustBe OK

      val jsonResult: String = contentAsJson(result).toString
      val nonSentenceTypesRecords:List[NonSentenceTypesRecord] = Json.parse(jsonResult).as[List[NonSentenceTypesRecord]]
      nonSentenceTypesRecords.foreach(x => {
        x.nonSentenceType match {
          case 1 => assert(x.name.equals("unspecified"))
          case 2 => assert(x.name.equals("references"))
          case 3 => assert(x.name.equals("table of contents"))
        }
      })
    }
  }
}