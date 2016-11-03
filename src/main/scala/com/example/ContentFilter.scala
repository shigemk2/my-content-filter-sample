package com.example

import akka.actor._

case class FilteredMessage(light: String, and: String, fluffy: String, message: String) {
  override def toString: String = {
    s"FilteredMessage(" + light + " " + and + " " + fluffy + " " + message + ")"
  }
}

case class UnfilteredPayload(largePayload: String)

object ContentFilterDriver {
}
