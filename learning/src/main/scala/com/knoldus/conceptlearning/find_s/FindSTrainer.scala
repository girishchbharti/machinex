package com.knoldus.conceptlearning.find_s

import com.knoldus.conceptlearning.common.{Model, Trainer}
import com.knoldus.exceptions.InconsistentTrainingDataException
import com.knoldus.util.JsonHelper
import com.knoldus.util.reader.JsonReader


trait FindSTrainer extends Trainer with JsonHelper {

  val trainingSampleFilePath: String
  val model: Model
  val trainingRatio: Double = 0.5

  //Train the algorithm
  def train: Boolean = {
    val data: List[Map[String, Any]] = read
    val (trainingData, validationData) = separate(data)
    val trainingStatus = training(trainingData)
    val validationSuccessful = validate(validationData)
    if (validationSuccessful) {
      model.persist
    } else {
      throw new Exception(s"Inconsistent training data with training status: $trainingStatus validation status: $validationSuccessful")
    }
  }

  //Read data from file
  override protected def read: List[Map[String, Any]] = {
    trainingSampleFilePath.split('.')(1) match {
      case "json" => info(s"json file to be read..: $trainingSampleFilePath")
        new JsonReader(trainingSampleFilePath).read
      case _ => List.empty
    }
  }

  //separate data into two files
  override protected def separate(data: List[Map[String, Any]]): (List[Map[String, Any]], List[Map[String, Any]]) = {
    val splitAt = (data.size * trainingRatio).toInt
    data.splitAt(splitAt)
  }

  //pass training data to algorithm
  override protected def training(trainingData: List[Map[String, Any]]): Boolean = {
    trainingData.forall { data =>
      model.training(data)
      true
    }
  }

  override protected def validate(validationData: List[Map[String, Any]]): Boolean = {
    validationData.forall { data: Map[String, Any] =>
      val expectedResult = model.predict(data)
      data.get(model.resKey) match {
        case Some(actualResult) =>
          if (expectedResult == actualResult) true else false
        case None => throw InconsistentTrainingDataException("Invalid training sample")
      }
    }
  }

}
