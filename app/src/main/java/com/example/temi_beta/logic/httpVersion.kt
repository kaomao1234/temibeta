package com.example.temi_beta.logic

import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter

fun main() {
    // สร้างอินสแตนซ์ของ Socket.IO client
    val opts = IO.Options()

    val sio: Socket = IO.socket("http://10.59.185.14:5000")

    // การกำหนดเหตุการณ์เมื่อเชื่อมต่อสำเร็จ
    sio.on(Socket.EVENT_CONNECT, Emitter.Listener {
        println("Connected to server")
    })

    // การกำหนดเหตุการณ์เมื่อได้รับข้อมูลจากเซิร์ฟเวอร์
    sio.on("response") { args ->
        val data = args[0] as String
        println("Server response: $data")
    }

    // การกำหนดเหตุการณ์เมื่อตัดการเชื่อมต่อ
    sio.on(Socket.EVENT_DISCONNECT, Emitter.Listener {
        println("Disconnected from server")
    })

    // เชื่อมต่อกับเซิร์ฟเวอร์
    sio.connect()

    print("Enter a message (or 'exit' to quit): ")
//    val message = readLine()
//    val data = JsonObject()
//    data.put("message", message)
//    sio.emit("message", data)


    // ตัดการเชื่อมต่อเมื่อเสร็จสิ้น
    sio.disconnect()
}