//
//  RNPaymentGateway.swift
//  RNPaymentGateway
//
//  Created by Senthil Kumar Selvaraj on 16/09/21.
//

import Foundation
import UIKit
import PaymentGatewaySwift

@objc(RNPaymentGateway)
class RNPaymentGateway: NSObject, RCTBridgeModule, PaymentGatewayDelegate{
  
  var resolveCallback: RCTPromiseResolveBlock?
  
  var rejectCallback: RCTPromiseRejectBlock?
  
  
  static func moduleName()->String{
    return "RNPaymentGateway"
  }
  
  static func requiresMainQueueSetup()->Bool{
    return true
  }
  
  @objc
  func open(_ url: String, params: NSDictionary, resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock){
    resolveCallback = resolve
    rejectCallback = reject
    let paymentParams = PaymentGatewayParams(json: params)
    DispatchQueue.main.async {
      if let controller = (UIApplication.shared.delegate as? AppDelegate)?.window?.rootViewController{
        PaymentGateway.open(controller: controller, url: url, params: paymentParams, delegate: self)
      }
    }
  }
  
  func didPaymentCompleted(_ controller: UIViewController, data: Any?) {
    DispatchQueue.main.async {
      if let data = data{
        self.resolveCallback?(data)
      }else{
        self.rejectCallback?("2000","Unkown error/Payment cancelled by manullay",NSError(domain: "Unkown error/Payment cancelled by manullay", code: 2000, userInfo: nil))
      }
      controller.dismiss(animated: true, completion: nil)
    }
  }
  
  func didPaymentCanceled(_ controller: UIViewController) {
    DispatchQueue.main.async{
      self.rejectCallback?("2000","Unkown error/Payment cancelled by manullay",NSError(domain: "Unkown error/Payment cancelled by manullay", code: 2000, userInfo: nil))
        controller.dismiss(animated: true, completion: nil)
      }
  }
  
}
