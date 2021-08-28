import 'dart:convert';

import 'package:flutter/services.dart';

part 'models/types_enum.dart';
part 'models/media_model.dart';

class Getter {
  static const MethodChannel _channel = MethodChannel('getter');


  /// get files information according to [GetterType]
  static Future<List<Media>> get({required GetterType type}) async {
    final List<Media> _data = [];
    final _result = await _channel.invokeMethod('get', {"type": type.index});
    for (final item in _result) {
      final Map<String, dynamic> _a =
          json.decode(item.toString()) as Map<String, dynamic>;
      _data.add(Media.fromJson(_a));
    }
    return _data;
  }


}
