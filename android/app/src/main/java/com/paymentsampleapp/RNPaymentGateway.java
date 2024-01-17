package com.paymentsampleapp;

import android.text.TextUtils;
import android.util.Log;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableType;

import java.util.HashMap;




import com.facebook.react.bridge.ReadableMapKeySetIterator;


import org.json.JSONException;
import org.json.JSONObject;


import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;

import com.mobile.pgv1.PaymentGatewayInitializer;
import com.mobile.pgv1.PaymentParams;
import com.mobile.pgv1.ResponseCallbackListener;

public class RNPaymentGateway extends ReactContextBaseJavaModule {

    ReactContext reactContext;

    RNPaymentGateway(ReactApplicationContext context) {
        super(context);
        reactContext = context;
    }

    @Override
    public String getName() {
        return "RNPaymentGateway";
    }

    @ReactMethod
    public void open(String url, ReadableMap params, Promise promise) {
        try {
            JSONObject json =  toJsonObject(params);
            String jsonString = json.toString();
            PaymentParams pgPaymentParams = new PaymentParams(jsonString);
            PaymentGatewayInitializer paymentGateway = new PaymentGatewayInitializer(url, pgPaymentParams, reactContext.getCurrentActivity());
            paymentGateway.initiatePaymentProcess(new ResponseCallbackListener() {
                @Override
                public void onPaymentResponse(String paymentResponse) {
                    if (!TextUtils.isEmpty(paymentResponse)) {
                        try{
                            JSONObject responseJson = new JSONObject(paymentResponse);
                            if (responseJson.has("payment_response")){
                                promise.resolve(responseJson.getString("payment_response"));
                            }else {
                                promise.reject("2000", "Unkown Error/Payment cancelled by manullay", new Exception() );
                            }
                        }catch (Exception e){
                            promise.reject("2000", "Unkown Error/Payment cancelled by manullay", new Exception() );
                        }
                    }else{
                        promise.reject("2000", "Unkown Error/Payment cancelled by manullay", new Exception() );
                    }
                }
            });

        }catch(Exception e){
            promise.reject("2000", "Unkown Error/Payment cancelled by manullay", e);
        }

    }

    private JSONObject toJsonObject(ReadableMap readableMap) throws JSONException {
        JSONObject object = new JSONObject();
        ReadableMapKeySetIterator iter = readableMap.keySetIterator();
        while(iter.hasNextKey()) {
            String key = iter.nextKey();
            ReadableType type = readableMap.getType(key);
            switch(type) {
                case Boolean:
                    object.put(key, readableMap.getBoolean(key));
                    break;
                case Number:
                    object.put(key, readableMap.getDouble(key));
                    break;
                case String:
                    object.put(key, readableMap.getString(key));
                    break;
                case Map:
                    object.put(key, toJsonObject(readableMap.getMap(key)));
                    break;
                case Array:
                    break;
            }
        }
        return object;
    }


    public static HashMap<String, String> readableMapToHashMap(ReadableMap readableMap) {
        if (readableMap == null) {
            return null;
        }
        HashMap map = new HashMap<String, String>();
        ReadableMapKeySetIterator keySetIterator = readableMap.keySetIterator();
        while (keySetIterator.hasNextKey()) {
            String key = keySetIterator.nextKey();
            ReadableType type = readableMap.getType(key);
            switch(type) {
                case String:
                    map.put(key, readableMap.getString(key));
                    break;
                default:
                    break;
                    // do nothing
            }
        }
        return map;
    }

}
