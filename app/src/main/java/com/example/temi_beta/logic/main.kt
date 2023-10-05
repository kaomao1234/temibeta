package com.example.temi_beta.logic

import com.beust.klaxon.JsonObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

fun main() {
    val serverAddress = "10.59.185.14" // Change this to the server's IP address
    val serverPort = 5000 // Change this to the server's port number

    try {
        // Create a socket connection to the server
        val socket = Socket(serverAddress, serverPort)

        // Input and output streams for reading/writing data to the server
        val inputStream = socket.getInputStream()
        val outputStream = socket.getOutputStream()

        // Wrap the input stream with a BufferedReader for reading data
        val reader = BufferedReader(InputStreamReader(inputStream))
        val out = PrintWriter(outputStream, true)
        val thread1 = Thread {
            // Wrap the output stream with a PrintWriter for writing data
            println("Do send")
            while (true) {

                // Send a message to the server
                print("Enter message: ")
                val message = readLine()
                if (message != null) {
                    if (message.lowercase() == "exit") {
                        socket.close()
                    }
                }
                val data = JsonObject()
                data["content"] = message
                data["type"] = "message"
                out.println(data.toJsonString(prettyPrint = true))
            }
        }
        val thread2 = Thread {
            println("Do receive")
            while (true) {
                val response = reader.readLine()
                println("Server response: $response")
            }
        }
        thread2.start()
//        thread1.start()


        // Read and print the response from the server


    } catch (e: Exception) {
        e.printStackTrace()
    }
}




