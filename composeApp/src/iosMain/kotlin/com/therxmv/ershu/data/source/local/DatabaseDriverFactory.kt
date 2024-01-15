package com.therxmv.ershu.data.source.local

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.therxmv.ershu.db.ERSHUDatabase

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver = NativeSqliteDriver(
        schema = ERSHUDatabase.Schema,
        name = "ERSHUDatabase.db"
    )
}