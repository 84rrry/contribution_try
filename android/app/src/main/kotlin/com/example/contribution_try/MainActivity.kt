// package com.example.contribution_try

// import io.flutter.embedding.android.FlutterActivity

// class MainActivity: FlutterActivity()
package com.example.contribution_try

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.EventChannel

class MainActivity : FlutterActivity() {
    private val CHANNEL = "com.example.sensor_channel"
    private val SENSOR_CHANNEL = "com.example.sensor_channel/stream"
    private var sensorManager: SensorManager? = null
    private var gravitySensor: Sensor? = null
    private var eventSink: EventChannel.EventSink? = null

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler { call, result ->
            when (call.method) {
                "startGravitySensor" -> startGravitySensor()
                "stopGravitySensor" -> stopGravitySensor()
                else -> result.notImplemented()
            }
        }

        EventChannel(flutterEngine.dartExecutor.binaryMessenger, SENSOR_CHANNEL)
            .setStreamHandler(object : EventChannel.StreamHandler {
                override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
                    eventSink = events
                }

                override fun onCancel(arguments: Any?) {
                    eventSink = null
                }
            })
    }

    private fun startGravitySensor() {
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        gravitySensor = sensorManager?.getDefaultSensor(Sensor.TYPE_GRAVITY)
        gravitySensor?.also { sensor ->
            sensorManager?.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    private fun stopGravitySensor() {
        sensorManager?.unregisterListener(sensorEventListener)
    }

    private val sensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            if (event.sensor.type == Sensor.TYPE_GRAVITY) {
                val gravityValues = mapOf(
                    "x" to event.values[0],
                    "y" to event.values[1],
                    "z" to event.values[2]
                )
                eventSink?.success(gravityValues)
            }
        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }
}
