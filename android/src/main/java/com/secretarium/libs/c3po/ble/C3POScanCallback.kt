package com.secretarium.libs.c3po.ble

import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.WritableMap

class C3POScanCallback : ScanCallback {

    private var sendEvent: ((name: String, params: WritableMap?) -> Unit)? = null;

    constructor(sendEvent: (name: String, params: WritableMap?) -> Unit) : super() {
        this.sendEvent = sendEvent
    }

    override fun onScanResult(callbackType: Int, result: ScanResult) {

        val params = Arguments.createMap()
        val paramsUUID = Arguments.createArray()

        for (uuid in result.scanRecord?.serviceUuids!!)
            paramsUUID.pushString(uuid.toString())

        params.putString("name", result.scanRecord?.deviceName ?: "")
        params.putString("address", result.device.address ?: "")
        params.putArray("services", paramsUUID)
        params.putInt("txPower", result?.scanRecord?.txPowerLevel ?: 0)
        params.putInt("rssi", result.rssi ?: 0)

        sendEvent?.let { it("onDeviceFound", params) }
    }
}