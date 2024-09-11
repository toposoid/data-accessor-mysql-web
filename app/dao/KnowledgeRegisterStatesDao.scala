package dao

import model.Tables.{KnowledgeRegisterStates, KnowledgeRegisterStatesRow}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.Inject
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

class KnowledgeRegisterStatesDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  def all()(implicit ec: ExecutionContext): Seq[KnowledgeRegisterStatesRow] = Await.result(db.run(KnowledgeRegisterStates.result), Duration.Inf)

  /** 検索 */
  def search(u: KnowledgeRegisterStatesRow)(implicit ec: ExecutionContext): Future[Seq[KnowledgeRegisterStatesRow]] = {
    val query = KnowledgeRegisterStates.filter(x => (x.id === u.id))
    db.run(query.result)
  }

  /** 登録 */
  def add(u: KnowledgeRegisterStatesRow)(implicit ec: ExecutionContext): Unit = {
    Await.result(db.run(KnowledgeRegisterStates += u), Duration.Inf)
  }

  def select(id: Long)(implicit ec: ExecutionContext): KnowledgeRegisterStatesRow = {
    val select = db.run(KnowledgeRegisterStates.filter(_.id === id).result.head)
    Await.result(select, Duration.Inf)
  }

  /** 更新 */
  def update(u: KnowledgeRegisterStatesRow)(implicit ec: ExecutionContext): Unit = {
    Await.result(db.run(KnowledgeRegisterStates.filter(_.id === u.id).update(u)), Duration.Inf)
  }

  /** 削除 */
  def delete(id: Long)(implicit ec: ExecutionContext): Unit = {
    Await.result(db.run(KnowledgeRegisterStates.filter(_.id === id).delete), Duration.Inf)
  }

}
