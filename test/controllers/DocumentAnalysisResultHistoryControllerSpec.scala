package controllers

import akka.actor.ActorSystem
import akka.stream.Materializer
import com.ideal.linked.toposoid.common.{TRANSVERSAL_STATE, TransversalState}
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
      val documentAnalysisResultRecord:DocumentAnalysisResultRecord = DocumentAnalysisResultRecord(
        documentId = documentId,
        originalFilename = "test",
        totalSeparatedNumber = 1
      )

      val fr = FakeRequest(POST, "/add")
        .withHeaders("Content-type" -> "application/json", TRANSVERSAL_STATE.str -> transversalState)
        .withJsonBody(Json.toJson(documentAnalysisResultRecord))
      val result = call(controller.add(), fr)
      status(result) mustBe OK

      val documentAnalysisResultRecord2: DocumentAnalysisResultRecord = DocumentAnalysisResultRecord(
        documentId = documentId,
        originalFilename = "",
        totalSeparatedNumber = 0
      )

      val fr2 = FakeRequest(POST, "/searchByDocumentId")
        .withHeaders("Content-type" -> "application/json", TRANSVERSAL_STATE.str -> transversalState)
        .withJsonBody(Json.toJson(documentAnalysisResultRecord2))
      val result2 = call(controller.searchByDocumentId(), fr2)
      status(result2) mustBe OK

      val jsonResult: String = contentAsJson(result2).toString()
      val documentAnalysisResultRecord3: DocumentAnalysisResultRecord = Json.parse(jsonResult).as[DocumentAnalysisResultRecord]

      assert(documentAnalysisResultRecord.documentId == documentAnalysisResultRecord3.documentId)
      assert(documentAnalysisResultRecord.originalFilename == documentAnalysisResultRecord3.originalFilename)
      assert(documentAnalysisResultRecord.totalSeparatedNumber == documentAnalysisResultRecord3.totalSeparatedNumber)

    }
  }
}