package com.knol.core.impl

import scala.collection.mutable.MutableList
import org.scalatest.BeforeAndAfter
import org.scalatest.FunSuite
import com.knol.core.Knol
import java.sql._
import com.knol.db.connection.DBConnection

class KnolRepoImplTest extends FunSuite with BeforeAndAfter with DBConnection {
  val knolRepoImpl = new KnolRepoImpl
  val objKnol = Knol("pushpendu", "pushpendupurkait@gmail.com", "123456",Some(3))
  val objKnolFail=Knol("pushpendu", "pushpendu@knoldus.com", "9887901096",Some(1))
  before {
    val conn: Option[Connection] = gotConnection
    conn match {
      case Some(conn) => {
        try {
          val query = "CREATE TABLE knol (id int(11) PRIMARY KEY AUTO_INCREMENT, name varchar(50), email varchar(50) UNIQUE KEY, mobile varchar(15))"
          val stmt = conn.createStatement()
          stmt.execute(query)
          knolRepoImpl.create(Knol("pushpendu", "pushpendu@knoldus.com", "9887901096", Some(1)))
          knolRepoImpl.create(Knol("pushpendu", "pushpendu@knol.com", "9887901096", Some(2)))
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
    //knolRepoImpl.dropTable
    val conn: Option[Connection] = gotConnection()
    conn match {
      case Some(conn) => {
        try {
          val query = "drop table knol"
          val stmt = conn.createStatement()
          stmt.execute(query)
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
    assert(knolRepoImpl.create(objKnol) === Some(3))
  }
  test("FAIL Create a knol") {
    assert(knolRepoImpl.create(objKnolFail) === None)
  }
  test("Delete a knol") {
    assert(knolRepoImpl.deleteKnolderByID(1) === Some(1))
  }
  test("Fail Delete a knol") {
    assert(knolRepoImpl.deleteKnolderByID(4) === None)
  }
  test("Search a knol") {
    assert(knolRepoImpl.searchKnolderByID(1) === Some(Knol("9887901096","pushpendu","pushpendu@knoldus.com",Some(1))))
  }
  test("Fail Search a knol") {
    assert(knolRepoImpl.searchKnolderByID(3) === None)
  }
  test("update a knol") {
    assert(knolRepoImpl.updateKnolder(Knol("pushpendu","pushpenduP@knol.com","9887901096",Some(1))) === Some(1))
  }
  test("FAIL update a knol") {
    assert(knolRepoImpl.updateKnolder(Knol("pushpendu","pushpendu@knoldus.com","9887901096",Some(2))) === None)
  }
  test("get list of knol") {
    assert(knolRepoImpl.getList() === Some(MutableList(Knol("pushpendu","pushpendu@knoldus.com","9887901096",Some(1)), Knol("pushpendu","pushpendu@knol.com","9887901096",Some(2)))))
  }
}
