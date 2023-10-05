package com.example.temi_beta.viewmodel

import RobotProtocol
import android.os.Build
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import java.io.BufferedReader
import java.io.FileReader
import java.text.DecimalFormat

data class DeviceSpec(val name: String, val value: String)
class ControlPanelViewModel(robotProtocol: RobotProtocol) : ViewModel() {
    private var _robotProtocol: RobotProtocol = robotProtocol
    var posX: Float = 0F
    var posY: Float = 0F
    var yaw: Float = 0F
    var tilt: Int = 0
    var detectionState: Boolean = false
    var location = mutableStateOf<List<String>>(_robotProtocol.getAllLocation())
    var trackingState: Boolean = false
    val deviceSpecs: List<DeviceSpec> = mutableListOf<DeviceSpec>().apply {
        add(DeviceSpec("Device", Build.DEVICE))
        add(DeviceSpec("Manufacturer", Build.MANUFACTURER))
        add(DeviceSpec("Model", Build.MODEL))
        add(DeviceSpec("Product", Build.PRODUCT))
        add(DeviceSpec("Android Version", Build.VERSION.RELEASE))
        add(DeviceSpec("Board", Build.BOARD))
        add(DeviceSpec("Hardware", Build.HARDWARE))
        add(DeviceSpec("Display", Build.DISPLAY))
        add(DeviceSpec("Brand", Build.BRAND))
        add(DeviceSpec("Bootloader", Build.BOOTLOADER))
        add(
            DeviceSpec(
                "Total RAM",
               getTotalRAM()
            )
        )
        addAll(getCPUInfo())
    }

    private fun getTotalRAM(): String {
        val totalRamBytes = Runtime.getRuntime().maxMemory()
        return formatSize(totalRamBytes)
    }

    private fun formatSize(size: Long): String {
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        var sizeValue = size.toDouble()
        var unitIndex = 0

        while (sizeValue > 1024 && unitIndex < units.size - 1) {
            sizeValue /= 1024
            unitIndex++
        }

        val decimalFormat = DecimalFormat("#,##0.#")
        return "${decimalFormat.format(sizeValue)} ${units[unitIndex]}"
    }

    private fun getCPUInfo(): List<DeviceSpec> {
        val cpuInfo = mutableListOf<DeviceSpec>()

        try {
            val reader = BufferedReader(FileReader("/proc/cpuinfo"))
            reader.useLines { lines ->
                for (line in lines) {
                    val parts = line.split(":")
                    if (parts.size == 2) {
                        val key = parts[0].trim()
                        val value = parts[1].trim()
                        if (key == "model name" || key == "vendor_id") {
                            cpuInfo.add(DeviceSpec(key, value))
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return cpuInfo
    }

    fun trackingSwitch() {
        trackingState = !trackingState
        _robotProtocol.setDetectionAndTracking(
            enableTrack = trackingState,
            enableDetect = detectionState
        )
    }

    fun detectionSwitch() {
        detectionState = !detectionState
        _robotProtocol.setDetectionAndTracking(
            enableTrack = trackingState,
            enableDetect = detectionState
        )
    }

    fun tiltBy(degree: Int, speed: Float) {
        _robotProtocol.tiltBy(degree, speed)
    }

    fun repose() {
        _robotProtocol.repose()
    }

    fun goToPostion(posX: Float, posY: Float, tilt: Int, yaw: Float) {
        _robotProtocol.goToPosition(posX, posY, tilt, yaw)

    }


    fun saveLocation(name: String) {
        _robotProtocol.saveLocation(name)
        location.value = getLocations()

    }

    fun deleteLocation(name: String) {
        _robotProtocol.deleteLocation(name)
        location.value = getLocations()
    }

    fun textToSpeech(text: String, isShowText: Boolean = false) {
        _robotProtocol.textToSpeech(text, isShowText)
    }

    fun getLocations(): List<String> {
        return _robotProtocol.getAllLocation()
    }

    fun goto(location: String) {
        _robotProtocol.goToLocation(location)
    }
}