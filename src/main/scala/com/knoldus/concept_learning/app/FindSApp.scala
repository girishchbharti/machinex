package com.knoldus.concept_learning.app

import akka.actor.{ActorSystem, Props}
import akka.pattern._
import akka.util.Timeout
import com.knoldus.concept_learning.actors.{FindSActor, FinishTraining, GetHypothesis}
import com.knoldus.util.LogHelper

import scala.collection.mutable.Map
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.util.Random


/**
  * Created by girish on 3/8/17.
  */
object FindSApp extends App with LogHelper {

  implicit val timeout = Timeout(5 seconds)
  val actorSystem = ActorSystem("FindS-ActorSystem")
  val trainerActor = actorSystem.actorOf(Props[FindSActor])


  /** ******************************
    * TRAINING
    * *******************************/

  val trainingDataSamples: List[Map[String, Any]] = TrainingDataGenerator.generateTrainingData

  info("***Start Training...")

  val res = trainingDataSamples map { trainingData =>
    info(s"Training data: $trainingData")
    trainerActor ! trainingData
    true
  }

  //Finish training
  info("Finishing training...")
  res.map { _ =>
    trainerActor ! FinishTraining
  }
  info("***Training finished!")




  /** *************************************
    * HYPOTHESIS
    * *************************************/

  val finalHypothesis = (trainerActor ? GetHypothesis)
  finalHypothesis map { hypothesis =>
    info(s"***FINAL HYPOTHESIS... : $hypothesis")
  }

  /** ***************************************************
    * TESTING
    * ***************************************************/

  info("***Testing new data object: <shape -> circular, size -> large, color -> dark, surface -> smooth>")

  val response = trainerActor ? Map("shape" -> "circular", "size" -> "large", "color" -> "dark", "surface" -> "smooth")

  response.map {
    case status: Boolean =>
      if (status) {
        info("***THE DATA OBJECT IS ... : +POSITIVE")
      } else {
        info("***THE DATA OBJECT IS ... : -NEGATIVE")
      }
      status
    case msg: String => error("ERROR FOUND WHILE PROCESSING: " + msg)
      false
  }

  response.onComplete(_ => actorSystem.terminate())
}


object TrainingDataGenerator {

  val shape = List("oval", "circular")
  val size = List("large", "small")
  val color = List("dark", "light")
  val surface = List("smooth", "irregular")

  def generateTrainingData: List[Map[String, Any]] = {
    List.range(0, 4).map { _ =>
      val result = if (random == 1) true else false
      Map("shape" -> shape(random), "size" -> size(random), "color" -> color(random), "surface" -> surface(random),
        "result" -> result)
    }
  }

  private def random = Random.nextInt(2)
}
