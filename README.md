## ktor-client-logging-json

If content-type is `json` or `hal+json`, it will automatically output the beautified json.

| Before | After |
| :---: | :---: |
| ![image](https://github.com/jisungbin/ktor-client-logging-json/assets/40740128/2db5de96-5fd8-4c28-9da9-aad0545af335) | ![image](https://github.com/jisungbin/ktor-client-logging-json/assets/40740128/5205b000-5edd-4197-bd43-35bba1414094) |

### Usage

This plugin is implemented in the same way as the official [`ktor-client-logging`](https://ktor.io/docs/client-logging.html). 
However, there are three differences:

1. all interfaces/properties are prefixed with `JsonAware`.
2. the `Logger` interface has a `fun prettifyJson(json: String): String = json` function is added.
3. the default log level is raised to `LogLevel.Body`.

Added to the `Logger` interface, the `prettifyJson(json: String): String` function is called when 
the content to be logged is of type `json` or `hal+json`, and the result is logged.

The Json deserialization library to use for the `prettifyJson()` implementation comes with
the Moshi, Jackson, and Gson implementations by default.

By default, the Json deserialization rules are as follows:

- Indent 2 spaces
- Allow `null`
- Allow lenient json

The specific implementations are available as `JsonAwareLogger.useMoshi()`, `JsonAwareLogger.useJackson()`, and `JsonAwareLogger.useGson()`.

### Download

- `ktor-client-logging-json`: ![ktor-client-logging-json](https://img.shields.io/maven-central/v/land.sungbin.ktor.client.logging/ktor-client-logging-json?style=flat-square)
- `ktor-client-logger-moshi`: ![ktor-client-logger-moshi](https://img.shields.io/maven-central/v/land.sungbin.ktor.client.logger/ktor-client-logger-moshi?style=flat-square)
- `ktor-client-logger-jackson`: ![ktor-client-logger-jackson](https://img.shields.io/maven-central/v/land.sungbin.ktor.client.logger/ktor-client-logger-jackson?style=flat-square)
- `ktor-client-logger-gson`: ![ktor-client-logger-gson](https://img.shields.io/maven-central/v/land.sungbin.ktor.client.logger/ktor-client-logger-gson?style=flat-square)

```kotlin
dependencies {
  implementation("land.sungbin.ktor.client.logging:ktor-client-logging-json:${version}")
  
  implementation("land.sungbin.ktor.client.logger:ktor-client-logger-moshi:${version}")
  implementation("land.sungbin.ktor.client.logger:ktor-client-logger-jackson:${version}")
  implementation("land.sungbin.ktor.client.logger:ktor-client-logger-gson:${version}")
}
```
