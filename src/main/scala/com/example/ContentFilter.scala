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
  val messageExchangeDispatcher = system.actorOf(Props[MessageExchangeDispatcher], "messageExchangeDispatcher")

  messageExchangeDispatcher ! UnfilteredPayload("A very large message with complex structure...")

  awaitCompletion
  println("RequestReply: is completed.")
}

class MessageExchangeDispatcher extends Actor {
  val messageContentFilter = context.actorOf(Props[MessageContentFilter], "messageContentFilter")

  override def receive: Receive = {
    case message: UnfilteredPayload =>
      println("MessageExchangeDispatcher: received unfiltered message: " + message.largePayload)
      messageContentFilter ! message
      ContentFilterDriver.completedStep()
    case message: FilteredMessage =>
      println("MessageExchangeDispatcher: dispatching: " + message)
      ContentFilterDriver.completedStep()
    case _ =>
      println("MessageExchangeDispatcher: received unexpected message")
  }
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