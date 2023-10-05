package com.example.temi_beta.hook

val dataStore = mutableMapOf<String, Any>()

class DataStorePreference {
    private val _dataStore = dataStore

    fun <T : Any> set(key: String, value: T) {
        dataStore[key] = value
    }

    fun <T : Any> setMultiple(vararg keyValuePairs: Pair<String, T>) {
        for ((key, value) in keyValuePairs) {
            dataStore[key] = value
        }
    }

    inline fun <reified T> getWithKey(key: String): T {
        val value =
            dataStore[key] ?: throw IllegalArgumentException("Key '$key' not found in DataStore.")

        if (value !is T) {
            throw ClassCastException("Value for key '$key' is not of expected type ${T::class.simpleName}.")
        }

        return value
    }

    fun getAll(): Map<String, Any> {
        return dataStore.toMap()
    }

    fun clear() {
        dataStore.clear()
    }

    fun removeWithKey(key: String) {
        dataStore.remove(key)
    }
}
