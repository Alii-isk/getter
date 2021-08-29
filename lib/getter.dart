/// import convert package.
///
/// this package help us to decode json code.
import 'dart:convert';

/// import services package.
///
/// this package help us to write native code.
import 'package:flutter/services.dart';

/// Make mdels as part of project.
part 'models/types_enum.dart';
part 'models/media_model.dart';

class Getter {
  static const MethodChannel _channel = MethodChannel('getter');

  /// get files information according to [GetterType].
  ///
  /// And return it as [Future], [List] of [Media].
  static Future<List<Media>> get({required GetterType type}) async {
    final List<Media> _data = [];

    /// Conect with native code.
    ///
    /// pass [type] as parameter
    final _result = await _channel.invokeMethod('get', {"type": type.index});

    /// loop into list of result.
    for (final item in _result) {
      /// Receive data as [Map] 7.
      ///
      /// Convert every [Map] to [Media] Model.
      final Map<String, dynamic> _a =
          json.decode(item.toString()) as Map<String, dynamic>;

      /// Add every [_a] converted to the [_data] variable.
      _data.add(Media.fromJson(_a));
    }

    /// return all the data.
    return _data;
  }
}
