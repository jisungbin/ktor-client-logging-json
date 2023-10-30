package land.sungbin.ktor.client.plugins.logger.gson

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import land.sungbin.ktor.client.plugins.logging.JsonAwareLogger
import land.sungbin.ktor.client.plugins.logging.copy

private val DEFAULT_GSON =
  GsonBuilder().setPrettyPrinting().serializeNulls().setLenient().create()

/** Use Gson as a json beautifier. */
public fun JsonAwareLogger.useGson(gson: Gson = DEFAULT_GSON): JsonAwareLogger =
  copy(
    prettifyJson = { json ->
      val element = JsonParser.parseString(json)
      gson.toJson(element)
    }
  )
