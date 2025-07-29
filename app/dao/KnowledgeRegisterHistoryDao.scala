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
import model.Tables.{KnowledgeRegisterHistory, KnowledgeRegisterHistoryRow}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.Inject
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext}

case class PaginatedResult[T](
                               totalCount: Int,
                               entities: List[T],
                               hasNextPage: Boolean
                             )

class KnowledgeRegisterHistoryDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  /** register */
  def add(u: KnowledgeRegisterHistoryRow)(implicit ec: ExecutionContext): Unit = {
    Await.result(db.run(KnowledgeRegisterHistory += u), Duration.Inf)
  }

  def searchByDocumentId(documentId: String)(implicit ec: ExecutionContext): Seq[KnowledgeRegisterHistoryRow] = {
    val search = db.run(KnowledgeRegisterHistory.filter(_.documentId === documentId).result)
    Await.result(search, Duration.Inf)
  }

  def getCountByDocumentId(documentId: String)(implicit ec: ExecutionContext): Int = {
    val search = db.run(KnowledgeRegisterHistory.filter(_.documentId === documentId).length.result)
    Await.result(search, Duration.Inf)
  }


  def searchByPropositionId(propositionId: String)(implicit ec: ExecutionContext): Seq[KnowledgeRegisterHistoryRow] = {
    val search = db.run(KnowledgeRegisterHistory.filter(_.propositionId === propositionId).result)
    Await.result(search, Duration.Inf)
  }

  //def select()

  //https://sysgears.com/articles/scala-development/pagination-with-slick-how-to-properly-build-select-queries/
  /*
  def findAll(userId: Long, documentId: String, limit: Int, offset: Int) = db.run {
    for {

      comments <- KnowledgeRegisterHistory.filter(x => x.userId === userId && x.documentId===documentId)
        .sortBy(_.createdAt)
        .drop(offset).take(limit)
        .result
      numberOfComments <- query.filter(_.creatorId === userId).length.result
    } yield PaginatedResult(
      totalCount = numberOfComments,
      entities = comments.toList,
      hasNextPage = numberOfComments - (offset + limit) > 0
    )
  }
  */

}
