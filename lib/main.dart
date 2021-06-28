import 'package:flutter/material.dart';
import 'package:gallery_app3/Add_Image.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      theme: ThemeData(primaryColor: Colors.black),
      title: "Flutter Staggered View",
      home: Add_Image(),
    );
  }
}
