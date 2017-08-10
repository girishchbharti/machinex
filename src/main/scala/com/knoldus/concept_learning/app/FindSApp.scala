package com.knoldus.concept_learning.app

import akka.actor.{ActorSystem, Props}
import akka.pattern._
import akka.util.Timeout
import com.knoldus.concept_learning.domains.FindS.{Concept, DataObject}
import com.knoldus.concept_learning.actors.{FindSActor, GetHypothesis}
import com.knoldus.concept_learning.domains.{TrainingData, TumorReport}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.util.Random


/**
  * Created by girish on 3/8/17.
  */
object FindSApp extends App {

  implicit val timeout = Timeout(5 seconds)
  val actorSystem = ActorSystem("FindS-ActorSystem")
  val trainerActor = actorSystem.actorOf(Props[FindSActor])

  /********************************
    * TRAINING
    ********************************/

  val trainingDataSamples: List[TrainingData] = TrainingDataGenerator.generateTrainingData

  println("**************************Start Training************************************")
  //Training of actor is being start
  trainingDataSamples map { trainingData =>
    println(s"Training data: ${trainingData}")
    trainerActor ! trainingData
  }

  Thread.sleep(3000)

  /***************************************
    * HYPOTHESIS
    **************************************/
  (trainerActor ? GetHypothesis) map {
    case res: Option[Concept] =>
      println("Final hypothesis.........>>>>>>>>>>>>>>>", res)
  }
  println("*****************************Training finished****************************")
  Thread.sleep(2000)


  /*****************************************************
   * TESTING
   ******************************************************/
  println("*****************************Testing****************************")
  println("""***************("circular", "large", "dark", "smooth")***************""")

  (trainerActor ? new DataObject("circular", "large", "dark", "smooth")) map {
    case Some(positive: Boolean) => if (positive) {
      println("Positive.......!!")
    } else {
      println("Negative.......!!")
    }
    case msg: String =>
      println("ERROR: " + msg)
  }

  actorSystem.terminate()
}


object TrainingDataGenerator {

  val shape = List("oval", "circular")
  val size = List("large", "small")
  val color = List("dark", "light")
  val surface = List("smooth", "irregular")

  def generateTrainingData: List[TrainingData] = {
    List.range(0, 1000).map { res =>
      new TrainingData(
        TumorReport(shape(random), size(random), color(random), surface(random)),
        if (random == 1) {
          true
        } else {
          false
        }
      )
    }
  }

  private def random = Random.nextInt(2)
}
