package com.knoldus.datagenerator

import com.knoldus.util.{FileHelper, JsonHelper}

import scala.util.Random

/**
  * Prediction:
  * IF a person will go to play football (Yes/No)
  *
  * Features:
  * sky (Sunny, Rainy)
  * airtemp (Warm, Cold)
  * humidity (Normal, High)
  * wind (Strong, Weak)
  * water (Warm, Cool)
  * forecast (Same, Change)
  *
  * @return
  */
object ConceptLearningTrainingDataGenerator extends FileHelper with JsonHelper {

  val filePath = "/tmp/training_data.json"

  val sky = List("Sunny", "Rainy")
  val airTemp = List("Warm", "Cold")
  val humidity = List("Normal", "High")
  val wind = List("Strong", "Weak")
  val water = List("Warm", "Cool")
  val forecast = List("Same", "Change")

  def randomTrainingData: String = {
    val trainingDataMap: List[Map[String, Any]] = List.range(0, 8).map { _ =>
      val result = if (random == 1) true else false
      Map("sky" -> sky(random), "airtemp" -> airTemp(random), "humidity" -> humidity(random), "wind" -> wind(random),
        "water" -> water(random), "forecast" -> forecast(random), "result" -> result)
    }
    writeToJsonFile(trainingDataMap)
    filePath
  }

  private def random = Random.nextInt(2)

  def sampleTrainingData: String = {
    val trainingDataMap: List[Map[String, Any]] = List(
      Map("sky" -> "Sunny", "airtemp" -> "Cool", "humidity" -> "Normal", "wind" -> "Strong",
        "water" -> "Cool", "forecast" -> "Same", "result" -> true),
      Map("sky" -> "Rainy", "airtemp" -> "Warm", "humidity" -> "High", "wind" -> "Weak",
        "water" -> "Warm", "forecast" -> "Change", "result" -> false),
      Map("sky" -> "Sunny", "airtemp" -> "Cool", "humidity" -> "Normal", "wind" -> "Weak",
        "water" -> "Cool", "forecast" -> "Same", "result" -> true),
      Map("sky" -> "Rainy", "airtemp" -> "Warm", "humidity" -> "High", "wind" -> "Weak",
        "water" -> "Warm", "forecast" -> "Same", "result" -> false),
      Map("sky" -> "Sunny", "airtemp" -> "Cool", "humidity" -> "Normal", "wind" -> "Strong",
        "water" -> "Cool", "forecast" -> "Change", "result" -> true),
      Map("sky" -> "Rainy", "airtemp" -> "Warm", "humidity" -> "High", "wind" -> "Weak",
        "water" -> "Warm", "forecast" -> "Same", "result" -> false),
      Map("sky" -> "Sunny", "airtemp" -> "Cool", "humidity" -> "Warm", "wind" -> "Strong",
        "water" -> "Cool", "forecast" -> "Change", "result" -> true)
    )
    writeToJsonFile(trainingDataMap)
    filePath
  }

  private def writeToJsonFile(trainingDataMap: List[Map[String, Any]]): Boolean = {
    reset(filePath)
    val jsonDataString = write(trainingDataMap)
    write(List(jsonDataString), filePath)
  }

}
