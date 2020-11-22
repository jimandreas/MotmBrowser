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

@file:Suppress("DEPRECATION", "unused")

package com.bammellab.mollib.common.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.*
import android.net.ConnectivityManager.NetworkCallback
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
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

It's LifeCycle conscious by implementing LifecycleObserver to avoid memory leakage
by doing some cleanup in onDestroy() method.
It supports from API 15 (Ice Cream Sandwich) through API 29 (Android Q)
For APIs prior to API 21, it uses a context-based BoradcastReceiver and NetworkInfo,
and uses ConnectivityManager.NetworkCallback for API 21 and above.
When both WiFi and cellular networks are on, then the connectivity
listener won't interrupt when the WiFi is disconnected while transitioning to the cellular network.
When the cellular network is on, then the connectivity listener won't interrupt
when the WiFi is connected and being the active network (as this is the preferred network).
If you're going to use the library, then no need to include this permission
android.permission.ACCESS_NETWORK_STATE; but you have to include it if you're going to use the utility class.
Capabilities

Get the current connectivity status (online / offline).
Continuous checking/listening to the internet connection and triggering a
callback when the device goes offline or online.
Get the type of the active internet connection (WiFi or Cellular).
Get the type of all available networks (WiFi or Cellular). >> Only supported on API 21+
Get the number of all available networks >> Only supported on API 21+
 */

class ConnectionUtil(private val context: Context) : LifecycleObserver {
    private var connectivityMgr: ConnectivityManager? = null
    private var networkStateReceiver: NetworkStateReceiver? = null

    private var isConnected = false
    private var connectionMonitor: ConnectionMonitor? = null

    // added in API 21 (Lollipop)
    // Deprecated in API 29
    // Checking internet connectivity
    interface ConnectionStateListener {
        fun onAvailable(isAvailable: Boolean)
    }

    init {
        connectivityMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager?
        (context as AppCompatActivity).lifecycle.addObserver(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            connectionMonitor = ConnectionMonitor()
            if (connectionMonitor != null) {
                val networkRequest = NetworkRequest.Builder()
                        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                        .build()
                connectivityMgr!!.registerNetworkCallback(networkRequest, connectionMonitor!!)
            }
        }
    }

    /**
     * Returns true if connected to the internet, and false otherwise
     *
     * NetworkInfo is deprecated in API 29
     * https://developer.android.com/reference/android/net/NetworkInfo
     *
     * getActiveNetworkInfo() is deprecated in API 29
     * https://developer.android.com/reference/android/net/ConnectivityManager#getActiveNetworkInfo()
     *
     * getNetworkInfo(int) is deprecated as of API 23
     * https://developer.android.com/reference/android/net/ConnectivityManager#getNetworkInfo(int)
     */
    fun isOnline(): Boolean {
        if (connectivityMgr == null) {
            Timber.e("ConnectionUtil: isOnline: connetivityMgr is null, quitting")
            return false
        }
        isConnected = false
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            // Checking internet connectivity

            val activeNetwork = connectivityMgr!!.activeNetworkInfo // Deprecated in API 29

            isConnected = activeNetwork != null
        } else {
            val allNetworks: Array<Network> = connectivityMgr!!.allNetworks // added in API 21 (Lollipop)
            for (network in allNetworks) {
                val networkCapabilities = connectivityMgr!!.getNetworkCapabilities(network)
                if (networkCapabilities != null) {
                    if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                            || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                            || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) isConnected = true
                }
            }
        }
        return isConnected
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
        val activeNetwork = connectivityMgr!!.activeNetworkInfo // Deprecated in API 29
        if (activeNetwork != null) if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val capabilities = connectivityMgr!!.getNetworkCapabilities(connectivityMgr!!.activeNetwork)
            if (capabilities != null) if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                // connected to mobile data
                return TRANSPORT_CELLULAR
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                // connected to wifi
                return TRANSPORT_WIFI
            }
        } else {
            if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE) { // Deprecated in API 28
                // connected to mobile data
                return TRANSPORT_CELLULAR
            } else if (activeNetwork.type == ConnectivityManager.TYPE_WIFI) { // Deprecated in API 28
                // connected to wifi
                return TRANSPORT_WIFI
            }
        }
        return NO_NETWORK_AVAILABLE
    }

    fun availableNetworksCount(): Int {
        var count = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val allNetworks: Array<Network> = connectivityMgr!!.allNetworks // added in API 21 (Lollipop)
            for (network in allNetworks) {
                val networkCapabilities = connectivityMgr!!.getNetworkCapabilities(network)
                if (networkCapabilities != null) if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                        || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) count++
            }
        }
        return count
    }

    fun availableNetworks(): List<Int> {
        val activeNetworks: MutableList<Int> = ArrayList()
        val allNetworks: Array<Network> // added in API 21 (Lollipop)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            allNetworks = connectivityMgr!!.allNetworks
            for (network in allNetworks) {
                val networkCapabilities = connectivityMgr!!.getNetworkCapabilities(network)
                if (networkCapabilities != null) {
                    if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) activeNetworks.add(TRANSPORT_WIFI)
                    if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) activeNetworks.add(TRANSPORT_CELLULAR)
                }
            }
        }
        return activeNetworks
    }

    fun onInternetStateListener(listener: ConnectionStateListener?) {
        connectionMonitor!!.setOnConnectionStateListener(listener)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        //Log.d(TAG, "onDestroy")
        (context as AppCompatActivity).lifecycle.removeObserver(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (connectionMonitor != null) connectivityMgr!!.unregisterNetworkCallback(connectionMonitor!!)
        } else {
            if (networkStateReceiver != null) context.unregisterReceiver(networkStateReceiver)
        }
    }

    inner class NetworkStateReceiver(private var listener: ConnectionStateListener) : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            if (intent.extras != null) {
                val activeNetworkInfo = connectivityMgr!!.activeNetworkInfo // deprecated in API 29

                /*
                 * activeNetworkInfo.getState() deprecated in API 28
                 * NetworkInfo.State.CONNECTED deprecated in API 29
                 * */if (!isConnected && activeNetworkInfo != null && activeNetworkInfo.state == NetworkInfo.State.CONNECTED) {
                    //Log.d(TAG, "onReceive: " + "Connected To: " + activeNetworkInfo.typeName)
                    isConnected = true
                    listener.onAvailable(true)
                } else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, java.lang.Boolean.FALSE)) {
                    if (!isOnline()) {
                        listener.onAvailable(false)
                        isConnected = false
                    }
                }
            }
        }
    }

    inner class ConnectionMonitor : NetworkCallback() {
        private var mConnectionStateListener: ConnectionStateListener? = null
        fun setOnConnectionStateListener(connectionStateListener: ConnectionStateListener?) {
            mConnectionStateListener = connectionStateListener
        }

        override fun onAvailable(network: Network) {
            if (isConnected) return
            //Log.d(TAG, "onAvailable: ")
            if (mConnectionStateListener != null) {
                mConnectionStateListener!!.onAvailable(true)
                isConnected = true
            }
        }

        override fun onLost(network: Network) {
            if (availableNetworksCount() == 0) {
                mConnectionStateListener!!.onAvailable(false)
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