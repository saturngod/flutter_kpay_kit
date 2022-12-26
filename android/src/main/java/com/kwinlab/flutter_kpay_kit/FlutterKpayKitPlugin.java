package com.kwinlab.flutter_kpay_kit;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.kbzbank.payment.KBZPay;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

import io.flutter.Log;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/** FlutterKpayKitPlugin */
public class FlutterKpayKitPlugin implements FlutterPlugin, MethodCallHandler , ActivityAware {
  /// The MethodChannel that will the communication between Flutter and native
  /// Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine
  /// and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;
  private static final String CHANNEL = "flutter_kpay_kit";
  private static EventChannel.EventSink sink;
  private static final String TAG = "kpay";
  private String orderString = "";
  private String orderSign = "";
  private String signType = "";
  

  private Activity activity;


  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), CHANNEL);
    channel.setMethodCallHandler(this);
    final EventChannel eventchannel = new EventChannel(flutterPluginBinding.getBinaryMessenger(),
            "flutter_kpay_kit/pay_status");
    eventchannel.setStreamHandler(new EventChannel.StreamHandler() {
      @Override
      public void onListen(Object o, EventChannel.EventSink eventSink) {
        SetSink(eventSink);
      }

      @Override
      public void onCancel(Object o) {

      }
    });

  }


  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
   if (call.method.equals("startPay")) {
      Log.d(TAG, "call");
      HashMap<String, Object> map = call.arguments();
      try {
        JSONObject params = new JSONObject(map);
        Log.v("startPay", params.toString());
         if (params.has("orderString") && params.has("orderSign") && params.has("signType")) {
          orderString = params.getString("orderString");
          orderSign = params.getString("orderSign");
          signType = params.getString("signType");
         
          startPay();
          result.success("payStatus " + 0);
        } else {
          result.error("parameter error", "parameter error", null);
        }
      } catch (JSONException e) {
        e.printStackTrace();
        return;
      }
    } else {
      result.notImplemented();
    }

  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }

  public static void SetSink(EventChannel.EventSink eventSink) {
    sink = eventSink;
    HashMap<String, Object> map = new HashMap();
    map.put("status", "10");
    map.put("orderId", "default123");
    sink.success(map);
  }

  public static void sendPayStatus(int status, String orderId) {
    HashMap<String, Object> map = new HashMap();
    map.put("status", status);
    map.put("orderId", orderId);
    sink.success(map);
  }

  private void startPay() {
    KBZPay.startPay(this.activity, orderString, orderSign, signType);
  }


  
  @Override
  public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
    this.activity = binding.getActivity();
  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {

  }

  @Override
  public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {

  }

  @Override
  public void onDetachedFromActivity() {

  }

  // private void showNotice(String msg) {
  // Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
  // }
}
