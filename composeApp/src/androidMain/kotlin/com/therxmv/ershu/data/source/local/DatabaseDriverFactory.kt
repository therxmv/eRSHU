package com.therxmv.ershu.data.source.local

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.therxmv.ershu.AndroidApp
import com.therxmv.ershu.db.ERSHUDatabase

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver = AndroidSqliteDriver(
        schema = ERSHUDatabase.Schema,
        context = AndroidApp.INSTANCE.applicationContext,
        name = "ERSHUDatabase.db"
    )
}