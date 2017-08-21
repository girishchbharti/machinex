package com.knoldus.conceptlearning.common


/**
  * Created by girish on 16/8/17.
  */
case class FileType(`type`: String)

object FileTypes{
  val json = FileType("json")
  val text = FileType("text")
  val csv = FileType("csv")
}

