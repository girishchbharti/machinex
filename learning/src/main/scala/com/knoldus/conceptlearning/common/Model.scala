package com.knoldus.conceptlearning.common

/**
  * Created by girish on 18/8/17.
  */
trait Model {

  val resKey: String

  def training(sample: scala.collection.immutable.Map[String, Any]): Boolean

  def getHypothesis: Any

  def predict(dataObject: scala.collection.immutable.Map[String, Any]): Boolean

  def persist: Boolean

  def trained: Boolean

}
