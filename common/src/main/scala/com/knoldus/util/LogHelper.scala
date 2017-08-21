package com.knoldus.util

import org.slf4j.{Logger, LoggerFactory}

import scala.util.Try

/**
  * Created by girish on 10/8/17.
  */
object LogLevel extends Enumeration {
  type LogLevel = Value
  val logLevels = List("debug", "info", "warn", "error")
  val DEBUG, INFO, WARN, ERROR = Value
}

trait LogHelper {

  import LogLevel._

  private val logger: Logger = LoggerFactory.getLogger(this.getClass())
  private val logLevel = logLevels.indexOf(Try("info").toOption
    .getOrElse("info").toLowerCase.trim)

  protected def debug(implicit msg: String): Unit = log(DEBUG)(logger.debug)

  protected def info(implicit msg: String): Unit = log(INFO)(logger.info)

  protected def warn(implicit msg: String): Unit = log(WARN)(logger.warn)

  protected def error(implicit msg: String): Unit = log(ERROR)(logger.error)

  protected def debug(implicit msg: String, ex: Throwable): Unit = logEx(DEBUG)(ex, logger.debug)

  protected def info(implicit msg: String, ex: Throwable): Unit = logEx(INFO)(ex, logger.info)

  protected def warn(implicit msg: String, ex: Throwable): Unit = logEx(WARN)(ex, logger.warn)

  protected def error(implicit msg: String, ex: Throwable): Unit = logEx(ERROR)(ex, logger.error)

  private def log(level: Value)(f: String => Unit)(implicit msg: String): Unit = if (level.id >= logLevel) f(msg)

  private def logEx(level: Value)(ex: Throwable, f: (String, Throwable) => Unit)(implicit msg: String): Unit =
    if (level.id >= logLevel) f(msg, ex)
}

