package com.knoldus.conceptlearning.common


/**
  * Responsibility of this class would be
  * 1. Accepting/reading training data
  * 2. Dividing it into two parts
  *   a. Training data
  *   b. Validation data
  * 3. Training Find-S algorithm with training data
  * 4. Verification of the algorithm with validation data
  */
trait Trainer {

  //Train the algorithm
  def train: Boolean

  //Read data from file
  protected def read: List[Map[String, Any]]

  //separate data into two files
  protected def separate(data: List[Map[String, Any]]): (List[Map[String, Any]], List[Map[String, Any]])

  //pass training data to algorithm
  protected def training(sample: List[Map[String, Any]]): Boolean

  //Validate final hypothesis
  protected def validate(validationData: List[Map[String, Any]]): Boolean

}
