package com.itnovate.MySQL2Redis

import com.github.shyiko.mysql.binlog.BinaryLogClient
import com.github.shyiko.mysql.binlog.BinaryLogClient._
import com.github.shyiko.mysql.binlog.event._
import com.google.gson.Gson
import scala.language.existentials

class BinlogTailer(eventHandler: JsonEventHandler) {
  val gson = new Gson()
  val tableMap = collection.mutable.Map.empty[Long, (String, String)]
  val (db_host, db_port, db_user, db_pass) = (System.getenv("DB_HOST"), System.getenv("DB_PORT").toInt,
    System.getenv("DB_USER"), System.getenv("DB_PASS"))
  val client = new BinaryLogClient(db_host, db_port, db_user, db_pass)

  client.registerEventListener(new EventListener() {
    override def onEvent(event: Event) {
      val eventType = event.getHeader[EventHeaderV4].getEventType()

      if (eventType == EventType.TABLE_MAP) {
        val data = event.getData[TableMapEventData]
        if (!tableMap.contains(data.getTableId))
          tableMap.update(data.getTableId, (data.getDatabase, data.getTable))
      }

      val (rows, tableId) = eventType match {
        case EventType.EXT_WRITE_ROWS =>
          val data = event.getData[WriteRowsEventData]
          (data.getRows(), data.getTableId)
        case EventType.EXT_UPDATE_ROWS =>
          val data = event.getData[UpdateRowsEventData]
          (data.getRows(), data.getTableId)
        case EventType.EXT_DELETE_ROWS =>
          val data = event.getData[DeleteRowsEventData]
          (data.getRows(), data.getTableId)
        case _ => (null, -1L)
      }

      if (tableId != -1) {
        val json = s"""{
          |"event_type": "${eventType}",
          |"table_name": "${tableMap(tableId)._2}",
          |"database_name": "${tableMap(tableId)._1}",
          |"rows": ${gson.toJson(rows)}
        |}""".stripMargin
        eventHandler.write(json)
      }
    }
  })
  client.connect()
}