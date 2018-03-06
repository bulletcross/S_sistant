package com.enterprise.bulletcross.assistant

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import android.hardware.SensorEventListener
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import java.util.Random;
import com.enterprise.bulletcross.assistant.R.id.graph
import com.jjoe64.graphview.Viewport
import android.media.MediaPlayer





/**
 * Service class constantly seeks for importance detection
 * Once importance is detected, service changes state
 * and constanly seeks for vicinity detection.
 * Vicinity is detected until interacted.
 * State 0 -> importance detection state (default)
 * State 1 -> Vicinity detection state
 * State 2 -> No detection state
 * State 3 -> Notify state
 *
 * State Change Flow
 * State 0 -> [interaction] -> State 2
 * State 0 -> [no interaction + importance is true] -> State 1
 * State 1 -> [interaction] -> State 2
 * State 1 -> [no interaction + vicinity is true] -> State 3
 * State 2 -> [no interaction] -> State 0
 * State 3 -> [interaction] -> State 0
 */

class MainActivity : AppCompatActivity(),SensorEventListener {
    var rgb_sensor_reading:TextView? = null
    private var mSensorManager: SensorManager? = null
    private var mLight: Sensor? = null

    private var graph:GraphView? = null

    private var series: LineGraphSeries<DataPoint>? = null
    private var lastX = 0

    private var r = Random()
    var mp:MediaPlayer? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rgb_sensor_reading = findViewById(R.id.textView)
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mLight = mSensorManager!!.getDefaultSensor(Sensor.TYPE_LIGHT)
        graph = findViewById<View>(R.id.graph) as GraphView
        series = LineGraphSeries<DataPoint>()
        /*val series = LineGraphSeries<DataPoint>(arrayOf<DataPoint>(DataPoint(0.0, 1.0),
                DataPoint(1.0, 0.0),
                DataPoint(2.0, 0.0),
                DataPoint(3.0, 0.0),
                DataPoint(4.0, 0.0)))*/

        graph!!.addSeries(series)

        val viewport = graph!!.getViewport()
        viewport.isYAxisBoundsManual = true
        viewport.setMinY(0.0)
        viewport.setMaxY(20.0)
        viewport.isScrollable = true

        mp = MediaPlayer.create(this, R.raw.beep)
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Do something here if sensor accuracy changes.
    }

    override fun onSensorChanged(event: SensorEvent) {
        val sensor_data = event.values[0]
        rgb_sensor_reading?.setText(sensor_data.toString())
        if(sensor_data < 3){
            mp!!.start()
        }
        //lastX++
        //series!!.appendData(DataPoint(lastX.toDouble(), sensor_data.toDouble()), true, 20)
    }

    override fun onResume() {
        // Register a listener for the sensor.
        super.onResume()
        mSensorManager!!.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL)
        // we're going to simulate real time with thread that append data to the graph
        Thread(Runnable {
            // we add 100 new entries
            for (i in 0..99) {
                runOnUiThread {
                    lastX++
                    var randomValue = 0.0 + (20.0 - 0.0) * r.nextDouble()
                    series!!.appendData(DataPoint(lastX.toDouble(), randomValue), true, 20)
                }

                // sleep to slow down the add of entries
                try {
                    Thread.sleep(600)
                } catch (e: InterruptedException) {
                    // manage error ...
                }

            }
        }).start()
    }

    override fun onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onPause()
        mSensorManager!!.unregisterListener(this)
    }
}
