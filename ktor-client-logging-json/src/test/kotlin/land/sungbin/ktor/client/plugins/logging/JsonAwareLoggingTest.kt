@file:Suppress("PrivatePropertyName")

package land.sungbin.ktor.client.plugins.logging

import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.headersOf
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

class JsonAwareLoggingTest {
  private val `Hello, World!` = "Hello, World!"
  private val `prettied-json` = "prettied-json"

  @Test
  fun xml_body() = runTest {
    val jsonAwareLogs = mutableListOf<String>()
    val logs = mutableListOf<String>()

    val engine = MockEngine { _ ->
      respond(
        content = `Hello, World!`,
        status = HttpStatusCode.OK,
        headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Xml.toString())
      )
    }
    val client = HttpClient(engine) {
      JsonAwareLogging {
        logger = object : JsonAwareLogger {
          override fun log(message: String) {
            jsonAwareLogs += message
            println("[JsonAwareLogger] $message")
          }

          override fun prettifyJson(json: String) = `prettied-json`
        }
      }
      Logging {
        logger = object : Logger {
          override fun log(message: String) {
            logs += message
            println("[Logger] $message")
          }
        }
        level = LogLevel.BODY
      }
    }

    client.get("") {
      contentType(ContentType.Application.Xml)
      setBody(`Hello, World!`)
    }

    jsonAwareLogs shouldBe logs
  }

  @Test
  fun json_body() = runTest {
    val jsonAwareLogs = mutableListOf<String>()
    val logs = mutableListOf<String>()

    val engine = MockEngine { _ ->
      respond(
        content = `Hello, World!`,
        status = HttpStatusCode.OK,
        headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
      )
    }
    val client = HttpClient(engine) {
      JsonAwareLogging {
        logger = object : JsonAwareLogger {
          override fun log(message: String) {
            jsonAwareLogs += message
            println("[JsonAwareLogger] $message")
          }

          override fun prettifyJson(json: String) = `prettied-json`
        }
      }
      Logging {
        logger = object : Logger {
          override fun log(message: String) {
            logs += message
            println("[Logger] $message")
          }
        }
        level = LogLevel.BODY
      }
    }

    client.get("") {
      contentType(ContentType.Application.Json)
      setBody(`Hello, World!`)
    }

    val jsonAwareLogBodys: List<String> =
      jsonAwareLogs.toList()
        .foldIndexed(mutableListOf()) { index, acc, log ->
          val logLines = log.split("\n").toMutableList()
          val bodyIndex = logLines.indexOfFirst { line -> line == "BODY START" } + 1
          val body = logLines.removeAt(bodyIndex)

          jsonAwareLogs[index] = logLines.joinToString("\n")
          acc.apply { add(body) }
        }
    val logBodys: List<String> =
      logs.toList()
        .foldIndexed(mutableListOf()) { index, acc, log ->
          val logLines = log.split("\n").toMutableList()
          val bodyIndex = logLines.indexOfFirst { line -> line == "BODY START" } + 1
          val body = logLines.removeAt(bodyIndex)

          logs[index] = logLines.joinToString("\n")
          acc.apply { add(body) }
        }

    jsonAwareLogs shouldContainExactly logs
    jsonAwareLogBodys shouldContainExactly listOf(`prettied-json`, `prettied-json`)
    logBodys shouldContainExactly listOf(`Hello, World!`, `Hello, World!`)
  }

  @Test
  fun haljson_body() = runTest {
    val jsonAwareLogs = mutableListOf<String>()
    val logs = mutableListOf<String>()

    val engine = MockEngine { _ ->
      respond(
        content = `Hello, World!`,
        status = HttpStatusCode.OK,
        headers = headersOf(HttpHeaders.ContentType, ContentType.Application.HalJson.toString())
      )
    }
    val client = HttpClient(engine) {
      JsonAwareLogging {
        logger = object : JsonAwareLogger {
          override fun log(message: String) {
            jsonAwareLogs += message
            println("[JsonAwareLogger] $message")
          }

          override fun prettifyJson(json: String) = `prettied-json`
        }
      }
      Logging {
        logger = object : Logger {
          override fun log(message: String) {
            logs += message
            println("[Logger] $message")
          }
        }
        level = LogLevel.BODY
      }
    }

    client.get("") {
      contentType(ContentType.Application.HalJson)
      setBody(`Hello, World!`)
    }

    val jsonAwareLogBodys: List<String> =
      jsonAwareLogs.toList()
        .foldIndexed(mutableListOf()) { index, acc, log ->
          val logLines = log.split("\n").toMutableList()
          val bodyIndex = logLines.indexOfFirst { line -> line == "BODY START" } + 1
          val body = logLines.removeAt(bodyIndex)

          jsonAwareLogs[index] = logLines.joinToString("\n")
          acc.apply { add(body) }
        }
    val logBodys: List<String> =
      logs.toList()
        .foldIndexed(mutableListOf()) { index, acc, log ->
          val logLines = log.split("\n").toMutableList()
          val bodyIndex = logLines.indexOfFirst { line -> line == "BODY START" } + 1
          val body = logLines.removeAt(bodyIndex)

          logs[index] = logLines.joinToString("\n")
          acc.apply { add(body) }
        }

    jsonAwareLogs shouldContainExactly logs
    jsonAwareLogBodys shouldContainExactly listOf(`prettied-json`, `prettied-json`)
    logBodys shouldContainExactly listOf(`Hello, World!`, `Hello, World!`)
  }
}