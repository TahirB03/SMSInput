package com.example.smsinput

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import com.example.smsinput.ui.theme.SMSInputTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SMSInputTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    App(this)
                }
            }
        }
    }
}

@Composable
fun App(
    context: Context
){
    var smsValue by remember { mutableStateOf("") }

    fun onSmsReceive(smsObject: SMSDetails){
        smsValue = smsObject.message
    }

    SMSListenerInput(
        context = context,
        value = smsValue,
        onSMSRecive = {onSmsReceive(it)},
        onValueChange = {smsValue=it}
    )
}
