/*
 *  Copyright 2020 Bammellab / James Andreas
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 */

package com.bammellab.mollib.common.util

import android.content.Context
import android.net.*
import android.net.ConnectivityManager.NetworkCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import timber.log.Timber


/**
 *
 * https://stackoverflow.com/a/54641263/3853712
 *

Update March 2020

As NetworkInfo is deprecated and as of API 29 from now we have to use
ConnectivityManager.NetworkCallback with its network state change
onAvailable() and onLost() callbacks.

Usage:

You can either use this library, or directly use the below utility class which is a part of this library.
Features

It's LifeCycle conscious by implementing DefaultLifecycleObserver to avoid memory leakage
by doing some cleanup in onDestroy() method.
It supports from API 23 (Marshmallow) and above.
Uses ConnectivityManager.NetworkCallback for network monitoring.
When both WiFi and cellular networks are on, then the connectivity
listener won't interrupt when the WiFi is disconnected while transitioning to the cellular network.
When the cellular network is on, then the connectivity listener won't interrupt
when the WiFi is connected and being the active network (as this is the preferred network).
Capabilities

Get the current connectivity status (online / offline).
Continuous checking/listening to the internet connection and triggering a
callback when the device goes offline or online.
Get the type of the active internet connection (WiFi or Cellular).
Get the type of all available networks (WiFi or Cellular).
Get the number of all available networks.
 */

class ConnectionUtil(private val context: Context) : DefaultLifecycleObserver {
    private var connectivityMgr: ConnectivityManager? = null

    private var isConnected = false
    private var connectionMonitor: ConnectionMonitor? = null

    // Checking internet connectivity
    interface ConnectionStateListener {
        fun onAvailable(isAvailable: Boolean)
    }

    init {
        connectivityMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager?
        (context as AppCompatActivity).lifecycle.addObserver(this)
        connectionMonitor = ConnectionMonitor()
        if (connectionMonitor != null) {
            val networkRequest = NetworkRequest.Builder()
                    .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                    .build()
            connectivityMgr!!.registerNetworkCallback(networkRequest, connectionMonitor!!)
        }
    }

    /**
     * Returns true if connected to the internet, and false otherwise.
     *
     * Uses activeNetwork + getNetworkCapabilities (available since API 21).
     */
    fun isOnline(): Boolean {
        if (connectivityMgr == null) {
            Timber.e("ConnectionUtil: isOnline: connetivityMgr is null, quitting")
            return false
        }
        val active = connectivityMgr!!.activeNetwork ?: return false
        val caps = connectivityMgr!!.getNetworkCapabilities(active) ?: return false
        return caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            || caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
            || caps.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
    }

    /**
     * Returns
     * NO_NETWORK_AVAILABLE >>> when you're offline
     * TRANSPORT_CELLULAR >> When Cellular is the active network
     * TRANSPORT_WIFI >> When Wi-Fi is the Active network
     *
     */
    fun activeNetwork(): Int {
        if (connectivityMgr == null) {
            Timber.e("ConnectionUtil: activeNetwork: connetivityMgr is null, quitting")
            return NO_NETWORK_AVAILABLE
        }
        val caps = connectivityMgr!!.getNetworkCapabilities(connectivityMgr!!.activeNetwork)
            ?: return NO_NETWORK_AVAILABLE
        return when {
            caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> TRANSPORT_CELLULAR
            caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> TRANSPORT_WIFI
            else -> NO_NETWORK_AVAILABLE
        }
    }

    fun availableNetworksCount(): Int = connectionMonitor?.getTrackedNetworks()?.size ?: 0

    fun availableNetworks(): List<Int> {
        val result = mutableListOf<Int>()
        connectionMonitor?.getTrackedNetworks()?.forEach { network ->
            val caps = connectivityMgr!!.getNetworkCapabilities(network) ?: return@forEach
            if (caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) result.add(TRANSPORT_WIFI)
            if (caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) result.add(TRANSPORT_CELLULAR)
        }
        return result
    }

    fun onInternetStateListener(listener: ConnectionStateListener?) {
        connectionMonitor!!.setOnConnectionStateListener(listener)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        (context as AppCompatActivity).lifecycle.removeObserver(this)
        if (connectionMonitor != null) connectivityMgr!!.unregisterNetworkCallback(connectionMonitor!!)
    }

    inner class ConnectionMonitor : NetworkCallback() {
        private var connectionStateListener1: ConnectionStateListener? = null
        private val trackedNetworks = mutableSetOf<Network>()

        fun getTrackedNetworks(): Set<Network> = trackedNetworks.toSet()

        fun setOnConnectionStateListener(connectionStateListener: ConnectionStateListener?) {
            connectionStateListener1 = connectionStateListener
        }

        override fun onAvailable(network: Network) {
            trackedNetworks.add(network)
            if (isConnected) return
            if (connectionStateListener1 != null) {
                connectionStateListener1!!.onAvailable(true)
                isConnected = true
            }
        }

        override fun onLost(network: Network) {
            trackedNetworks.remove(network)
            if (trackedNetworks.isEmpty()) {
                connectionStateListener1?.onAvailable(false)
                isConnected = false
            }
        }
    }

    companion object {
        /**
         * Indicates there is no available network.
         */
        private const val NO_NETWORK_AVAILABLE = -1

        /**
         * Indicates this network uses a Cellular transport.
         */
        const val TRANSPORT_CELLULAR = 0

        /**
         * Indicates this network uses a Wi-Fi transport.
         */
        const val TRANSPORT_WIFI = 1
    }
}
