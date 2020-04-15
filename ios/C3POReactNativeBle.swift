//
//  C3POReactNativeBle.swift
//  C3POReactNativeBle
//
//  Created by Florian Guitton on 15/04/2020.
//

import Foundation

@objc(C3POReactNativeBle)
class C3POReactNativeBle: NSObject {

    @objc
    func setManufacturerId(_ manufacturerId: Number) -> Void {
        resolve("You manufacturerId: \(manufacturerId)'")
    }

    @objc
    func broadcast(_ uuid: String, resolve: RCTPromiseResolveBlock, rejecter reject: RCTPromiseRejectBlock) -> Void {
        resolve("You sent: email '\(email)', password '\(password)'")
    }

    @objc
    func stopBroadcast(_ resolve: RCTPromiseResolveBlock, rejecter reject: RCTPromiseRejectBlock) -> Void {
        resolve()
    }

    @objc
    func scan(_ resolve: RCTPromiseResolveBlock, rejecter reject: RCTPromiseRejectBlock) -> Void {
        resolve("You sent: email '\(email)', password '\(password)'")
    }

    @objc
    func stopScan(_ resolve: RCTPromiseResolveBlock, rejecter reject: RCTPromiseRejectBlock) -> Void {
        resolve("You sent: email '\(email)', password '\(password)'")
    }

}