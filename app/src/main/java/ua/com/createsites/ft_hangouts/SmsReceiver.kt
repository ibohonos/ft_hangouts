package ua.com.createsites.ft_hangouts

import android.content.BroadcastReceiver
import android.telephony.SmsMessage
import android.provider.Telephony
import android.content.Context
import android.content.Intent
import android.widget.Toast
import android.os.Build

class SmsReceiver: BroadcastReceiver() {
	override fun onReceive(context: Context, intent: Intent) {
		if (intent.action.equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
			val extras = intent.extras

			if (extras != null) {
				val sms = (extras.get("pdus") as Array<*>).get(0) as ByteArray

				val format = extras.getString("format")

				val smsMessage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
					SmsMessage.createFromPdu(sms, format)
				} else {
					SmsMessage.createFromPdu(sms)
				}

				val phoneNumber = smsMessage.originatingAddress
				val messageText = smsMessage.messageBody.toString()

				Toast.makeText(context, "$phoneNumber\n$messageText", Toast.LENGTH_SHORT).show()
			}
		}
	}
}