package jp.co.rakuten.htmxpoc

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.coroutines.runBlocking

data class Todo(val userId: Int, val id: Int, val title: String, val completed: Boolean)

class TodoService() {
    private val todosUrl: String = "https://jsonplaceholder.typicode.com/todos"
    private val userId: Int = 1;

    private lateinit var todos: List<Todo>

    fun initMemoryDB() {
        try {
            println("Initializing DB...")
            syncFromAPI()
            println("Okay!")
        } catch (error: Exception) {
            println("Exception in initMemoryDB: $error");
        }
    }

    fun list(): List<Todo> {
        return todos
    }

    fun getById(id: String): Todo? {
        return todos.find { it.id == id.toInt() }
    }

    fun create(title: String): List<Todo> {
        val todo = Todo(userId, todos.size + 1, completed = false, title = title)
        todos.plus(todo)
        return todos
    }

    fun updateById(id: String, todo: Todo): List<Todo> {
        todos = todos.map {
            if (it.id == id.toInt()) {
                return@map todo
            }
            return@map it
        }

        return todos
    }

    fun deleteById(id: String): List<Todo> {
        return todos.filter { it.id != id.toInt() }
    }

    private fun syncFromAPI() = runBlocking {
        try {
            val gson = Gson()
            val listType = object : TypeToken<List<Todo>>() {}.type
            val resultString = get(todosUrl)
            val allTodos = gson.fromJson<List<Todo>>(resultString, listType)
            todos = allTodos.filter { it.userId == userId }
        } catch (error: Exception) {
            println("Exception in syncFromAPI: $error");
        }
    }

    private fun get(urlString: String): String {
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection
        try {
            connection.requestMethod = "GET"
            connection.connect()

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = connection.inputStream
                val reader = BufferedReader(InputStreamReader(inputStream))
                val response = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }
                return response.toString()
            } else {
                throw Exception("HTTP request failed with response code $responseCode")
            }
        } finally {
            connection.disconnect()
        }
    }

}