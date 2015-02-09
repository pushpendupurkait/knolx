package com.knol.core.impl

import java.sql._
import com.knol.core._
import com.knol.db.connection.DBConnection

class KnolRepoImpl extends DBConnection with KnolRepo {
  /**
   * create method is to insert a new row in table.
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
          val query = "insert into knol(name,email,mobile) values('" + name + "','" + email + "','" + mobile + "')"
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
   * deleteKnolderByID is used to delete a row by passing ID as an argument.
   */
  def deleteKnolderByID(id: Int): Option[Int] = {
    val conn: Option[Connection] = gotConnection()
    conn match {
      case Some(conn) => {
        try {
          val query = "delete from knol where id=" + id
          val stmt = conn.prepareStatement(query, Statement.KEEP_CURRENT_RESULT)
          val rs = stmt.executeUpdate();
          require(rs>0)
          print(rs + " row succesfully deleted")
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
   * searchKnolderByID is used to search a row by using ID.
   */
  def searchKnolderByID(id: Int): Option[Knol] = {
    val conn: Option[Connection] = gotConnection()
    conn match {
      case Some(conn) => {
        try {
          val query = "select * from knol where id=" + id + ";"
          val stmt = conn.createStatement()
          val rs: ResultSet = stmt.executeQuery(query)
          rs.next()
          val name = rs.getString("name")
          val email = rs.getString("email")
          val mobile = rs.getString("mobile")
          Some(Knol(mobile, name, email, Some(id)))
        } catch {
          case ex: Exception => {
            logger.error("exception occured", ex)
            None
          }
        }
      }
      case None =>  None
    }
  }
  /**
   * updateKnolder is used to make modification in Rows.
   */
  def updateKnolder(knolx: Knol): Option[Int] = {
    val conn: Option[Connection] = gotConnection()
    conn match {
      case Some(conn) => {
        try {
          val id = knolx.id.get
          val name=knolx.name
          val email = knolx.email
          val mobile = knolx.mobile
          val query = "update knol set name='" + name + "',email='" + email + "',mobile ='" + mobile + "' where id=" + id
          val stmt = conn.createStatement()
          val result = stmt.executeUpdate(query)
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
   * getList is used to get all rows from table knol.
   */
  def getList(): Option[scala.collection.mutable.MutableList[Knol]] = {
    val conn: Option[Connection] = gotConnection()
    conn match {
      case Some(conn) => {
        try {
          val query = "select * from knol"
          val stmt = conn.createStatement()
          val result: ResultSet = stmt.executeQuery(query)
          val list = scala.collection.mutable.MutableList[Knol]()
          while (result.next()) {
            val id = result.getInt("id")
            val name = result.getString("name")
            val email = result.getString("email")
            val mobile = result.getString("mobile")
            list += Knol(name, email, mobile, Some(id))
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
