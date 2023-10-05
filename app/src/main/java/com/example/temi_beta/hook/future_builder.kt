package com.example.temi_beta.hook

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember


/**
 * A class representing a snapshot of a future value.
 * @param value The value to be stored in the snapshot.
 * @param T The type of the value.
 */
class FutureSnapshot<T>(var value: T?) {

    /**
     * Checks whether the snapshot contains valid data.
     * @return true if the snapshot contains non-null data, false otherwise.
     */
    fun hasData(): Boolean {
        return value != null
    }

    /**
     * Retrieves the data stored in the snapshot.
     * @throws IllegalStateException if the value is null, indicating the absence of data.
     * @return The non-null data stored in the snapshot.
     */
    val data: T
        get() {
            return value ?: throw IllegalStateException("Value is null, cannot retrieve data.")
        }
}

/**
 * A composable function that asynchronously fetches a future value and provides a snapshot of its state to the UI.
 * @param future A suspending function representing the future value to be fetched.
 * @param content The UI composable lambda that takes a snapshot of the future value as a parameter.
 * Use this to define how the UI should react based on the snapshot.
 */

@Composable
fun <T : Any> FutureBuilder(
    future: suspend () -> T,
    content: @Composable (snapshot: FutureSnapshot<T>) -> Unit
) {
    // Create a state to hold the snapshot of the future value
    val snapshotState = remember { mutableStateOf(FutureSnapshot<T>(null)) }

    // Execute the provided suspending function to fetch the future value
    LaunchedEffect(true) {
        val result = future() // Fetch the future value
        snapshotState.value = FutureSnapshot(value = result) // Update the snapshot state
    }

    // Pass the snapshot to the content lambda to build the UI based on the snapshot state
    content(snapshotState.value)
}
