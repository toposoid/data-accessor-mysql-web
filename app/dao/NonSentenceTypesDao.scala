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

package dao

import model.Tables.{NonSentenceTypes, NonSentenceTypesRow}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.Inject
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

class NonSentenceTypesDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  def all()(implicit ec: ExecutionContext): Seq[NonSentenceTypesRow] = Await.result(db.run(NonSentenceTypes.result), Duration.Inf)

  /** 検索 */
  def search(u: NonSentenceTypesRow)(implicit ec: ExecutionContext): Future[Seq[NonSentenceTypesRow]] = {
    val query = NonSentenceTypes.filter(x => (x.id === u.id))
    db.run(query.result)
  }

  /** 登録 */
  def add(u: NonSentenceTypesRow)(implicit ec: ExecutionContext): Unit = {
    Await.result(db.run(NonSentenceTypes += u), Duration.Inf)
  }

  def select(id: Long)(implicit ec: ExecutionContext): NonSentenceTypesRow = {
    val select = db.run(NonSentenceTypes.filter(_.id === id).result.head)
    Await.result(select, Duration.Inf)
  }

  /** 更新 */
  def update(u: NonSentenceTypesRow)(implicit ec: ExecutionContext): Unit = {
    Await.result(db.run(NonSentenceTypes.filter(_.id === u.id).update(u)), Duration.Inf)
  }

  /** 削除 */
  def delete(id: Long)(implicit ec: ExecutionContext): Unit = {
    Await.result(db.run(NonSentenceTypes.filter(_.id === id).delete), Duration.Inf)
  }

}
