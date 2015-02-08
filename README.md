# MySQL to Redis Binlog

Simple tool to push INSERT, UPDATE and DELETE events from a MySQL Binlog to a Redis List from where they can be consumed by workers.

The events will be LPUSHed to Redis key "database_events" as a JSON:
```json
{
  "event_type": "EXT_WRITE_ROWS|EXT_UPDATE_ROWS|EXT_DELETE_ROWS",
  "table_name": "some_table",
  "database_name": "some_database",
  "rows": "[ affected rows - see bellow ]"
}
```

The affected rows will have two forms depending on the event_type.

For WRITE & DELETE events the rows will be an Array of Arrays containing one or more affected rows with the content of all the fields in the table in the order they are in the database.

For UPDATE events, the rows will be an Array of Key-Value pairs representing the affected rows. Inside the "key" field you will find the contents of the row pre-UPDATE and in the "value" field the post-UPDATE values. Sorry for the bad naming.

### What do I use it for?
Mostly for extracting things that are implemented as callbacks thus allowing my service to not have to know about the existence of other services/workers if it doesn't need to.
Examples:
  1. Cache invalidation (not using it now but would apply to Varnish)
  2. Solr Synchronization (I actually have some business logic in my documents so a simple DIH wouldn't be enough)

### Preparing the database
Your database needs to have
  1. ROW based replication enabled
  2. a user with REPLICATION_CLIENT and REPLICATION_SLAVE permissions enabled:
  ```sql
GRANT REPLICATION CLIENT, REPLICATION SLAVE ON *.* TO 'imareplicator'@'%' identified by 'blahblah';
```

# Running
```sh
docker run \
  -e DB_HOST=db.acme.com \
  -e DB_PORT=3306 \
  -e DB_USER=imareplicator \
  -e DB_PASS=blahblah \
  -e REDIS_HOST=127.0.0.1 \
  -e REDIS_PORT=6379 \
  -p 6379 \
  itnovate/mysql2redis
```

The docker instance has a local redis running but you can override it if you need to.

### Version
barely 0.0.1-alpha

### Tech

* [Scala]
* [mysql-binlog-connector-java]
* [jedis]

License
----

MIT



[Scala]:http://www.scala-lang.org/
[mysql-binlog-connector-java]:https://github.com/shyiko/mysql-binlog-connector-java
[jedis]:https://github.com/xetorthio/jedis
