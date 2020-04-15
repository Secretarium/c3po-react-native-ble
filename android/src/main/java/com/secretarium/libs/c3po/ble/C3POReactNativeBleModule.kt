package com.secretarium.libs.c3po.ble

import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.ParcelUuid
import android.util.Log
import com.facebook.react.bridge.*
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter
import com.secretarium.libs.c3po.ble.C3POErrorTypes.*

class C3POReactNativeBleModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    companion object {
        const val displayName = "C3POReactNativeBle"
        private var scannerInstance: BluetoothLeScanner? = null
        private var scannerCallback: ScanCallback? = null
        private var advertiserInstance: BluetoothLeAdvertiser? = null
        private var advertiserCallback: AdvertiseCallback? = null
    }

    private var manufacturerId: Int = 0
    private var scannerSettings: ScanSettings? = null
    private var scannerShouldLive: Boolean = false
    private var advertiserSettings: AdvertiseSettings? = null
    private var advertiserData: AdvertiseData? = null
    private var advertiserShouldLive: Boolean = false
    private val bluetoothAdapterPointer: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private var bluetoothAdapterReady: Boolean = false
    private val bluetoothReceiver: BroadcastReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == BluetoothAdapter.ACTION_STATE_CHANGED) {
                
                val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
                when (state) {
                    BluetoothAdapter.STATE_OFF -> bluetoothAdapterReady = false
                    BluetoothAdapter.STATE_TURNING_OFF -> {
                        bluetoothAdapterReady = false
                    }
                    BluetoothAdapter.STATE_ON -> {
                        bluetoothAdapterReady = true
                        startBroadcast()
                    }
                    BluetoothAdapter.STATE_TURNING_ON -> bluetoothAdapterReady = true
                }

                Arguments.createMap().let {
                    it.putInt("state", state)
                    sendEvent("onBluetoothStateChange", it)
                }
            }
        }
    }

    override fun getName(): String {
        return "C3POReactNativeBle"
    }

    private fun sendEvent(eventName: String, params: WritableMap?): Unit {
        reactApplicationContext
                .getJSModule(RCTDeviceEventEmitter::class.java)
                .emit(eventName, params)
    }

    private fun checkAdapter(promise: Promise): Boolean {
        if (bluetoothAdapterPointer == null) {
            Log.w(displayName, EBLNAVAILABLE.meaning)
            promise.reject(EBLNAVAILABLE.name, EBLNAVAILABLE.meaning)
            return false
        }
        if (!bluetoothAdapterReady) {
            Log.w(displayName, EBLDISABLED.meaning)
            promise.reject(EBLDISABLED.name, EBLDISABLED.meaning)
            return false
        }
        return true
    }

    @ReactMethod
    fun setManufacturerId(manufacturerId: Int) {
        this.manufacturerId = manufacturerId
    }

    @ReactMethod
    fun broadcast(uuid: String, payload: ReadableArray, promise: Promise) {
        if (manufacturerId == 0x0000) {
            Log.w(displayName, ECOMPINVALID.meaning)
            promise.reject(ECOMPINVALID.name, ECOMPINVALID.meaning)
            return
        }
        try {
            val advertiserSettingsBuilder = AdvertiseSettings.Builder()
            advertiserSettingsBuilder.run {
                setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
                setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_ULTRA_LOW)
                setConnectable(true)
            }
            val advertiserDataBuilder = AdvertiseData.Builder()
            advertiserDataBuilder.run {
                setIncludeDeviceName(false)
                setIncludeTxPowerLevel(true)
                addManufacturerData(manufacturerId, payload.toArrayList().map {
                    (it as Number).toByte()
                }.toByteArray())
                addServiceUuid(ParcelUuid.fromString(uuid))
            }
            advertiserSettings = advertiserSettingsBuilder.build()
            advertiserData = advertiserDataBuilder.build()
            advertiserShouldLive = true
            startBroadcast()
            promise.resolve(true)
        } catch (e: Exception) {
            promise.reject(ECNTBROAD.name, ECNTBROAD.meaning, e)
        }
    }

    private fun startBroadcast() {

        if (!advertiserShouldLive)
            return

        if (advertiserCallback == null)
            advertiserCallback = C3POAdvertiseCallback(::sendEvent)

        if (advertiserInstance == null)
            advertiserInstance = bluetoothAdapterPointer?.bluetoothLeAdvertiser

        advertiserInstance?.startAdvertising(advertiserSettings, advertiserData, advertiserCallback);
    }

    @ReactMethod
    fun stopBroadcast(promise: Promise) {
        if (!checkAdapter(promise))
            return

        try {
            advertiserInstance?.stopAdvertising(advertiserCallback)
            promise.resolve(true)
        } catch (exception: Exception) {
            promise.reject(exception)
        }
    }

    @ReactMethod
    fun scan(promise: Promise) {
        try {
            val scannerSettingsBuilder = ScanSettings.Builder()
            scannerSettingsBuilder.run { setScanMode(ScanSettings.SCAN_MODE_LOW_POWER) }
            scannerSettings = scannerSettingsBuilder.build()
            scannerShouldLive = true
            startScan()
            promise.resolve(true)
        } catch (e: Exception) {
            promise.reject(ECNTSCAN.name, ECNTSCAN.meaning, e)
        }
    }

    private fun startScan() {

        if (!scannerShouldLive)
            return

        if (scannerCallback == null)
            scannerCallback = C3POScanCallback(::sendEvent)

        if (scannerInstance == null)
            scannerInstance = bluetoothAdapterPointer?.bluetoothLeScanner

        scannerInstance?.startScan(null, scannerSettings, scannerCallback)
    }

    @ReactMethod
    fun stopScan(promise: Promise) {
        scannerShouldLive = false
        scannerInstance?.stopScan(scannerCallback)
    }
}