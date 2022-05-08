package ca.derekellis

import app.cash.sqldelight.driver.sqljs.worker.initAsyncSqlDriver
import ca.derekellis.db.MyDatabase
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.html.div
import kotlinx.html.dom.append
import kotlinx.html.table
import kotlinx.html.td
import kotlinx.html.tr
import org.w3c.dom.Node


fun main() {
    window.onload = {
        document.body?.sayHello()

        console.log("Hello!")
        MainScope().launch {
            val driver = initAsyncSqlDriver("/worker.sql-wasm.js")

            console.log("Initialized!!")
            val database = MyDatabase.Schema.create(driver).run { MyDatabase(driver) }
            database.nameQueries.insert("Derek")
            database.nameQueries.insert("Gustavo")
            database.nameQueries.insert("Celeste")
            database.nameQueries.insert("Geoffrey")
            database.nameQueries.insert("Grayson")

            val list = database.nameQueries.getAll().executeAsList()
            document.body?.nameTable(list)
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
