package com.garoz.devriyemobil.database
import app.cash.sqldelight.db.SqlDriver

import android.content.Context
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

actual class DatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(DevriyeDatabase.Schema, context, "devriye.db")
    }
}