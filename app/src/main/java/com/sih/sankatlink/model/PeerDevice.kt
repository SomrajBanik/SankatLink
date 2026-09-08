package com.sih.sankatlink.model

/**
 * Discovered offline local peer (Wi-Fi Direct / BLE Mesh)
 */
data class PeerDevice(
    val id: String,
    val name: String,
    val transport: String, // "Wi-Fi Direct" or "BLE Mesh"
    val rssi: Int, // Signal strength in dBm (-30 to -90)
    val distanceEstimateMeters: Float,
    val isConnected: Boolean = false,
    val isEmergencyResponder: Boolean = false
)

