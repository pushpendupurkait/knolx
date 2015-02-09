package com.knol.core.impl

import com.knol.core._
import java.text.SimpleDateFormat
import java.util.Date
import com.knol.db.connection.DBConnection
import java.sql._
import org.slf4j.LoggerFactory
import org.omg.CORBA.Request

class SessionRepoImpl extends SessionRepo with DBConnection {
  /**
   * create is used to insert a new row in table.
   */
  def create(knolx: Knol): Option[Int] = {
    val conn: Option[Connection] = gotConnection()
    conn match {
      case Some(conn) => {
        try {
          val id = knolx.id.get
          val name = knolx.name
          val email = knolx.email
          val mobile = knolx.mobile
          val query = "insert into knol(name,email,mobile,id) values('" + name + "','" + email + "','" + mobile + "'," + id + ")"
          val stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
          stmt.executeUpdate
          val rs = stmt.getGeneratedKeys
          rs.next()
          "New ID is " + rs.getInt(1)
          Some(rs.getInt(1))
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
  /**
   * searchSessionByID is used to search any row by using ID.
   */
  def searchSessionByID(id: Int): Option[Knolx] = {
    val dateField = "2001/02/02"
    val logger = LoggerFactory.getLogger(this.getClass)
    val conn: Option[Connection] = gotConnection()
    conn match {
      case Some(conn) => {
        try {
          val query = "select * from knolx where id=" + id
          val stmt = conn.createStatement()
          val rs: ResultSet = stmt.executeQuery(query)
          rs.next()
          val topic = rs.getString("topic")
          val knol_id = rs.getInt("knol_id")
          val id_knolx = rs.getInt("id")
          Some(Knolx(topic, dateField, knol_id, Some(id_knolx)))
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
  /**
   * createNewSession is used to insert a row in Table knolx.
   */
  def createNewSession(knolx: Knolx): Option[Int] = {
    val conn: Option[Connection] = gotConnection()
    conn match {
      case Some(conn) => {
        try {
          val innerQuery = "select id from knol where id=" + knolx.knol_id
          val topic = knolx.topic
          val date = knolx.date
          val query = "insert into knolx(topic,date,knol_id) values('" + topic + "','" + date + "',(" + innerQuery + "))"
          val stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
          stmt.executeUpdate
          val rs = stmt.getGeneratedKeys
          rs.next()

          Some(rs.getInt(1))
        } catch {
          case ex: Exception => {
            logger.error("exception, May be there is no knolder with same knol_id. " + ex)
            None
          }
        }
      }
      case None => None
    }
  }
  /**
   * deleteSessionByID is used to delete a row from table knolx using ID
   */
  def deleteSessionByID(id: Int): Option[Int] = {
    val conn: Option[Connection] = gotConnection()
    conn match {
      case Some(conn) => {
        try {
          val query = "delete from knolx where id=" + id
          val stmt = conn.prepareStatement(query, Statement.KEEP_CURRENT_RESULT)
          val rs = stmt.executeUpdate();
          print(rs + " row succesfully deleted")
          require(rs > 0)
          Some(rs)
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
  /**
   * deleteSessionByID is used to update a row from table knolx using ID
   */
  def updateSession(knolx: Knolx): Option[Int] = {
    val conn: Option[Connection] = gotConnection()
    conn match {
      case Some(conn) => {
        try {
          val topic = knolx.topic
          val date = knolx.date
          val id = knolx.knol_id
          val query = "update knolx set topic='" + topic + "',date='" + date + "' where id=" + knolx.knol_id
          val stmt = conn.createStatement()
          val result = stmt.executeUpdate(query)
          require(result > 0)
          Some(result)
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
  /**
   * getList is used to get all rows from knolx Table.
   */
  def getList(): Option[scala.collection.mutable.MutableList[Knolx]] = {
    val conn: Option[Connection] = gotConnection()
    conn match {
      case Some(conn) => {
        try {
          val query = "select * from knolx"
          val stmt = conn.createStatement()
          val result: ResultSet = stmt.executeQuery(query)
          val list = scala.collection.mutable.MutableList[Knolx]()
          while (result.next()) {
            val id = result.getInt("id")
            val topic = result.getString("topic")
            val date = result.getString("date")
            val knol_id = result.getInt("knol_id")
            list += Knolx(topic, date, knol_id, Some(id))
          }
          Some(list)
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
