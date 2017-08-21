package com.knoldus.util

import java.io.{File, FileWriter, PrintWriter}

import scala.io.Source
import scala.util.Try

/**
  * Created by girish on 11/8/17.
  */
trait FileHelper extends LogHelper {

  def read(filePath: String): List[String] = {
    try{
      Source.fromFile(filePath).getLines().toList
    }catch{
      case ex: Exception =>
        info("Error found while creating a file")
        throw ex
    }
  }

  def write(lines: List[String], filePath: String): Boolean = {
    val writer = new FileWriter(filePath, true)
    val status = lines.map { line =>
      Try {
        writer.write(line)
        true
      }.toOption.getOrElse {
        error("Error found while writing data")
        false
      }
    }
    writer.close()
    !status.contains(false)
  }

  def reset(path: String) = {
    val fileTemp = new File(path)
    if (fileTemp.exists) {
      fileTemp.delete()
    }
  }

}
