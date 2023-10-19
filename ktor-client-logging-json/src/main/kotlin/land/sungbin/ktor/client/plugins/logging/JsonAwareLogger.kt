/*
 * Copyright 2014-2022 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 * Modified by Ji Sungbin.
 */

package land.sungbin.ktor.client.plugins.logging

import io.ktor.client.HttpClient
import org.slf4j.LoggerFactory

/**
 * [HttpClient] Logger.
 */
public interface JsonAwareLogger {
  /**
   * Add [message] to log.
   */
  public fun log(message: String)

  /**
   * Format the [json] pretty.
   */
  public fun prettifyJson(json: String): String = json

  public companion object
}

public fun JsonAwareLogger.copy(
  log: (message: String) -> Unit = ::log,
  prettifyJson: (json: String) -> String = ::prettifyJson,
): JsonAwareLogger = object : JsonAwareLogger {
  override fun log(message: String) {
    log(message)
  }

  override fun prettifyJson(json: String): String {
    return prettifyJson(json)
  }
}

/**
 * Default logger to use.
 */
public val JsonAwareLogger.Companion.DEFAULT: JsonAwareLogger
  get() = object : JsonAwareLogger {
    private val delegate = LoggerFactory.getLogger(HttpClient::class.java)!!
    override fun log(message: String) {
      delegate.info(message)
    }
  }

/**
 * Android [JsonAwareLogger]: breaks up long log messages that would be truncated by Android's max log
 * length of 4068 characters
 */
public val JsonAwareLogger.Companion.ANDROID: JsonAwareLogger
  get() = MessageLengthLimitingJsonAwareLogger()

/**
 * A [JsonAwareLogger] that breaks up log messages into multiple logs no longer than [maxLength]
 *
 * @property maxLength max length allowed for a log message
 * @property minLength if log message is longer than [maxLength], attempt to break the log
 * message at a new line between [minLength] and [maxLength] if one exists
 */
public class MessageLengthLimitingJsonAwareLogger(
  private val maxLength: Int = 4000,
  private val minLength: Int = 3000,
  private val delegate: JsonAwareLogger = JsonAwareLogger.DEFAULT,
) : JsonAwareLogger {
  override fun log(message: String) {
    longLog(message)
  }

  private tailrec fun longLog(message: String) {
    // String to be logged is longer than the max...
    if (message.length > maxLength) {
      var msgSubstring = message.substring(0, maxLength)
      var msgSubstringEndIndex = maxLength

      // Try to find a substring break at a newline char.
      msgSubstring.lastIndexOf('\n').let { lastIndex ->
        if (lastIndex >= minLength) {
          msgSubstring = msgSubstring.substring(0, lastIndex)
          // skip over new line char
          msgSubstringEndIndex = lastIndex + 1
        }
      }

      // Log the substring.
      delegate.log(msgSubstring)

      // Recursively log the remainder.
      longLog(message.substring(msgSubstringEndIndex))
    } else {
      delegate.log(message)
    } // String to be logged is shorter than the max...
  }
}

/**
 * [JsonAwareLogger] using [println].
 */
public val JsonAwareLogger.Companion.SIMPLE: JsonAwareLogger get() = SimpleJsonAwareLogger()

/**
 * Empty [JsonAwareLogger] for test purpose.
 */
public val JsonAwareLogger.Companion.EMPTY: JsonAwareLogger
  get() = object : JsonAwareLogger {
    override fun log(message: String) {}
  }

private class SimpleJsonAwareLogger : JsonAwareLogger {
  override fun log(message: String) {
    println("HttpClient: $message")
  }
}
