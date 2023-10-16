/*
 * Copyright 2014-2022 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 * Modified by Ji Sungbin.
 */

package land.sungbin.ktor.client.plugins.logging

/**
 * [Logging] log level.
 */
public enum class LogLevel(
  public val info: Boolean,
  public val headers: Boolean,
  public val body: Boolean,
) {
  ALL(info = true, headers = true, body = true),
  HEADERS(info = true, headers = true, body = false),
  BODY(info = true, headers = false, body = true),
  INFO(info = true, headers = false, body = false),
  NONE(info = false, headers = false, body = false),
}
