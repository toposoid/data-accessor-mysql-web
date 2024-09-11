package controllers

import model.Tables.{DocumentAnalysisResultHistory, KnowledgeRegisterHistory, KnowledgeRegisterStates}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.Inject
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.io.Source

class TestUtils @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile]{

  import profile.api._

  def deleteAll(tableName:String)={
    Await.result(db.run(sqlu"TRUNCATE TABLE toposoiddb.#$tableName"),Duration.Inf)
  }

  def createTable() = {
    val schema = DocumentAnalysisResultHistory.schema ++ KnowledgeRegisterHistory.schema ++ KnowledgeRegisterStates.schema
    Await.result(db.run(schema.dropIfExists),Duration.Inf)
    Await.result(db.run(schema.create),Duration.Inf)
  }
}
