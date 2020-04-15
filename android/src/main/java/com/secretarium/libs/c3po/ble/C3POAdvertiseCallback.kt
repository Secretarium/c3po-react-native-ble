package com.secretarium.libs.c3po.ble

import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseSettings
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.WritableMap
import com.secretarium.libs.c3po.ble.C3POErrorTypes.*

class C3POAdvertiseCallback : AdvertiseCallback {

    private var sendEvent: ((name: String, params: WritableMap?) -> Unit)? = null;

    constructor(sendEvent: (name: String, params: WritableMap?) -> Unit) : super() {
        this.sendEvent = sendEvent
    }

    private fun triggerErrorEvent(enum: C3POErrorTypes) {

        val params = Arguments.createMap()
        params.putBoolean("error", true)
        params.putString("name", enum.name)
        params.putString("meaning", enum.name)
        sendEvent?.let { it("onAdvertiserStartFailure", params) }
    }


    override fun onStartFailure(errorCode: Int) {
        super.onStartFailure(errorCode)
        when (errorCode) {
            ADVERTISE_FAILED_FEATURE_UNSUPPORTED ->triggerErrorEvent(EADNSUPPORTED)
            ADVERTISE_FAILED_TOO_MANY_ADVERTISERS -> triggerErrorEvent(EADNINSTANCE)
            ADVERTISE_FAILED_ALREADY_STARTED -> triggerErrorEvent(EADARUNNING)
            ADVERTISE_FAILED_DATA_TOO_LARGE -> triggerErrorEvent(EADDTOOLARGE)
            ADVERTISE_FAILED_INTERNAL_ERROR -> triggerErrorEvent(EADINTERROR)
        }
    }

    override fun onStartSuccess(settingsInEffect: AdvertiseSettings) {
        super.onStartSuccess(settingsInEffect)
        sendEvent?.let { it(settingsInEffect.toString(), null) }
    }
}