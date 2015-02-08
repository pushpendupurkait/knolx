package com.knol.core.impl

import java.sql.Connection

import org.scalatest.BeforeAndAfter
import org.scalatest.FunSuite

import com.knol.core.JoinedKnol
import com.knol.core.Knol
import com.knol.core.Knolx
import com.knol.db.connection.DBConnection

class KnolSessionRepoTest extends FunSuite with BeforeAndAfter with DBConnection{
  val KnolSessionImpl = new SessionRepoImpl
  val knolSessionRepoImplTest = new KnolSessionRepoImpl
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
          KnolSessionImpl.create(Knol("pushpendu", "pushpendu@knoldus.com", "9887901096", Some(1)))
          KnolSessionImpl.createNewSession(Knolx("topicName", "2012/02/08", 1))
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
  test("joining test") {
    assert(knolSessionRepoImplTest.joinedSelect(1) === Some(JoinedKnol(1,1,"pushpendu","pushpendu@knoldus.com","9887901096","2012-02-08")))
  }
  test("Fail joining test") {
    assert(knolSessionRepoImplTest.joinedSelect(2) === None)
  }
}