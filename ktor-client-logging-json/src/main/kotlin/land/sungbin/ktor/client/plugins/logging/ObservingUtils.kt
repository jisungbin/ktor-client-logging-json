/*
 * Copyright 2014-2022 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 * Modified by Ji Sungbin.
 */

package land.sungbin.ktor.client.plugins.logging

import io.ktor.http.content.OutgoingContent
import io.ktor.util.copyToBoth
import io.ktor.utils.io.ByteChannel
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.ByteWriteChannel
import io.ktor.utils.io.close
import io.ktor.utils.io.writeFully
import io.ktor.utils.io.writer
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope

internal suspend fun OutgoingContent.observe(log: ByteWriteChannel): OutgoingContent =
  when (this) {
    is OutgoingContent.ByteArrayContent -> {
      log.writeFully(bytes())
      log.close()
      this
    }
    is OutgoingContent.ReadChannelContent -> {
      val responseChannel = ByteChannel()
      val content = readFrom()
      content.copyToBoth(log, responseChannel)
      LoggedContent(this, responseChannel)
    }
    is OutgoingContent.WriteChannelContent -> {
      val responseChannel = ByteChannel()
      val content = toReadChannel()
      content.copyToBoth(log, responseChannel)
      LoggedContent(this, responseChannel)
    }
    else -> {
      log.close()
      this
    }
  }

@OptIn(DelicateCoroutinesApi::class)
private fun OutgoingContent.WriteChannelContent.toReadChannel(): ByteReadChannel =
  GlobalScope.writer(Dispatchers.Default) {
    writeTo(channel)
  }.channel
