package land.sungbin.ktor.client.plugins.logger.moshi

import io.kotest.matchers.collections.shouldContainExactly
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.get
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.headersOf
import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import land.sungbin.ktor.client.plugins.logging.JsonAwareLogger
import land.sungbin.ktor.client.plugins.logging.JsonAwareLogging

class MoshiLoggerTest {
  @Test
  fun prettify_json() = runTest {
    val minifyJson = "{\"userId\":1,\"id\":null,\"title\":\"delectus aut autem\",\"completed\":false}"
    val prettifyJson = """
      {
        "userId": 1.0,
        "id": null,
        "title": "delectus aut autem",
        "completed": false
      }
    """.trimIndent()

    val logs = mutableListOf<String>()

    val engine = MockEngine { _ ->
      respond(
        content = minifyJson,
        status = HttpStatusCode.OK,
        headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
      )
    }
    val client = HttpClient(engine) {
      JsonAwareLogging {
        logger = object : JsonAwareLogger {
          override fun log(message: String) {
            logs += message
            println(message)
          }
        }.useMoshi()
      }
    }

    client.get("") {
      contentType(ContentType.Application.Json)
      setBody(minifyJson)
    }

    val logBodys = logs.map { log ->
      val logLines = log.split("\n").toMutableList()
      val bodyStartIndex = logLines.indexOfFirst { line -> line == "BODY START" } + 1
      val bodyEndIndex = logLines.indexOfFirst { line -> line == "BODY END" }

      logLines.subList(bodyStartIndex, bodyEndIndex).joinToString("\n")
    }

    logBodys shouldContainExactly listOf(prettifyJson, prettifyJson)
  }
}