package com.sih.bahubhashini.network

import com.sih.bahubhashini.model.AudioMessage
import com.sih.bahubhashini.model.PeerDevice

/**
 * Offline P2P mesh network transport interface.
 *
 * Planned implementation: Android Wi-Fi Direct (WifiP2pManager) + BLE Mesh.
 * - Wi-Fi Direct: ~250 Mbps, up to ~200m range
 * - BLE Mesh: ~1 Mbps, up to ~100m, multi-hop
 *
 * TODO: Implement WifiP2pManager peer discovery, group owner negotiation,
 *       and socket-based text payload transfer. Add BLE GATT server/client
 *       for fallback when Wi-Fi Direct is unavailable.
 *       Text payloads are < 150 bytes each — well within BLE MTU limits.
 */
interface MeshNetworkManager {
    /** Starts peer discovery over Wi-Fi Direct and BLE. */
    fun startDiscovery()

    /** Stops all discovery and closes connections. */
    fun stopDiscovery()

    /** Returns list of currently reachable peers. */
    fun getConnectedPeers(): List<PeerDevice>

    /**
     * Sends a translated text payload to all connected peers.
     * @param message The AudioMessage whose translated text will be transmitted.
     */
    suspend fun broadcast(message: AudioMessage): Boolean

    /**
     * Registers a callback to be invoked when a new message arrives from a peer.
     */
    fun onMessageReceived(callback: (AudioMessage) -> Unit)

    /** Releases all Wi-Fi Direct and BLE resources. */
    fun release()
}
