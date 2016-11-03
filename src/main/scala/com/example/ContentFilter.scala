package com.example

import akka.actor.Actor.Receive
import akka.actor._

case class FilteredMessage(light: String, and: String, fluffy: String, message: String) {
  override def toString: String = {
    s"FilteredMessage(" + light + " " + and + " " + fluffy + " " + message + ")"
  }
}

case class UnfilteredPayload(largePayload: String)

object ContentFilterDriver extends CompletableApp(3) {
}

class MessageContentFilter extends Actor {
  override def receive: Receive = {
    case message: UnfilteredPayload =>
      println("MessageContentFilter: received unfiltered message: " + message.largePayload)
      // filtering occurs...
      sender ! FilteredMessage("this", "feels", "so", "right")
      ContentFilterDriver.completedStep()
    case _ =>
      println("MessageContentFilter: received unexpected message")
  }
}