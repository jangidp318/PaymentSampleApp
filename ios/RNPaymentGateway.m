//
//  RNPaymentGateway.m
//  RNPaymentGateway
//
//  Created by Senthil Kumar Selvaraj on 16/09/21.
//

#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(RNPaymentGateway, NSObject)

RCT_EXTERN_METHOD(open:(NSString *)url params:(NSDictionary *)params resolve:(RCTPromiseResolveBlock *)resolve reject:(RCTPromiseRejectBlock *)reject)

@end
