/// make this Model part of the main project
part of 'package:getter/getter.dart';

class Media {
  int id;
  String title;
  Uri path;
  DateTime addedTime;
  int? duration;
  String? artist;
  int size;
  String mimeType;
  Media({
    required this.id,
    required this.addedTime,
    this.artist,
    this.duration,
    required this.mimeType,
    required this.path,
    required this.size,
    required this.title,
  });

  factory Media.fromJson(Map<String, dynamic> json) => Media(
        id: int.parse(json["id"].toString()),

        /// convert [String] to [DateTime]
        addedTime: DateTime.parse(json["date"].toString()),
        mimeType: json["mime_type"].toString(),
        path: Uri.parse(json["path"].toString()),
        size: int.parse(json["size"].toString()),
        title: json["title"].toString(),
        artist: json["artist"] != null ? json['artist'].toString() : null,
        duration: json["duration"] != null
            ? int.parse(json["duration"].toString())
            : null,
      );
}
