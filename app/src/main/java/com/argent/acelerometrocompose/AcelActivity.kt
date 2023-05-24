package com.argent.acelerometrocompose

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.argent.acelerometrocompose.ui.theme.AcelerometroComposeTheme

class AcelActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AcelerometroComposeTheme(){
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    GreetingPreview2()
                }
            }
        }

    }
}

@Composable
fun Greeting2() {
    var gravity = remember {
        FloatArray(3)
    }
    var geomag = remember {
        FloatArray(3)
    }
    val yaw = remember {
        mutableStateOf(0.0)
    }
    val pitch = remember {
        mutableStateOf(0.0)
    }
    val roll = remember {
        mutableStateOf(0.0)
    }
    var register by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    val sm: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    var sensor: Sensor? by remember {
        mutableStateOf(null)
    }
    var sensor2: Sensor? by remember {
        mutableStateOf(null)
    }
    if(sensor == null && sensor2 == null){
        sensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensor2 = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        val accSensorEventListener = object : SensorEventListener {
            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
                return
            }

            // on below line we are creating a sensor on sensor changed
            override fun onSensorChanged(event: SensorEvent) {
                if (event.sensor?.type == Sensor.TYPE_ACCELEROMETER)
                    gravity = event.values!!

                if (event.sensor?.type == Sensor.TYPE_MAGNETIC_FIELD)
                    geomag = event.values!!

                if (gravity != null && geomag != null) {
                    val r = FloatArray(9)
                    val i = FloatArray(9)

                    if (SensorManager.getRotationMatrix(r, i, gravity, geomag)) {
                        val orientation = FloatArray(3)
                        SensorManager.getOrientation(r, orientation)
                        yaw.value = Math.toDegrees(orientation[0].toDouble())
                        pitch.value = Math.toDegrees(orientation[1].toDouble())
                        roll.value = Math.toDegrees(orientation[2].toDouble())
                    }
                }
            }
        }
        sm.registerListener(accSensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        sm.registerListener(accSensorEventListener, sensor2, SensorManager.SENSOR_DELAY_NORMAL)

    }

    Vista(yaw.value, pitch.value, roll.value)
}

@Composable
fun Vista(yaw: Double, pitch: Double, roll: Double) {
    Column(modifier = Modifier
        .fillMaxSize()
        .wrapContentSize(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally) {
        val yawString = String.format("%.2f", yaw)
        val pitchString = String.format("%.2f", pitch)
        val rollString = String.format("%.2f", roll)
        Text(text = yawString)
        Text(text = pitchString)
        Text(text = rollString)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    AcelerometroComposeTheme() {
        Greeting2()
    }
}