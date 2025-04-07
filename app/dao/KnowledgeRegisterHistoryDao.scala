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
