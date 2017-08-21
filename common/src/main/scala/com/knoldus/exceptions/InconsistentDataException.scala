package com.knoldus.exceptions

/**
  * Created by girish on 11/8/17.
  */
trait MachinexException extends Exception

case class InconsistentTrainingDataException(label: String) extends MachinexException

case class InconsistentDataException(label: String) extends MachinexException

