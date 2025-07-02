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

package profiles

import slick.jdbc.JdbcProfile

import java.sql.PreparedStatement
import java.sql.ResultSet
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

trait ExMySQLProfile extends JdbcProfile with slick.jdbc.MySQLProfile { driver =>
  private val localDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

  override val columnTypes = new ExJdbcTypes

  class ExJdbcTypes extends super.JdbcTypes {

    @inline
    private[this] def stringToMySqlString(value: String): String = {
      value match {
        case null => "NULL"
        case _ =>
          val sb = new StringBuilder
          sb.append('\'')
          for (c <- value) c match {
            case '\'' => sb.append("\\'")
            case '"'  => sb.append("\\\"")
            case 0    => sb.append("\\0")
            case 26   => sb.append("\\Z")
            case '\b' => sb.append("\\b")
            case '\n' => sb.append("\\n")
            case '\r' => sb.append("\\r")
            case '\t' => sb.append("\\t")
            case '\\' => sb.append("\\\\")
            case _    => sb.append(c)
          }
          sb.append('\'')
          sb.toString
      }
    }

    /**
     * Override LocalDateTime handler, to parse values as we expect them.
     *
     * The default implementation in Slick does not support TIMESTAMP or DATETIME
     * columns, but expects timestamps to be stored as VARCHAR
     */
    override val localDateTimeType: LocalDateTimeJdbcType = new LocalDateTimeJdbcType {
      override def sqlType: Int = java.sql.Types.TIMESTAMP
      override def setValue(v: LocalDateTime, p: PreparedStatement, idx: Int): Unit = {
        p.setString(idx, if (v == null) null else v.toString)
      }
      override def getValue(r: ResultSet, idx: Int): LocalDateTime = {
        r.getString(idx) match {
          case null          => null
          case iso8601String => LocalDateTime.parse(iso8601String, localDateTimeFormatter)
        }
      }
      override def updateValue(v: LocalDateTime, r: ResultSet, idx: Int) = {
        r.updateString(idx, if (v == null) null else v.format(localDateTimeFormatter))
      }
      override def valueToSQLLiteral(value: LocalDateTime): String = {
        stringToMySqlString(value.format(localDateTimeFormatter))
      }
    }

  }
}

trait MySQLProfile extends ExMySQLProfile {}

object MySQLProfile extends MySQLProfile
