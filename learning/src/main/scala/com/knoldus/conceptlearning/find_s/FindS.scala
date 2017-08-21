package com.knoldus.conceptlearning.find_s

import com.knoldus.conceptlearning.common.Model
import com.knoldus.exceptions.InconsistentTrainingDataException
import com.knoldus.util.{FileHelper, JsonHelper, LogHelper}
import scala.util.control.NonFatal


/**
  * Created by girish on 11/8/17.
  */
class FindS(key: String = "result", hypothesisDir: String = "/tmp") extends Model with LogHelper with FileHelper with JsonHelper {

  private val metaData: scala.collection.mutable.Map[String, Any] = scala.collection.mutable.Map.empty
  private val hypothesis: scala.collection.mutable.Map[String, Any] = init
  val resKey = key
  val path = hypothesisDir + "/find_s"

  def trained: Boolean = metaData.getOrElse("trained", false).toString.toBoolean

  def init: scala.collection.mutable.Map[String, Any] = {
    try {
      read(path).headOption match {
        case Some(hypothesis) =>
          val temp: scala.collection.mutable.Map[String, Any] = scala.collection.mutable.Map.empty
          parse(hypothesis).extract[Map[String, Any]].toList.map { case (k, v) => temp += (k -> v) }
          temp
        case None => scala.collection.mutable.Map.empty
      }
    } catch {
      case NonFatal(ex) =>
        error("Error found while reading hypothesis from file", ex)
        scala.collection.mutable.Map.empty
    }
  }

  def persist: Boolean = {
    reset(hypothesisDir)
    val status = write(List(write(hypothesis)), hypothesisDir)
    metaData.+=("trained" -> true)
    status
  }

  def getHypothesis: scala.collection.mutable.Map[String, Any] = hypothesis

  def predict(dataObject: scala.collection.immutable.Map[String, Any]): Boolean = {
    !hypothesis.map {
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

  def training(sample: scala.collection.immutable.Map[String, Any]): Boolean = {
    info(s"Processing training data... : $sample")
    sample.get(key) match {
      case Some(result: Boolean) =>
        if (result) {
          info(s"positive sample found, learning from sample......")
          learn(sample - key)
        } else {
          warn(s"Ignoring the sample: $sample")
          false
        }
      case None =>
        error("Invalid training sample found. Terminating learning process!")
        throw InconsistentTrainingDataException("Invalid training sample")
    }
  }

  @throws[InconsistentTrainingDataException]
  private def learn(sample: scala.collection.immutable.Map[String, Any]): Boolean = {
    if (hypothesis.isEmpty) {
      sample.map {
        case (k, v) => hypothesis.put(k, v)
          true
        case _ => error("Inconsistency found! Stopped learning! Please check your training data!")
          throw InconsistentTrainingDataException("Invalid training sample")
      }.toList.contains(true)
    } else {
      updateHypothesis(sample)
    }
  }

  @throws[InconsistentTrainingDataException]
  private def updateHypothesis(sample: scala.collection.immutable.Map[String, Any]): Boolean = {
    hypothesis.map {
      case (k, v) =>
        sample.get(k) match {
          case Some(value) =>
            if (!v.equals(value)) {
              hypothesis.update(k, "?")
              true
            } else {
              false
            }
          case None => error("Inconsistency found! Stopped learning! Please check your training data!")
            throw InconsistentTrainingDataException("Invalid training sample")
        }
      case _ => error("Inconsistency found! Stopped learning! Please check your training data!")
        throw InconsistentTrainingDataException("Invalid training sample found! Stopping learning process!")
    }.toList.contains(true)
  }

}
