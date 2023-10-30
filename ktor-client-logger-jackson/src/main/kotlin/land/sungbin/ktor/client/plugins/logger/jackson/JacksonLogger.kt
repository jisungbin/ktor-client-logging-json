package land.sungbin.ktor.client.plugins.logger.jackson

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import land.sungbin.ktor.client.plugins.logging.JsonAwareLogger
import land.sungbin.ktor.client.plugins.logging.copy

private val DEFAULT_MAPPER =
  ObjectMapper()
    .setDefaultLeniency(true)
    .setDefaultPrettyPrinter(DefaultPrettyPrinter())
    .enable(SerializationFeature.INDENT_OUTPUT)
    .setDefaultPropertyInclusion(JsonInclude.Include.ALWAYS)

/** Use Jackson as a json beautifier. */
public fun JsonAwareLogger.useJackson(mapper: ObjectMapper = DEFAULT_MAPPER): JsonAwareLogger =
  copy(
    prettifyJson = { json ->
      val element = mapper.readValue(json, Any::class.java)
      mapper.writeValueAsString(element)
    }
  )
