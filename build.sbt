name := "MySQL2Redis"

version := "1.0"

scalaVersion := "2.11.5"

initialCommands in console := """
import com.itnovate.b2z._
import com.github.shyiko.mysql.binlog.BinaryLogClient
import com.github.shyiko.mysql.binlog.BinaryLogClient._
import com.github.shyiko.mysql.binlog.event._
import com.google.gson.Gson
"""

libraryDependencies ++= List(
  "com.github.shyiko" % "mysql-binlog-connector-java" % "0.1.1",
  "redis.clients" % "jedis" % "2.6.2",
  "com.google.code.gson" % "gson" % "2.3.1"
)