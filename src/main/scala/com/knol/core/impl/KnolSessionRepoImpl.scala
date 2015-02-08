package com.knol.core.impl

import com.knol.db.connection.DBConnection
import com.knol.core.KnolSessionRepo
import com.knol.core.JoinedKnol
import java.sql._
import com.knol.core.Knolx
import com.knol.core.Knol

class KnolSessionRepoImpl extends DBConnection with KnolSessionRepo {
  def joinedSelect(id: Int): Option[JoinedKnol] = {
    val conn: Option[Connection] = gotConnection
    conn match {
      case Some(conn) => {
        try {
          val query = "select knol.id,knolx.id,knol.name,knol.email,knol.mobile,knolx.date from knol, knolx where "+ id +"=knolx.knol_id "
          val stmt = conn.createStatement()
          val result = stmt.executeQuery(query)
          result.next()
          val knol_id = result.getInt("knol.id")
          val session_id = result.getInt("knolx.id")
          val name = result.getString("knol.name")
          val email = result.getString("knol.email")
          val mobile = result.getString("knol.mobile")
          val date = result.getString("knolx.date")
          Some(JoinedKnol(knol_id, session_id, name, email, mobile, date))
        } catch {
          case ex: Exception => {
            logger.error("exception occured", ex)
            None
          }
        }
      }
      case None => None
    }
  }
}
