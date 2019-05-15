package ua.com.createsites.ft_hangouts

import ua.com.createsites.ft_hangouts.DBHelper.UserDBHelper
import ua.com.createsites.ft_hangouts.Models.User
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
				val userDB = UserDBHelper(context)
				val allUsers = userDB.allUser
				var count = 0
				var myUser: User? = null

				val tel2: String = when {
					phoneNumber.count() == 9 -> "+380$phoneNumber"
					phoneNumber.count() == 12 ->
						(when {
							phoneNumber.take(1) != "+" -> "+$phoneNumber"
							else -> phoneNumber
						}).toString()
					else -> phoneNumber
				}

				allUsers.forEach { user ->
					val tel: String = when {
						user.phone.count() == 9 -> "+380${user.phone}"
						user.phone.count() == 12 ->
							(when {
								user.phone.take(1) == "+" -> "+${user.phone}"
								else -> user.phone
							}).toString()
						else -> user.phone
					}

					if (tel == tel2) {
						count++
						myUser = user
					}
				}

				if (count == 0) {
					myUser = User(null, tel2, tel2, "null")
					userDB.addUser(myUser!!)
				}

				Toast.makeText(context, "${myUser!!.name}\n$messageText", Toast.LENGTH_SHORT).show()
			}
		}
	}
}