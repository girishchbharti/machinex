package com.knoldus.util.reader

/**
  * Created by girish on 16/8/17.
  */
trait FileReader {

  //Read file content as List[Map[String, Any]]
  def read: List[Map[String, Any]]

}
