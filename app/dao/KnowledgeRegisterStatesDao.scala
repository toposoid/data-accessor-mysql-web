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
