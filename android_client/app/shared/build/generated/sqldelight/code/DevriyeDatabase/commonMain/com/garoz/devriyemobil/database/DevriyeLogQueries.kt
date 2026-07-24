package com.garoz.devriyemobil.database

import app.cash.sqldelight.Query
import app.cash.sqldelight.TransacterImpl
import app.cash.sqldelight.db.SqlDriver
import kotlin.Any
import kotlin.Long
import kotlin.String

public class DevriyeLogQueries(
  driver: SqlDriver,
) : TransacterImpl(driver) {
  public fun <T : Any> tumKayitlariGetir(mapper: (
    id: Long,
    nfcUid: String,
    okumaZamani: String,
    sicilNo: String,
    senkronizeEdildi: Long,
  ) -> T): Query<T> = Query(-782_463_837, arrayOf("DevriyeLog"), driver, "DevriyeLog.sq",
      "tumKayitlariGetir", "SELECT * FROM DevriyeLog WHERE senkronizeEdildi = 0") { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2)!!,
      cursor.getString(3)!!,
      cursor.getLong(4)!!
    )
  }

  public fun tumKayitlariGetir(): Query<DevriyeLog> = tumKayitlariGetir { id, nfcUid, okumaZamani,
      sicilNo, senkronizeEdildi ->
    DevriyeLog(
      id,
      nfcUid,
      okumaZamani,
      sicilNo,
      senkronizeEdildi
    )
  }

  public fun kayitEkle(
    nfcUid: String,
    okumaZamani: String,
    sicilNo: String,
  ) {
    driver.execute(805_363_743, """
        |INSERT INTO DevriyeLog(nfcUid, okumaZamani, sicilNo, senkronizeEdildi)
        |VALUES (?, ?, ?, 0)
        """.trimMargin(), 3) {
          bindString(0, nfcUid)
          bindString(1, okumaZamani)
          bindString(2, sicilNo)
        }
    notifyQueries(805_363_743) { emit ->
      emit("DevriyeLog")
    }
  }

  public fun durumGuncelle(id: Long) {
    driver.execute(-769_617_778, """UPDATE DevriyeLog SET senkronizeEdildi = 1 WHERE id = ?""", 1) {
          bindLong(0, id)
        }
    notifyQueries(-769_617_778) { emit ->
      emit("DevriyeLog")
    }
  }
}
