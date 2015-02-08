package com.itnovate.MySQL2Redis

import com.github.shyiko.mysql.binlog.BinaryLogClient
import com.github.shyiko.mysql.binlog.BinaryLogClient._
import com.github.shyiko.mysql.binlog.event._
import com.google.gson.Gson

object MySQL2Redis extends App {
  val redisHandler = new RedisRPushEventHandler(System.getenv("REDIS_HOST"),
    System.getenv("REDIS_PORT").toInt)
  new BinlogTailer(redisHandler)
}