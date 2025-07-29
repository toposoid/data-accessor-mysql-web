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

import model.Tables.{NonSentenceSections, NonSentenceSectionsRow}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.Inject
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext}

class NonSentenceSectionsDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  /** register */
  def add(u: NonSentenceSectionsRow)(implicit ec: ExecutionContext): Unit = {
    Await.result(db.run(NonSentenceSections += u), Duration.Inf)
  }

  def searchByDocumentId(documentId: String)(implicit ec: ExecutionContext): Seq[NonSentenceSectionsRow] = {
    val search = db.run(NonSentenceSections.filter(_.documentId === documentId).result)
    Await.result(search, Duration.Inf)
  }

  //def select()

  //https://sysgears.com/articles/scala-development/pagination-with-slick-how-to-properly-build-select-queries/
  /*
  def findAll(userId: Long, documentId: String, limit: Int, offset: Int) = db.run {
    for {

      comments <- NonSentenceSections.filter(x => x.userId === userId && x.documentId===documentId)
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
