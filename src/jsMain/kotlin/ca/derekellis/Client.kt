package ca.derekellis

import app.cash.sqldelight.driver.sqljs.asPromise
import app.cash.sqldelight.driver.sqljs.initAsyncSqlDriver
import ca.derekellis.db.MyDatabase
import kotlinx.html.div
import kotlinx.html.dom.append
import org.w3c.dom.Node
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.html.table
import kotlinx.html.td
import kotlinx.html.tr
import org.w3c.dom.Worker
import kotlin.js.Promise

lateinit var myDb: MyDatabase

fun main() {
    window.onload = {
        document.body?.sayHello()

        console.log("Hello!")
        initAsyncSqlDriver("/worker.sql-wasm.js").then { driver ->
            console.log("Initialized!!")
            MyDatabase.Schema.create(driver).asPromise().then { MyDatabase(driver) }
        }.then { database ->
            myDb = database
            val promises = arrayOf(
                database.nameQueries.insert("Derek").asPromise(),
                database.nameQueries.insert("Gustavo").asPromise(),
                database.nameQueries.insert("Geoffrey").asPromise(),
                database.nameQueries.insert("Grayson").asPromise()
            )
            Promise.all(promises)
        }.then {
            myDb.nameQueries.getAll().executeAsList().asPromise()
        }.then {
            document.body?.nameTable(it)
        }
    }
}

fun Node.nameTable(names: List<String>) {
    append {
        table {
            names.forEach {
                tr { td { +it } }
            }
        }
    }
}

fun Node.sayHello() {
    append {
        div {
            +"Hello from JS!!!"
        }
    }
}
