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

package controllers

import com.ideal.linked.toposoid.common.{TRANSVERSAL_STATE, ToposoidUtils, TransversalState}
import com.typesafe.scalalogging.LazyLogging
import dao.KnowledgeRegisterStatesDao
import play.api.libs.json.{Json, OWrites, Reads}
import play.api.mvc.{BaseController, ControllerComponents}

import javax.inject.Inject
import scala.concurrent.ExecutionContext

case class KnowledgeRegisterStatesRecord(stateId:Long, name:String)
object KnowledgeRegisterStatesRecord {
  implicit val jsonWrites: OWrites[KnowledgeRegisterStatesRecord] = Json.writes[KnowledgeRegisterStatesRecord]
  implicit val jsonReads: Reads[KnowledgeRegisterStatesRecord] = Json.reads[KnowledgeRegisterStatesRecord]
}

class KnowledgeRegisterStatesController @Inject()(knowledgeRegisterStatesDao:KnowledgeRegisterStatesDao, val controllerComponents: ControllerComponents)(implicit ec: ExecutionContext) extends BaseController  with LazyLogging{

  def getAll = Action(parse.json) { request =>
    val transversalState = Json.parse(request.headers.get(TRANSVERSAL_STATE.str).get).as[TransversalState]
    try {
      val result = knowledgeRegisterStatesDao.all()
      val knowledgeRegisterStatesRecords:List[KnowledgeRegisterStatesRecord] = result.map(x => KnowledgeRegisterStatesRecord(x.id, x.name)).toList
      Ok(Json.toJson(knowledgeRegisterStatesRecords))
    } catch {
      case e: Exception => {
        logger.error(ToposoidUtils.formatMessageForLogger(e.toString, transversalState.userId), e)
        BadRequest(Json.obj("status" -> "Error", "message" -> e.toString()))
      }
    }
  }
}
