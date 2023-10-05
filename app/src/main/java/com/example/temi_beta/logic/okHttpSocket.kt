package com.example

import io.socket.client.IO
import io.socket.client.Socket

fun main(args: Array<String>) {

// ...

    val socket: Socket = IO.socket("http://localhost:5000")

// Set up event listeners and other configurations here
    socket.on(Socket.EVENT_CONNECT) {
        println("Connected to server")
    }

    socket.on("custom_event") { data ->
        println("Received custom event: $data")
    }

// Add more event listeners as needed

    socket.connect()
    socket.emit("my_message", "Hello from client!")
    socket.disconnect()


}