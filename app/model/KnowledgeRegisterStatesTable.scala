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

package model

import java.time.LocalDateTime
// AUTO-GENERATED Slick data model for table KnowledgeRegisterStates
trait KnowledgeRegisterStatesTable {

  self:TablesRoot  =>

  import profile.api._
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}
  /** Entity class storing rows of table KnowledgeRegisterStates
   *  @param id Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey
   *  @param name Database column name SqlType(VARCHAR), Length(255,true)
   *  @param createdAt Database column created_at SqlType(DATETIME)
   *  @param updatedAt Database column updated_at SqlType(DATETIME) */
  case class KnowledgeRegisterStatesRow(id: Long, name: String, createdAt: LocalDateTime, updatedAt: LocalDateTime)
  /** GetResult implicit for fetching KnowledgeRegisterStatesRow objects using plain SQL queries */
  implicit def GetResultKnowledgeRegisterStatesRow(implicit e0: GR[Long], e1: GR[String], e2: GR[LocalDateTime]): GR[KnowledgeRegisterStatesRow] = GR{
    prs => import prs._
    KnowledgeRegisterStatesRow.tupled((<<[Long], <<[String], <<[LocalDateTime], <<[LocalDateTime]))
  }
  /** Table description of table knowledge_register_states. Objects of this class serve as prototypes for rows in queries. */
  class KnowledgeRegisterStates(_tableTag: Tag) extends profile.api.Table[KnowledgeRegisterStatesRow](_tableTag, Some("toposoiddb"), "knowledge_register_states") {
    def * = (id, name, createdAt, updatedAt).<>(KnowledgeRegisterStatesRow.tupled, KnowledgeRegisterStatesRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(name), Rep.Some(createdAt), Rep.Some(updatedAt))).shaped.<>({r=>import r._; _1.map(_=> KnowledgeRegisterStatesRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(VARCHAR), Length(255,true) */
    val name: Rep[String] = column[String]("name", O.Length(255,varying=true))
    /** Database column created_at SqlType(DATETIME) */
    val createdAt: Rep[LocalDateTime] = column[LocalDateTime]("created_at")
    /** Database column updated_at SqlType(DATETIME) */
    val updatedAt: Rep[LocalDateTime] = column[LocalDateTime]("updated_at")
  }
  /** Collection-like TableQuery object for table KnowledgeRegisterStates */
  lazy val KnowledgeRegisterStates = new TableQuery(tag => new KnowledgeRegisterStates(tag))
}
