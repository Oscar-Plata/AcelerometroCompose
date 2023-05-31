package com.argent.acelerometrocompose

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import info.mqtt.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttException

private var iniciado: Boolean = false
lateinit var broker: MqttAndroidClient

@Composable
fun ConfigurarMqttScreen(onBack: () -> Unit){
    val context = LocalContext.current
    val top = remember {
        mutableStateOf("")
    }
    var ser = remember {
        mutableStateOf("test.mosquitto.org")
    }
    var puerto = remember {
        mutableStateOf("1883")
    }
    val placeholder = ""
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
            BasicTextField(
                value = ser.value,
                onValueChange = { newText ->
                    ser.value = newText
                },
                textStyle = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.DarkGray
                ),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 64.dp) // margin left and right
                            .fillMaxWidth()
                            .border(
                                width = 2.dp,
                                color = Color(0xFFAAE9E6),
                                shape = RoundedCornerShape(size = 16.dp)
                            )
                            .padding(horizontal = 16.dp, vertical = 12.dp), // inner padding
                    ) {
                        if (ser.value.isEmpty()) {
                            Text(
                                text = placeholder,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color.LightGray
                            )
                        }
                        innerTextField()
                    }
                }
            )
            BasicTextField(
                value = puerto.value,
                onValueChange = { newText ->
                    puerto.value = newText
                },
                textStyle = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.DarkGray
                ),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 64.dp) // margin left and right
                            .fillMaxWidth()
                            .border(
                                width = 2.dp,
                                color = Color(0xFFAAE9E6),
                                shape = RoundedCornerShape(size = 16.dp)
                            )
                            .padding(horizontal = 16.dp, vertical = 12.dp), // inner padding
                    ) {
                        if (puerto.value.isEmpty()) {
                            Text(
                                text = placeholder,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color.LightGray
                            )
                        }
                        innerTextField()
                    }
                }
            )
            Button(
                onClick = {
                    var url ="tcp://${ser.value}:${puerto.value}"
                    connectBroker(context, url)
                },
            ) {
                Text(text = "Conectar")
            }
        }
}

fun connectBroker(applicationContext : Context, url:String): Boolean {
    broker = MqttAndroidClient ( applicationContext,url,"CLIENTE" )
    var connect = false
    try {
        val token = broker.connect()
        token.actionCallback = object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken)                        {
                Log.i("Connection", "success ")
                Toast.makeText(applicationContext,"CONECCION OK", Toast.LENGTH_LONG).show()
                connect = true
                //connectionStatus = true
                // Give your callback on connection established here
            }
            override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                //connectionStatus = false

                Toast.makeText(applicationContext,"CONECCION NO", Toast.LENGTH_LONG).show()
                Log.i("Connection", "failure")
                // Give your callback on connection failure here
                exception.printStackTrace()
            }
        }
    } catch (e: MqttException) {
        Toast.makeText(applicationContext,e.toString(), Toast.LENGTH_LONG).show()
        e.printStackTrace()
    }
    return connect
}

private fun publishBroker(topic: String, msg: String, qos: Int, retained: Boolean) {
    try {
        val token = broker.publish(topic,msg.toByteArray(),qos,retained)
        token.actionCallback = object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken) {
                //Toast.makeText(applicationContext,"Publish OK",Toast.LENGTH_SHORT).show()
            }
            override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                //Toast.makeText(applicationContext,"Publish NO",Toast.LENGTH_SHORT).show()
                exception.printStackTrace()
            }
        }
    } catch (e: MqttException) {
        //Toast.makeText(LocalContext.current,e.toString(),Toast.LENGTH_LONG).show()
        e.printStackTrace()
    }
}

//FUNCION PARA DESCONECTAR EL BROKER, NO SE SI FUNCIONA
private fun disconnectBroker() {
    try {
        broker.disconnect()
    } catch (e: MqttException) {
        //Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show()
        e.printStackTrace()
    }
}

fun publicar(applicationContext: Context, top: String, xh: Float, yh: Float, zh: Float){
    if(!iniciado) {
        iniciado = true
        Toast.makeText(applicationContext,"HANDLER INICIADO", Toast.LENGTH_SHORT).show()
        publishBroker(top, "${String.format("%2.2f", xh)},${String.format("%2.2f", yh)},${String.format("%2.2f", zh)}",0, false)
    }else{
        Toast.makeText(applicationContext,"HANDLER DETENENIDO", Toast.LENGTH_SHORT).show()
        iniciado = false
        //detener handler
    }
}