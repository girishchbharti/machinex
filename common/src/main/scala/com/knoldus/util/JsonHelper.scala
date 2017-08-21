package com.knoldus.util


import java.text.SimpleDateFormat

import org.json4s
import org.json4s.JsonAST.{JInt, JLong}
import org.json4s.{CustomSerializer, JDouble, JNothing, JNull, JString, JValue}
import org.json4s.native.JsonMethods.{render, parse => jParser, pretty => jPretty}
import org.json4s.native.Serialization.{write => jWrite}

import scala.util.Try


/**
  * Created by girish on 11/8/17.
  */
trait JsonHelper {

  val EMPTY_STRING = ""
  val JavaNull = null

  implicit val formats = new org.json4s.DefaultFormats {
    override def dateFormatter: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")
  } ++ List(StringToBigDecimalSerializer)

  case object StringToBigDecimalSerializer extends CustomSerializer[BigDecimal](format => (
    {
      case JInt(value) => BigDecimal(value.longValue)
      case JLong(value) => BigDecimal(value)
      case JNull => JavaNull
      case JString(value) => Try(BigDecimal(value.toInt)).getOrElse(BigDecimal(0))
    },
    {
      case d: BigDecimal => JDouble(d.toDouble)
    }
  ))

  protected def write[T <: AnyRef](value: T): String = jWrite(value)

  protected def pretty(value: String): String = jPretty(render(parse(value)))

  protected def parse(value: String): JValue = jParser(value)

  protected def parseMaps(string: String): List[Map[String, Any]] =  parse(string).extract[List[Map[String, Any]]]

  protected def parseMap(string: String): Map[String, Any] =  parse(string).extract[Map[String, Any]]

  implicit protected def extractOrEmptyString(json: JValue): String = json match {
    case JNothing => EMPTY_STRING
    case data     => data.extract[String]
  }

}
