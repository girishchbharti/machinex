package com.knoldus.util.reader

import com.knoldus.util.{FileHelper, JsonHelper}

/**
  * Created by girish on 16/8/17.
  */
class JsonReader(filePath: String) extends FileHelper with FileReader with JsonHelper {

  /**
    * This function takes json file path and returns List[Map[String, Any]]
    *
    * @return List[Map[String, Any]]
    */
  def read: List[Map[String, Any]] = {
    val json: String = read(filePath).mkString(" ")
    try {
      parseMaps(json)
    } catch {
      case ex: Exception =>
        throw ex
    }
  }

}
