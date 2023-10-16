package land.sungbin.ktor.client.plugins.logging

import com.squareup.moshi.JsonReader
import com.squareup.moshi.Moshi
import io.kotest.matchers.collections.shouldContainExactly
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.get
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.headersOf
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest
import okio.Buffer
import io.ktor.client.plugins.logging.Logger as StdLogger
import io.ktor.client.plugins.logging.Logging as StdLogging

class PrettyJsonLoggingTest {
  private val anyAdapter = Moshi.Builder().build().adapter(Any::class.java).indent("  ")
  private val testJson = "{ \"key1\": \"value1\", \"key2\": \"value2\" }"

  @Test
  fun moshi_with_any_adapter() {
    val value = JsonReader.of(Buffer().writeUtf8(testJson)).readJsonValue()
    val result = anyAdapter.toJson(value)

    assertEquals(
      expected = """
      |{
      |  "key1": "value1",
      |  "key2": "value2"
      |}
      """.trimMargin(),
      actual = result,
    )
  }

  @Test
  fun equal_stdlogging_logging() = runTest {
    val messages = mutableListOf<String>()
    val stdMessages = mutableListOf<String>()
    val engine = MockEngine { _ ->
      respond(
        content = testJson,
        status = HttpStatusCode.OK,
        headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
      )
    }
    val client = HttpClient(engine) {
      install(Logging) {
        logger = object : Logger {
          override fun log(message: String) {
            messages += message
          }
        }
      }
      install(StdLogging) {
        logger = object : StdLogger {
          override fun log(message: String) {
            stdMessages += message
          }
        }
      }
    }

    client.get("/")

    messages shouldContainExactly stdMessages
  }

  @Test
  fun json_content_pretty_test() = runTest {
    val messages = mutableListOf<String>()
    val engine = MockEngine { _ ->
      respond(
        content = testJson,
        status = HttpStatusCode.OK,
        headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
      )
    }
    val client = HttpClient(engine) {
      install(io.ktor.client.plugins.logging.Logging) {
        logger = object : /*Logger, */io.ktor.client.plugins.logging.Logger {
          override fun log(message: String) {
            messages += message
          }

          /*override fun prettifyJson(json: String): String {
            val value = JsonReader.of(Buffer().writeUtf8(json)).readJsonValue()
            return anyAdapter.toJson(value)
          }*/
        }
      }
    }

    val response = client.get("/") {
      setBody(testJson)
      contentType(ContentType.Application.Json)
    }.bodyAsText()

    println(response)
    println(messages.joinToString("\n"))
  }
}