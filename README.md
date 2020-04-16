# @secretarium/react-native-ble

Secretarium BLE component for C3PO.
This component aims are providing Bluetooth LE advertising (peripheral) and scnanning (central) for react-native. It it coded in Kotlin for Android and Swift for iOS.

## Limitations

There are multiple mismatch and OS limitations to the component's functioning on both implementations. This is because Android doesn't offer cutomised complete local name advertising by default (https://android.googlesource.com/platform/packages/apps/Bluetooth/+/master/src/com/android/bluetooth/gatt/AdvertiseHelper.java#43) and iOS doesn't permit tranditional named advertising in background apps (https://developer.apple.com/documentation/corebluetooth/cbperipheralmanager/1393252-startadvertising).


## Installation

```sh
yarn install @secretarium/react-native-ble
```

### Setting up the Android Project

In `AndroidManifest.xml`, add Bluetooth permissions and update <uses-sdk/>:
```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android" package="com.secretarium.apps.c3po">
    <uses-permission android:name="android.permission.BLUETOOTH"/>
    <uses-permission android:name="android.permission.BLUETOOTH_ADMIN"/>
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
    ...
</manifest>

```

### Setting up the iOS Project

On your `Info.plist` file, include: 
```xml
<dict>
    ...
    <key>NSBluetoothAlwaysUsageDescription</key>
    <string>We uses Bluetooth to scan other beacons around.</string>
    <key>UIBackgroundModes</key>
    <array>
        <string>bluetooth-central</string>
        <string>bluetooth-peripheral</string>
        ...
    </array>
    <key>UIRequiredDeviceCapabilities</key>
    <array>
        <string>bluetooth-le</string>
        ...
    </array>
</dict>
```

## Usage

```js
import C3POReactNativeBle from "@secretarium/react-native-ble";

await C3POReactNativeBle.setManufacturerId(0xFFFF);
```

## License

MIT
