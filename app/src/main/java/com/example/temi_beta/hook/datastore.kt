package com.example.temi_beta.hook

val data: MutableList<Any> =  mutableListOf()

class DataStore {
    private  val dataRef = data
    inline fun <reified T : Any> updateValue(newValue: T) {
        val existingValue = getValue<T>()
        if (existingValue != null) {
            val index = data.indexOf(existingValue)
            data[index] = newValue
        }
    }

    inline fun <reified T : Any> addValue(value: T) {
        data.add(value)
    }

    inline fun <reified T> getValue(): T? {
        return data.find { it is T } as? T
    }

    inline fun <reified T> removeValues() {
        data.removeAll { it is T }
    }
}