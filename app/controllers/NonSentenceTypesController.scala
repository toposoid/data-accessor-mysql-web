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

package controllers

import com.ideal.linked.toposoid.common.{TRANSVERSAL_STATE, ToposoidUtils, TransversalState}
import com.ideal.linked.toposoid.knowledgebase.regist.rdb.model.NonSentenceTypesRecord
import com.typesafe.scalalogging.LazyLogging
import dao.NonSentenceTypesDao
import play.api.libs.json.Json
import play.api.mvc.{BaseController, ControllerComponents}

import javax.inject.Inject
import scala.concurrent.ExecutionContext

/*
case class NonSentenceTypesRecord(stateId:Long, name:String)
object NonSentenceTypesRecord {
  implicit val jsonWrites: OWrites[NonSentenceTypesRecord] = Json.writes[NonSentenceTypesRecord]
  implicit val jsonReads: Reads[NonSentenceTypesRecord] = Json.reads[NonSentenceTypesRecord]
}
*/
class NonSentenceTypesController @Inject()(nonSentenceTypesDao:NonSentenceTypesDao, val controllerComponents: ControllerComponents)(implicit ec: ExecutionContext) extends BaseController  with LazyLogging{

  def getAll = Action(parse.json) { request =>
    val transversalState = Json.parse(request.headers.get(TRANSVERSAL_STATE.str).get).as[TransversalState]
    try {
      val result = nonSentenceTypesDao.all()
      val nonSentenceTypesRecords:List[NonSentenceTypesRecord] = result.map(x => NonSentenceTypesRecord(x.id, x.name)).toList
      Ok(Json.toJson(nonSentenceTypesRecords))
    } catch {
      case e: Exception => {
        logger.error(ToposoidUtils.formatMessageForLogger(e.toString, transversalState.userId), e)
        BadRequest(Json.obj("status" -> "Error", "message" -> e.toString()))
      }
    }
  }
}
