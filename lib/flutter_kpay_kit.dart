import 'dart:convert';
import 'dart:io';

import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:pretty_dio_logger/pretty_dio_logger.dart';

class FlutterKpayKit {
  static const MethodChannel _channel = MethodChannel('flutter_kpay_kit');
  static const EventChannel _eventChannel =
      EventChannel('flutter_kpay_kit/pay_status');
  static Stream<dynamic>? _streamPayStatus;
  static String prepay_id = "";

  // String COMPLETED = 1;
  // String FAIL = 2;
  // String CANCEL = 3;
  static Stream<dynamic> onPayStatus() {
    _streamPayStatus = _eventChannel.receiveBroadcastStream();
    return _streamPayStatus!;
  }

static Future<String> startPay({
    required String orderString,
    required String orderSign,
    required String signType,
    String? urlScheme,
  }) async {
    final String data = await _channel.invokeMethod('startPay', {
      'orderString': orderString,
      'orderSign': orderSign,
      'signType': signType,
      'url_scheme': urlScheme,
    });

    return data;
  }
  
}
