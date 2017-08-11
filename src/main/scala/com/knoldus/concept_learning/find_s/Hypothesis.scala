package com.knoldus.concept_learning.find_s

import com.knoldus.util.LogHelper

import scala.collection.mutable.Map

/**
  * Created by girish on 10/8/17.
  */
trait Hypothesis extends LogHelper {

  val hypothesis: Map[String, Any]

  def predict(dataObject: Map[String, Any]): Boolean = {
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

}
