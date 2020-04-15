package com.secretarium.libs.c3po.ble

import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseSettings
import com.facebook.react.bridge.Promise
import com.secretarium.libs.c3po.ble.C3POErrorTypes.*

class C3POAdvertiseCallback : AdvertiseCallback {

    private var promise: Promise? = null;

    constructor(promise: Promise?) : super() {
        this.promise = promise
    }

    override fun onStartFailure(errorCode: Int) {
        super.onStartFailure(errorCode)
        if (promise == null)
            return
        when (errorCode) {
            ADVERTISE_FAILED_FEATURE_UNSUPPORTED -> promise?.reject(EADNSUPPORTED.name, EADNSUPPORTED.meaning)
            ADVERTISE_FAILED_TOO_MANY_ADVERTISERS -> promise?.reject(EADNINSTANCE.name, EADNINSTANCE.meaning)
            ADVERTISE_FAILED_ALREADY_STARTED -> promise?.reject(EADARUNNING.name, EADARUNNING.meaning)
            ADVERTISE_FAILED_DATA_TOO_LARGE -> promise?.reject(EADDTOOLARGE.name, EADDTOOLARGE.meaning)
            ADVERTISE_FAILED_INTERNAL_ERROR -> promise?.reject(EADINTERROR.name, EADINTERROR.meaning)
        }
    }

    override fun onStartSuccess(settingsInEffect: AdvertiseSettings) {
        super.onStartSuccess(settingsInEffect)
        if (promise == null)
            return
        promise?.resolve(settingsInEffect.toString())
    }
}