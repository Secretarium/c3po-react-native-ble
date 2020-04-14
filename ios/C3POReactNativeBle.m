#import "C3POReactNativeBle.h"

@implementation C3POReactNativeBle

RCT_EXPORT_MODULE()

RCT_REMAP_METHOD(getDeviceName,
                 findEventsWithResolver:(RCTPromiseResolveBlock)resolve
                 rejecter:(RCTPromiseRejectBlock)reject)
{
  UIDevice *deviceInfo = [UIDevice currentDevice];

  resolve(deviceInfo.name);
}

@end
