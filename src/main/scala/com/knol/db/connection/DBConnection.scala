package com.knol.db.connection

import java.sql.Connection
import java.sql.DriverManager

import org.slf4j.LoggerFactory

import com.typesafe.config.ConfigFactory
/**
 * DBConnection is used to establish connection in database knol.
 */
trait DBConnection {
  val config = ConfigFactory.load()
  val url = config.getString("db.url")
  val usr = config.getString("db.user")
  val password = config.getString("db.password")
  val driver = config.getString("db.driver")
  val logger = LoggerFactory.getLogger(this.getClass)
  def gotConnection(): Option[Connection] = {
    try {
      Class.forName(driver)
      var conn = DriverManager.getConnection(url, usr, password)
      Some(conn)
    } catch {
      case ex: Exception => {
        logger.error("exception occured", ex)
        None
      }
    }
  }
}
