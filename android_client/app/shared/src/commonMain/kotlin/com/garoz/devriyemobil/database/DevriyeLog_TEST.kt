package com.garoz.devriyemobil.database

import kotlin.Long
import kotlin.String

public data class DevriyeLog(
  public val id: Long,
  public val nfcUid: String,
  public val okumaZamani: String,
  public val sicilNo: String,
  public val senkronizeEdildi: Long,
)
