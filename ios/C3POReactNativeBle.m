#import <Foundation/Foundation.h>
#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>

@interface RCT_EXTERN_MODULE(C3POReactNativeBle, RCTEventEmitter)

    + (BOOL)requiresMainQueueSetup
    {
        return YES;
    }
    
    RCT_EXTERN_METHOD(
        supportedEvents:
    )

    RCT_EXTERN_METHOD(
        setManufacturerId: (nonnull NSNumber *)manufacturerId
    )

    RCT_EXTERN_METHOD(
        broadcast: (NSString *)uuid
        payload:(NSArray *)payload
        resolver:(RCTPromiseResolveBlock)resolve
        rejecter:(RCTPromiseRejectBlock)reject
    )

    RCT_EXTERN_METHOD(
        stopBroadcast:
        (RCTPromiseResolveBlock)resolve
        rejecter:(RCTPromiseRejectBlock)reject
    )

    RCT_EXTERN_METHOD(
        scan:
        (RCTPromiseResolveBlock)resolve
        rejecter:(RCTPromiseRejectBlock)reject
    )

    RCT_EXTERN_METHOD(
        stopScan:
        (RCTPromiseResolveBlock)resolve
        rejecter:(RCTPromiseRejectBlock)reject
    )

@end
    
