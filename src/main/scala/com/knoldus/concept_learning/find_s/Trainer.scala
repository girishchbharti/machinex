package com.knoldus.concept_learning.find_s

import com.knoldus.util.LogHelper

import scala.collection.mutable

/**
  * Created by girish on 10/8/17.
  */
trait Trainer extends LogHelper {

  import scala.collection.mutable.Map

  val concept: Map[String, Any] = Map().empty

  def training(sample: Map[String, Any]): Boolean = {
    info(s"Processing training data... : $sample")
    sample.get("result") match {
      case Some(result: Boolean) =>
        if (result) {
          info(s"positive sample found, learning from sample......")
          learn(sample - "result")
          true
        } else {
          warn(s"Ignoring the sample: $sample")
          false
        }
      case None =>
        error("Invalid training sample found. Terminating learning process!")
        throw new Exception("Invalid training sample")
    }
  }

  private def learn(sample: Map[String, Any]): Map[String, Any] = {
    if (concept.isEmpty) {
      sample.map {
        case (k, v) => concept.put(k, v)
        case _ => error("Inconsistency found! Stopped learning! Please check your training data!")
          throw new Exception("Invalid training sample")
      }
      sample
    } else {
      findConjunctiveConcept(sample)
    }
  }

  private def findConjunctiveConcept(sample: Map[String, Any]): mutable.Map[String, Any] = {
    concept.foreach {
      case (k, v) =>
        sample.get(k) match {
          case Some(value) =>
            if (!v.equals(value)) {
              concept.update(k, "?")
            }
          case None => error("Inconsistency found! Stopped learning! Please check your training data!")
            throw new Exception("Invalid training sample")
        }
      case _ => error("Inconsistency found! Stopped learning! Please check your training data!")
        throw new Exception("Invalid training sample found! Stopping learning process!")
    }
    concept
  }

  def getTargetConcept: mutable.Map[String, Any] = concept

}
