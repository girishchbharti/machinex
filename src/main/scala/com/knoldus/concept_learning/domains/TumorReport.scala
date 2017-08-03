package com.knoldus.concept_learning.domains

/**
  * Created by girish on 3/8/17.
  */

case class TumorReport(
                        shape: String,
                        size: String,
                        color: String,
                        surface: String
                      )

case class TrainingData(
                         sample: TumorReport,
                         result: Boolean
                       )


