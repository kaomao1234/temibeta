package com.example.temi_beta.utils

import RobotProtocol
import android.util.Log
import com.example.temi_beta.hook.DataStore
import com.example.temi_beta.state.TemiSocketStatus
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter

class TemiSocketIO(ip:String,port: String,val robotProtocol: RobotProtocol) {
    var sio: Socket = IO.socket("http://$ip:$port")
    val dataStore = DataStore()
    val status = dataStore.getValue<TemiSocketStatus>()
    val isConnect = status?.isConnect
    val uri = status?.uri
    val table: Map<String, String> = mapOf(
        "1" to "table1",
        "2" to "table2",
        "3" to "table3",
        "4" to "table4"
    )
    init {
        uri?.value?.set("ip",ip)
        uri?.value?.set("port",port)
        eventInit()
    }
    fun eventInit() {
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
            if (dest == "home base") {
                robotProtocol.goToLocation("home base")
            } else {
                table[dest]?.let { robotProtocol.goToLocation(it) }
            }
        }
        sio.on("sender_location") {
            sio.emit("receiver_location", robotProtocol.getAllLocation())
        }
        sio.on("talker") { data ->
            val message = data[0] as String
            robotProtocol.textToSpeech(message, isShowText = true);
        }
    }

    fun emit(event: String, data: Any) {
        sio.emit(event, data)
    }

    fun connect() {
        isConnect?.value = true
        sio.connect()
    }

    fun disconnect() {
        isConnect?.value = false
        sio.disconnect()
    }

}