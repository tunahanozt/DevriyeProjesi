package com.garoz.devriyemobil.database.shared

import app.cash.sqldelight.TransacterImpl
import app.cash.sqldelight.db.AfterVersion
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import com.garoz.devriyemobil.database.DevriyeDatabase
import com.garoz.devriyemobil.database.DevriyeLogQueries
import kotlin.Long
import kotlin.Unit
import kotlin.reflect.KClass

internal val KClass<DevriyeDatabase>.schema: SqlSchema<QueryResult.Value<Unit>>
  get() = DevriyeDatabaseImpl.Schema

internal fun KClass<DevriyeDatabase>.newInstance(driver: SqlDriver): DevriyeDatabase =
    DevriyeDatabaseImpl(driver)

private class DevriyeDatabaseImpl(
  driver: SqlDriver,
) : TransacterImpl(driver), DevriyeDatabase {
  override val devriyeLogQueries: DevriyeLogQueries = DevriyeLogQueries(driver)

  public object Schema : SqlSchema<QueryResult.Value<Unit>> {
    override val version: Long
      get() = 1

    override fun create(driver: SqlDriver): QueryResult.Value<Unit> {
      driver.execute(null, """
          |CREATE TABLE DevriyeLog (
          |    id INTEGER PRIMARY KEY AUTOINCREMENT,
          |    nfcUid TEXT NOT NULL,
          |    okumaZamani TEXT NOT NULL,
          |    sicilNo TEXT NOT NULL,
          |    senkronizeEdildi INTEGER NOT NULL DEFAULT 0
          |)
          """.trimMargin(), 0)
      return QueryResult.Unit
    }

    override fun migrate(
      driver: SqlDriver,
      oldVersion: Long,
      newVersion: Long,
      vararg callbacks: AfterVersion,
    ): QueryResult.Value<Unit> = QueryResult.Unit
  }
}
