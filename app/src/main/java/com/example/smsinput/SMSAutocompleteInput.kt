package com.example.smsinput

import android.Manifest
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.provider.Telephony
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.security.Permissions

@Composable
fun SMSListenerInput(
    context: Context,
    value: String,
    onValueChange: (newValue: String) -> Unit,
    onSMSRecive: (smsReceived: SMSDetails) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Enter the code.",
){


    fun checkIfSMSPermissionIsGranted(): Boolean{
        return context.checkSelfPermission(Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED
    }

    var isRunning = false
    val smsReciever = SMSReciever{
        onSMSRecive(it)
    }
    val filter = IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)
    var isSMSPermissionGranted by remember { mutableStateOf(checkIfSMSPermissionIsGranted()) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if(isGranted){
                isSMSPermissionGranted = true
            }
        }
    )


    fun onFocusInput(focus: FocusState) {
        if (focus.isFocused && !isRunning) {
            context.registerReceiver(smsReciever, filter)
            isRunning = true
        } else if (!focus.isFocused && isRunning) {
            context.unregisterReceiver(smsReciever)
            isRunning = false
        }
    }



    LaunchedEffect(Unit) {
        if(!isSMSPermissionGranted){
            permissionLauncher.launch(Manifest.permission.RECEIVE_SMS)
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            if (isRunning) {
                context.unregisterReceiver(smsReciever)
            }
        }
    }
    OutlinedTextField(
        value=value,
        label = { Text(label)},
        onValueChange = {onValueChange(it)},
        modifier = modifier.onFocusChanged{onFocusInput(it)}
    )
}
