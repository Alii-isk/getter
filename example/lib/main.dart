import 'dart:io';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:getter/getter.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: HomeScreen(),
    );
  }
}

class HomeScreen extends StatefulWidget {
  const HomeScreen({Key? key}) : super(key: key);

  @override
  _HomeScreenState createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  Future<List<Media>> getImages() async {
    return await Getter.get(type: GetterType.photos);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: FutureBuilder<List<Media>>(
        future: getImages(),
        builder: (_, snapshot) {
          if (!snapshot.hasData) {
            return Center(
              child: const CircularProgressIndicator(),
            );
          }
          if (snapshot.data!.isEmpty) {
            return Text('Nothing to show');
          }
          return GridView.count(
            crossAxisCount: 2,
            children: List.generate(
              snapshot.data!.length,
              (i) => Image.file(
                File.fromUri(snapshot.data![i].path),
                fit: BoxFit.cover,
              ),
            ),
          );
        },
      ),
    );
  }
}
