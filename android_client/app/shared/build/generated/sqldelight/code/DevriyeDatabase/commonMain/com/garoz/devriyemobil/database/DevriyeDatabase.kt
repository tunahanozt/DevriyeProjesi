package com.garoz.devriyemobil.database

import app.cash.sqldelight.Transacter
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import com.garoz.devriyemobil.database.shared.newInstance
import com.garoz.devriyemobil.database.shared.schema
import kotlin.Unit

public interface DevriyeDatabase : Transacter {
  public val devriyeLogQueries: DevriyeLogQueries

  public companion object {
    public val Schema: SqlSchema<QueryResult.Value<Unit>>
      get() = DevriyeDatabase::class.schema

    public operator fun invoke(driver: SqlDriver): DevriyeDatabase =
        DevriyeDatabase::class.newInstance(driver)
  }
}
