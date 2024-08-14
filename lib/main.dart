import 'package:flutter/material.dart';
import 'package:sensors_plus/sensors_plus.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: GravityScreen(),
    );
  }
}

class GravityScreen extends StatefulWidget {
  @override
  _GravityScreenState createState() => _GravityScreenState();
}

class _GravityScreenState extends State<GravityScreen> {
  GravityEvent? _gravityrEvent;

  @override
  void initState() {
    super.initState();
    _startListening();
  }

  void _startListening() {
    gravityEventStream().listen((GravityEvent event) {
      setState(() {
        _gravityrEvent = event;
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Gravity Example'),
      ),
      body: Center(
        child: _gravityrEvent == null
            ? Text('Waiting for data...')
            : Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: <Widget>[
                  Text('X: ${_gravityrEvent!.x.toStringAsFixed(2)}'),
                  Text('Y: ${_gravityrEvent!.y.toStringAsFixed(2)}'),
                  Text('Z: ${_gravityrEvent!.z.toStringAsFixed(2)}'),
                ],
              ),
      ),
    );
  }
}
