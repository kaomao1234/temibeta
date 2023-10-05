package com.example.temi_beta.utils

import RobotProtocol
import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter

class TemiSocketIO(ip: String, port: String, robotProtocol: RobotProtocol) {
    val sio: Socket = IO.socket("http://$ip:$port")
    val table: Map<String, String> = mapOf(
        "1" to "table1",
        "2" to "table2",
        "3" to "table3",
    )

    init {
        sio.on(Socket.EVENT_CONNECT, Emitter.Listener {
            Log.d("Event Socket", "Connected to Server")
        })
        sio.on("response", Emitter.Listener { args ->
            val data = args[0] as String
            Log.d("Event Socket", "Response := $data")
        })
        sio.on(Socket.EVENT_DISCONNECT, Emitter.Listener {
            Log.d("Event Socket", "Disconnected from server")
        })
        sio.on("receiver_goto_dest") { data ->
            val dest = data[0] as String
            table[dest]?.let { robotProtocol.goToLocation(it) }
        }
        sio.on("sender_location") {
            sio.emit("receiver_location", robotProtocol.getAllLocation())
        }
        sio.on("talker") { data ->
            val message = data[0] as String
            robotProtocol.textToSpeech(message, isShowText = true);
            sio.emit("move_to")

        }
    }

    fun emit(event: String, data: Any) {
        sio.emit(event, data)
    }

    fun connect() {
        sio.connect()
    }

    fun disconnect() {
        sio.disconnect()
    }

}