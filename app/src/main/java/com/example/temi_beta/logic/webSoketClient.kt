
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI

fun main() {
    runBlocking {
        launch { client() }
    }
}

suspend fun client() = withContext(Dispatchers.IO) {
    val uri = URI("ws://192.168.188.104:5000")
    val client = object : WebSocketClient(uri) {
        override fun onOpen(handshakedata: ServerHandshake) {
            val message = "Hello, Server!"
            send(message)
        }

        override fun onMessage(message: String) {
            println("Response from server: $message")
        }

        override fun onClose(code: Int, reason: String, remote: Boolean) {
            println("Connection closed")
        }

        override fun onError(ex: Exception) {
            ex.printStackTrace()
        }
    }

    client.connect()
    while (!client.isOpen) {
        // Wait for the connection to open
    }
}
