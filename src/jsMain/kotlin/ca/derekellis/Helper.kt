package ca.derekellis

import app.cash.sqldelight.async.coroutines.awaitCreate
import app.cash.sqldelight.async.coroutines.awaitMigrate
import app.cash.sqldelight.async.coroutines.awaitQuery
import app.cash.sqldelight.db.SqlDriver
import ca.derekellis.db.MyDatabase

private const val versionPragma = "user_version"

internal suspend fun migrateIfNeeded(driver: SqlDriver) {
    val oldVersion =
        driver.awaitQuery(null, "PRAGMA $versionPragma", mapper = { cursor ->
            if (cursor.next()) {
                cursor.getLong(0)?.toInt()
            } else {
                null
            }
        }, 0) ?: 0

    val newVersion = MyDatabase.Schema.version

    if (oldVersion == 0) {
        MyDatabase.Schema.awaitCreate(driver)
        driver.execute(null, "PRAGMA $versionPragma=$newVersion", 0)
    } else if (oldVersion < newVersion) {
        MyDatabase.Schema.awaitMigrate(driver, oldVersion, newVersion)
        driver.execute(null, "PRAGMA $versionPragma=$newVersion", 0)
    }
}
