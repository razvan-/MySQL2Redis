package com.itnovate.MySQL2Redis

trait JsonEventHandler {
  def write(s: String): Unit
}