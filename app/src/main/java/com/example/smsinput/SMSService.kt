package com.example.smsinput

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.telephony.SmsMessage

data class SMSDetails(
    val message: String,
    val sender: String,

    )

class SMSReciever(
    callback: (message: SMSDetails) -> Unit = {}
) : BroadcastReceiver() {

    private val _callback = callback

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action.equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
            val bundle = intent.extras
            if (bundle != null) {
                val pdus = bundle.get("pdus") as Array<*>
                for (pdu in pdus) {
                    val format = bundle.getString("format")
                    val smsMessage = SmsMessage.createFromPdu(pdu as ByteArray, format)
                    val messageBody = smsMessage.messageBody
                    val sender = smsMessage.displayOriginatingAddress
                    val smsObj = SMSDetails(messageBody,sender)
                    _callback(smsObj)
                }
            }
        }
    }
}
