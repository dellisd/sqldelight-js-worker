package ca.derekellis

import app.cash.sqldelight.async.db.AsyncSqlDriver
import ca.derekellis.db.MyDatabase

private const val versionPragma = "user_version"

internal suspend fun migrateIfNeeded(driver: AsyncSqlDriver) {
    val oldVersion =
        driver.executeQuery(null, "PRAGMA $versionPragma", mapper = { cursor ->
            if (cursor.next()) {
                cursor.getLong(0)?.toInt()
            } else {
                null
            }
        }, 0) ?: 0

    val newVersion = MyDatabase.Schema.version

    if (oldVersion == 0) {
        MyDatabase.Schema.create(driver)
        driver.execute(null, "PRAGMA $versionPragma=$newVersion", 0)
    } else if (oldVersion < newVersion) {
        MyDatabase.Schema.migrate(driver, oldVersion, newVersion)
        driver.execute(null, "PRAGMA $versionPragma=$newVersion", 0)
    }
}
