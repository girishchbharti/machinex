package com.knoldus.app

import com.knoldus.conceptlearning.common.Model
import com.knoldus.conceptlearning.find_s.{FindS, FindSTrainer}
import com.knoldus.datagenerator.ConceptLearningTrainingDataGenerator
import com.knoldus.util.{FileHelper, LogHelper}


/**
  * Find-S example
  */
object FindSExample extends App with LogHelper {

  /** ******************************
    * TRAINING DATA GENERATION
    * *******************************/
  val trainingDataFilePath = ConceptLearningTrainingDataGenerator.randomTrainingData

  /** ******************************
    * TRAINER INITIALIZATION
    * *******************************/
  val path = "/tmp/find_s"
  val jsonHelper = new FileHelper {}.reset(path)
  val trainer = new FindSTrainer {
    val trainingSampleFilePath = trainingDataFilePath
    val model: Model = new FindS("result", path)
    override val trainingRatio = 1.0
  }

  /** ******************************
    * TRAINING
    * *******************************/
  if (!trainer.model.trained) {
    trainer.train
  } else {
    info("Model is trained, skipping training process")
  }

  /** ******************************
    * TRAINED MODEL
    * *******************************/
  val trainedModel = trainer.model

  info(s"***Hypothesis: ${trainedModel.getHypothesis}")

  /** ***********************************
    * TESTING
    * ***********************************/
  val testDataObject = Map("sky" -> "Sunny", "airtemp" -> "Cool", "humidity" -> "Warm", "wind" -> "Weak",
    "water" -> "Cool", "forecast" -> "Change")
  info(s"***Testing new positive data object: $testDataObject")
  val status = trainedModel.predict(testDataObject)
  if (status) {
    info("***THE DATA OBJECT IS ... : +POSITIVE")
  } else {
    info("***THE DATA OBJECT IS ... : -NEGATIVE")
  }

}
