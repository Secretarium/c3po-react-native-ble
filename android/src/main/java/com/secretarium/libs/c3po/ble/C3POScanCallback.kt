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

        if (result.scanRecord?.serviceUuids != null)
            for (uuid in result.scanRecord.serviceUuids)
                paramsUUID.pushString(uuid.toString())
        params.putArray("serviceUUIDs", paramsUUID)
        params.putInt("RSSI", result.rssi)

        if (result.scanRecord != null) {
            params.putInt("txPower", result.scanRecord.txPowerLevel)
            params.putString("deviceName", result.scanRecord.deviceName)
            params.putInt("advFlags", result.scanRecord.advertiseFlags)
        }

        if (result.device != null) {
            params.putString("deviceAddress", result.device.address)
        }

        sendEvent?.let { it("onDeviceFound", params) }
    }
}