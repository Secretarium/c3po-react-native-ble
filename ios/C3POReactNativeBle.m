#import <Foundation/Foundation.h>
#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>

@interface RCT_EXTERN_MODULE(C3POReactNativeBle, RCTEventEmitter)

    RCT_EXTERN_METHOD(
        supportedEvents
    )

    RCT_EXTERN_METHOD(
        setManufacturerId: (nonnull NSNumber *)manufacturerId
    )

    RCT_EXTERN_METHOD(
        broadcast: (NSString *)uuid
        payload:(NSArray *)payload
        resolve:(RCTPromiseResolveBlock)resolve
        rejecter:(RCTPromiseRejectBlock)reject
    )

    RCT_EXTERN_METHOD(
        stopBroadcast
        resolve:(RCTPromiseResolveBlock)resolve
        rejecter:(RCTPromiseRejectBlock)reject
    )

    RCT_EXTERN_METHOD(
        scan
        resolve:(RCTPromiseResolveBlock)resolve
        rejecter:(RCTPromiseRejectBlock)reject
    )

    RCT_EXTERN_METHOD(
        stopScan
        resolve:(RCTPromiseResolveBlock)resolve
        rejecter:(RCTPromiseRejectBlock)reject
    )

@end
    
