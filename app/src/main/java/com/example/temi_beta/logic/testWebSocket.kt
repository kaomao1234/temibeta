
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.http.HttpMethod
import io.ktor.websocket.send
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

fun runWebSocket() {
    val client = HttpClient {
        install(WebSockets)
    }
    runBlocking {
        var a = 0
        client.webSocket(method = HttpMethod.Get, host = "192.168.188.104", port = 5000, path = null) {

//                val othersMessage = incoming.receive() as? Frame.Text ?: continue
//                Log.e("WebSocketWatcher", "run: ${othersMessage.readText()}", )
                send("hand check $a")
                delay(1000)
                a+=1

        }
    }
    client.close()
    println("Connection closed. Goodbye!")
}