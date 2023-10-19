package land.sungbin.ktor.client.plugins.logger.moshi

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.Moshi
import land.sungbin.ktor.client.plugins.logging.JsonAwareLogger
import land.sungbin.ktor.client.plugins.logging.copy
import okio.Buffer

private val DEFAULT_ADAPTER =
  Moshi.Builder()
    .build()
    .adapter(Any::class.java)
    .indent("  ")
    .serializeNulls()
    .lenient()

public fun JsonAwareLogger.useMoshi(adapter: JsonAdapter<Any> = DEFAULT_ADAPTER): JsonAwareLogger =
  copy(
    prettifyJson = { json ->
      val element = JsonReader.of(Buffer().writeUtf8(json)).readJsonValue()
      adapter.toJson(element)
    }
  )
