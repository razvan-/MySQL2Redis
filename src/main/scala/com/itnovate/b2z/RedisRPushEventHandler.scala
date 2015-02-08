package com.itnovate.MySQL2Redis

import redis.clients.jedis._

class RedisRPushEventHandler(redisHost: String, redisPort: Int) extends JsonEventHandler {
 val jedis = new Jedis(redisHost, redisPort)
  def write(jsonEvent: String): Unit = {
    jedis.lpush("database_events", jsonEvent)
  }
}