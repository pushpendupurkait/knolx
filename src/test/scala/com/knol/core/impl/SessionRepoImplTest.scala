package com.knol.core.impl

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import com.knol.core.Knolx
import java.util.Date
import scala.collection.mutable.MutableList
import com.knol.core.Knol
import java.sql._
import com.knol.db.connection.DBConnection

class SessionRepoImplTest extends FunSuite  with BeforeAndAfter with DBConnection{
  val sessionRepoImpl = new SessionRepoImpl
  val objKnol = Knolx("topicName", "2001/02/02", 1)
  val objKnolFail = Knolx("topicName", "2012/08/02", 19,Some(1))
  before {
   val conn: Option[Connection] = gotConnection()
    conn match {
      case Some(conn) => {
        try {
          val knolQuery = "CREATE TABLE knol (id int(11) PRIMARY KEY AUTO_INCREMENT, name varchar(50), email varchar(50) UNIQUE KEY, mobile varchar(15))"
          var knolxQuery = "CREATE TABLE knolx (id int(11) PRIMARY KEY AUTO_INCREMENT, topic varchar(255),"
          knolxQuery += " date date, knol_id int(11) NOT NULL, FOREIGN KEY(knol_id) REFERENCES knol(id))"
          val knolStmt = conn.createStatement()
          val knolxStmt = conn.createStatement()
          knolStmt.execute(knolQuery)
          knolxStmt.execute(knolxQuery)
          sessionRepoImpl.create(Knol("pushpendu", "pushpendu@knoldus.com", "9887901096", Some(1)))
          sessionRepoImpl.createNewSession(Knolx("topicName", "2012/02/08", 1))
          conn.close()
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
  after {
    val conn: Option[Connection] = gotConnection()
    conn match {
      case Some(conn) => {
        try {
          val queryKnolx = "drop table knolx, knol"
          val stmtKnolx = conn.createStatement()
          stmtKnolx.execute(queryKnolx)
          conn.close()
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
  test("Create a knol") {
    assert(sessionRepoImpl.createNewSession(objKnol) === Some(2))
  }
  test("FAIL Create a knol") {
    assert(sessionRepoImpl.createNewSession(objKnolFail) === None)
  }
  test("Search a knol Session") {
    val dt = "2001/02/02"
    assert(sessionRepoImpl.searchSessionByID(1) === Some(Knolx("topicName",dt,1,Some(1))))
  }
  test("FAIL Search a knol Session") {
    val dt = "02/02/2001"
    assert(sessionRepoImpl.searchSessionByID(2) === None)
  }
  test("Delete a knol Session") {
    assert(sessionRepoImpl.deleteSessionByID(1) === Some(1))
  }  
  test("FAIL Delete a knol Session") {
    assert(sessionRepoImpl.deleteSessionByID(10) === None)
  }
  test("update a knol Session") {
      assert(sessionRepoImpl.updateSession(objKnol) === Some(1))
  }
  test("Fail update a knol Session") {
      assert(sessionRepoImpl.updateSession(objKnolFail) === None)
  }
  test("get list of knol") {
    assert(sessionRepoImpl.getList() === Some(MutableList(Knolx("topicName","2012-02-08",1,Some(1)))))
  }
}

