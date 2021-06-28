import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter_staggered_grid_view/flutter_staggered_grid_view.dart';
import 'package:image_picker/image_picker.dart';
import 'package:photo_view/photo_view.dart';

class Add_Image extends StatefulWidget {
  const Add_Image({Key? key}) : super(key: key);

  @override
  _Add_ImageState createState() => _Add_ImageState();
}

class _Add_ImageState extends State<Add_Image> {
  List _images = [];

  late File imagef;
  final picker = ImagePicker();

  chooseImage() async {
    final pickedfile = await picker.getImage(source: ImageSource.gallery);
    if (pickedfile != null) {
      setState(() {
        _images.add(File(pickedfile.path));
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.grey[800],
      appBar: AppBar(
          actions: <Widget>[
            IconButton(
              icon: Icon(
                Icons.camera_alt,
                color: Colors.white,
              ),
              onPressed: () {
                chooseImage();
              },
            )

            /*     FlatButton(
              textColor: Colors.white,
              onPressed: () {
                chooseImage();
              },
              child: Text("ADD"),
              shape: CircleBorder(side: BorderSide(color: Colors.transparent)),
            ),*/
          ],
          backgroundColor: Colors.pink[200],
          title: Text("Nadine Gallery",
              style: TextStyle(
                  fontSize: 15,
                  fontWeight: FontWeight.bold,
                  color: Colors.white))),
      body: Container(
        child: Padding(
          padding: const EdgeInsets.all(8.0),
          child: _images.length == 0
              ? Center(
                  child: Text("No Images been added",
                      style: TextStyle(color: Colors.white)))
              : StaggeredGridView.countBuilder(
                  //crossAxisCount: 4,
                  itemCount: _images.length,
                  itemBuilder: (BuildContext context, int index) => InkWell(
                    onTap: () {
                      Navigator.push(context, MaterialPageRoute(builder: (_) {
                        return DetailScreen(_images[index]);
                      }));
                    },
                    child: Card(
                      color: Colors.grey[700],
                      margin: EdgeInsets.all(0),
                      shape: RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(8)),
                      child: Container(
                        margin: EdgeInsets.all(8),
                        child: ClipRRect(
                            borderRadius: BorderRadius.circular(8),
                            child: Image.file(
                              _images[index],
                              fit: BoxFit.cover,
                            )),
                      ),
                    ),
                  ),

                  staggeredTileBuilder: (int index) => StaggeredTile.fit(1),

                  //   StaggeredTile.count(2, index.isEven ? 2 : 1),
                  //: StaggeredTile.count(1, 1),
                  mainAxisSpacing: 8,
                  crossAxisSpacing: 8,
                  crossAxisCount: 2,
                ),
        ),
      ),
    );
  }
}

class DetailScreen extends StatefulWidget {
  DetailScreen(this.image);

  final File image;

  @override
  _DetailScreenState createState() => _DetailScreenState();
}

class _DetailScreenState extends State<DetailScreen>
    with SingleTickerProviderStateMixin {
  late Animation<Offset> animation;
  late AnimationController animationController;
  var _opacity = 1.0;
  var _xOffset = 0.0;
  var _yOffset = 0.0;
  var _blurRadius = 0.0;
  var _spreadRadius = 0.0;

  @override
  void initState() {
    super.initState();

    animationController = AnimationController(
      vsync: this,
      duration: Duration(seconds: 2),
    );
    animation = Tween<Offset>(
      begin: Offset(0.0, 0.3),
      end: Offset(0.0, 0.0),
    ).animate(CurvedAnimation(
      parent: animationController,
      curve: Curves.fastLinearToSlowEaseIn,
    ));

    Future<void>.delayed(Duration(seconds: 0), () {
      animationController.forward();
    });
  }

  @override
  void dispose() {
    // TODO: implement dispose

    animationController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: GestureDetector(
        child: SlideTransition(
          position: animation,
          child: PhotoView(
            imageProvider: FileImage(widget.image),
            minScale: PhotoViewComputedScale.contained * 0.8,
            maxScale: PhotoViewComputedScale.covered * 2,
            enableRotation: true,
            backgroundDecoration: BoxDecoration(
              boxShadow: [
                BoxShadow(
                  color: Colors.grey.shade400,
                  offset: Offset(20.0, 10.0),
                  blurRadius: 20.0,
                  spreadRadius: 30.0,
                )
              ],
              borderRadius: BorderRadius.circular(12),
              gradient: LinearGradient(
                colors: [Colors.grey.shade800, Colors.grey.shade800],
                begin: Alignment.topLeft,
                end: Alignment.bottomRight,
              ),
            ),
          ),
        ),
        onTap: () {
          Navigator.pop(context);
        },
      ),
    );
  }
}
