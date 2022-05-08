package ca.derekellis

import app.cash.sqldelight.driver.sqljs.worker.JsWorkerSqlDriver
import app.cash.sqldelight.async.coroutines.awaitAsList
import app.cash.sqldelight.async.coroutines.awaitCreate
import app.cash.sqldelight.driver.sqljs.worker.initAsyncSqlDriver
import ca.derekellis.db.MyDatabase
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.dom.append
import kotlinx.html.id
import kotlinx.html.input
import kotlinx.html.js.onClickFunction
import kotlinx.html.label
import kotlinx.html.table
import kotlinx.html.td
import kotlinx.html.tr
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.Node
import org.w3c.dom.Worker

fun main() {
    window.onload = {
        document.body?.sayHello()
        val scope = MainScope()
        scope.launch {
            //language=JavaScript
            val driver =
                JsWorkerSqlDriver(js("""new Worker(new URL("./worker.js", import.meta.url))""").unsafeCast<Worker>())

            migrateIfNeeded(driver)
            val database = MyDatabase(driver)

            val list = database.nameQueries.getAll().awaitAsList()
            document.body?.inputWidget(database, scope)
            document.body?.nameTable(list)
        }
    }
}

fun Node.nameTable(names: List<String>) {
    append {
        table {
            id = "results"
            names.forEach {
                tr { td { +it } }
            }
        }
    }
}

fun Node.sayHello() {
    append {
        div {
            +"Hello from JS!!! Refresh the page to see data persistence"
        }
    }
}

fun Node.inputWidget(database: MyDatabase, scope: CoroutineScope) {
    append {
        div {
            label {
                +"Enter Text:"
                htmlFor = "name-input"
            }
            input {
                id = "name-input"
            }
            button {
                +"Submit"
                this.onClickFunction = {
                    val input = document.getElementById("name-input") as HTMLInputElement
                    val name = input.value
                    scope.launch {
                        database.nameQueries.insert(name)
                        document.getElementById("results")?.append {
                            tr { td { +name } }
                        }
                    }
                }
            }
        }
    }
}
