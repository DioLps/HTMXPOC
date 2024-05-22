package jp.co.rakuten.htmxpoc

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView

import io.ktor.server.application.*
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import org.json.JSONObject


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val todoService = TodoService()
        super.onCreate(savedInstanceState)
        try {
            Thread {
                // Perform network operation
                todoService.initMemoryDB()
            }.start()
        } catch (e: Exception) {
            println("Couldn't initialize the thread!")
        }

//        Create server
        embeddedServer(CIO, port = 9000) {
            install(WebSockets)
            // ...
            routing {
                webSocket() {
                    println("onConnect")
                    try {
                        val todos = todoService.list()

                        val frame: Frame.Text = incoming.receive() as Frame.Text
                        val msg = JSONObject(frame.readText())
                        val header = msg.getJSONObject("HEADERS")
                        val trigger: String = header.getString("HX-Trigger")

                        println(header.toString())
                        println("Trigger $trigger")

                        when (trigger) {
                            "load-todos" -> {
//                                val todos = todoService.list()
//                        TODO: Create renderTodos fn
                                println("inside: load-todos")
                                val loader =  Loader(applicationContext)
                                println(loader.load("list-item-todo"))
                                send(todos.toString())
                            }

                            "toggle-todo-request" -> {
//                        TODO: Create renderTodos fn
                                send(todos.toString())
                            }

                            "create-todo-request" -> {
//                        TODO: Create renderTodos fn
                                send(todos.toString())
                            }

                            "delete-todo-request" -> {
//                        TODO: Create renderTodos fn
                                send(todos.toString())
                            }

                            "create-todo-dialog-shell" -> {
//                        TODO: Create renderTodos fn
                                send(todos.toString())
                            }

                            else -> {
                                println("Received not authorized message - Trigger: $trigger")
                            }
                        }


                    } catch (e: ClosedReceiveChannelException) {
                        println("onClose ${closeReason.await()}")
                    } catch (e: Throwable) {
                        println("onError ${closeReason.await()}")
                        e.printStackTrace()
                    }
                }
            }
        }.start(wait = false)

//        ws://localhost:5000
//
//        Get Templates
//        init webView.
//        Init websocket

        setContent {
            WebViewInit(url = "file:///android_asset/index.html");
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Properly stop the WebSocket server when the activity is destroyed
    }
}


@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewInit(url: String) {
    AndroidView(
        factory = {
            WebView(it).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                )
                webViewClient = WebViewClient()
                settings.javaScriptEnabled = true
//                Note: some websites doesn't provide icons if they realize that you're loading from android emulator
//                settings.userAgentString = System.getProperty("http.agent")
                loadUrl(url)
            }
        },
        update = {
            it.loadUrl(url)
        }
    )
}
