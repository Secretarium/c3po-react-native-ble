//
//  C3POReactNativeBle.swift
//  C3POReactNativeBle
//
//  Created by Florian Guitton on 15/04/2020.
//

import Foundation
import CoreBluetooth
import CoreLocation

@objc(C3POReactNativeBle)
open class C3POReactNativeBle: RCTEventEmitter, CBPeripheralManagerDelegate, CBCentralManagerDelegate {

    var manufacturerId: Int?;
    var peripheralUUID: CBUUID?;
    var peripheralManager: CBPeripheralManager?
    var peripheralReady: Bool = false
    var peripheralShouldLive: Bool = false
    var centralManager: CBCentralManager?
    var centralReady: Bool = false
    var centralShouldLive: Bool = false
    var bluetoothServices: CBMutableService?
    
    public override static func moduleName() -> String! {
        return "C3POReactNativeBle"
    }

    @objc
    open override func supportedEvents() -> [String] {
        return ["onDeviceFound"]
    }
    
    public func peripheralManagerDidUpdateState(_ peripheral: CBPeripheralManager) {
        if peripheral.state == .poweredOn {
            self.peripheralReady = true
            startBroadcast()
        } else {
            self.peripheralReady = false
        }
    }
    
    public func centralManagerDidUpdateState(_ central: CBCentralManager) {
        if central.state == .poweredOn {
            self.centralReady = true
            startScan()
        } else {
            self.centralReady = false
        }
    }

    public func centralManager(_ central: CBCentralManager, didDiscover peripheral: CBPeripheral, advertisementData: [String: Any], rssi RSSI: NSNumber) {
        self.sendEvent(withName: "onDeviceFound", body: [
            "name": peripheral.name ?? "",
            "identifier": peripheral.identifier.uuidString ?? "",
            "services": peripheral.services?.description ?? "",
            "txPower": advertisementData["kCBAdvDataTxPowerLevel"] ?? 0,
            "rssi": RSSI.intValue
        ])
    }

    func startBroadcast() {

        if (self.peripheralShouldLive == false) {
            return
        }

        if (self.peripheralManager == nil) {
            self.peripheralManager = CBPeripheralManager(delegate: self, queue: nil, options: nil)
        }
        
        if (self.peripheralReady == false) {
            return
        }

        self.peripheralManager?.removeAllServices()
        self.peripheralManager?.add(CBMutableService(type: self.peripheralUUID!, primary: true))
        self.peripheralManager?.startAdvertising([CBAdvertisementDataLocalNameKey : "C3PO", CBAdvertisementDataServiceUUIDsKey : [self.peripheralUUID!]])
    }

    @objc
    func setManufacturerId(_ manufacturerId: NSNumber) -> Void {
        self.manufacturerId = manufacturerId.intValue
    }

    @objc
    func broadcast(_ uuid: NSString, payload: NSArray, resolver resolve : RCTPromiseResolveBlock, rejecter reject: RCTPromiseRejectBlock) -> Void {
        self.peripheralUUID = CBUUID(string: uuid as String)
        self.peripheralShouldLive = true
        startBroadcast()        
        resolve(true)
    }

    @objc
    func stopBroadcast(_ resolve: RCTPromiseResolveBlock, rejecter reject: RCTPromiseRejectBlock) -> Void {
        self.peripheralShouldLive = false
        self.peripheralManager?.stopAdvertising()
        resolve(true)
    }

    @objc
    func scan(_ resolve: RCTPromiseResolveBlock, rejecter reject: RCTPromiseRejectBlock) -> Void {
        self.centralShouldLive = true
        startScan()
        resolve(true)
    }
    
    func startScan() {
        
        if (self.centralShouldLive == false) {
            return
        }

        if (self.centralManager == nil) {
            self.centralManager = CBCentralManager(delegate: self, queue: nil)
        }
        
        if (self.centralReady == false) {
            return
        }
        
            self.centralManager?.scanForPeripherals(withServices: nil)
    }

    @objc
    func stopScan(_ resolve: RCTPromiseResolveBlock, rejecter reject: RCTPromiseRejectBlock) -> Void {
        self.centralShouldLive = false
        self.centralManager?.stopScan()
        resolve(true)
    }
}
