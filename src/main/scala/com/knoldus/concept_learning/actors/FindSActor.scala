package com.knoldus.concept_learning.actors

import akka.actor.Actor
import akka.event.Logging
import com.knoldus.util.LogHelper

import scala.util.Try


case object GetHypothesis

case object FinishTraining



class FindSActor extends Actor with LogHelper {
  import context._
  import scala.collection.mutable.Map

  val log = Logging(context.system, this)
  val concept: Map[String, Any] = Map().empty

  override def preStart(): Unit = {
    info("Actor has become a trainy!")
    become(trainy)
  }

  def trainy: Receive = {

    case trainingSample: Map[String, Any] =>
      info(s"Processing training data... : $trainingSample")
      trainingSample.get("result") match {
        case Some(result: Boolean) =>
          if (result) {
            info(s"positive sample found, learning from sample......")
            learn(trainingSample - "result")
          } else {
            warn(s"Ignoring the sample: $trainingSample")
          }
        case None =>
          error("Invalid training sample found. Terminating learning process!")
          become(angry)
          throw new Exception("Invalid training sample")
      }

    case GetHypothesis => sender ! concept

    case FinishTraining => become(trained)

    case msg => error(s"Oops! Did not understand the message: $msg")

  }

  def trained: Receive = {

    case dataObject: Map[String, Any] =>
      info(s"Data object found for classification: $dataObject")
      sender ! Try(predict(dataObject)).toOption.getOrElse("Exception found while processing. Please check the actor state!")

    case _ => sender ! "Invalid data object found!"
  }

  private def predict(dataObject: Map[String, Any]): Boolean = {
    !concept.map {
      case (k, v) =>
        dataObject.get(k) match {
          case Some(value) => if (v.equals(value) || v == "?") true else false
          case None => error("Invalid object found! Stopping analysis!")
            throw new Exception("Invalid object found!")
        }
      case _ => error("Inconsistency found! Please check your training data and train the model again!")
        throw new Exception("Inconsistency found!")
    }.toList.contains(false)
  }

  def angry: Receive = {
    case _ => sender ! "Inconsistency found! Stopped learning! Please check your training data!"
  }

  private def learn(sample: Map[String, Any]): Unit = {
    if (concept.isEmpty) {
      sample.map {
        case (k, v) => concept.put(k, v)
        case _ => error("Inconsistency found! Stopped learning! Please check your training data!")
          become(angry)
          throw new Exception("Invalid training sample")
      }
    } else {
      findConjunctiveConcept(sample)
    }
  }

  private def findConjunctiveConcept(sample: Map[String, Any]): Unit = {
    concept.foreach {
      case (k, v) =>
        sample.get(k) match {
          case Some(value) =>
            if (!v.equals(value)) {
              concept.update(k, "?")
            }
          case None => error("Inconsistency found! Stopped learning! Please check your training data!")
            become(angry)
            throw new Exception("Invalid training sample")
        }
      case _ => error("Inconsistency found! Stopped learning! Please check your training data!")
        become(angry)
        throw new Exception("Invalid training sample found! Stopping learning process!")
    }
  }

  def receive: Receive = trainy

}
