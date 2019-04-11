package ua.com.createsites.ft_hangouts

import ua.com.createsites.ft_hangouts.Adapter.ListMessagesAdapter
import ua.com.createsites.ft_hangouts.DBHelper.SmsData
import kotlinx.android.synthetic.main.messages_view.*
import android.support.v7.app.AppCompatActivity
import android.telephony.SmsManager
import android.view.MenuItem
import android.widget.Toast
import android.os.Bundle
import android.net.Uri
import java.util.Date

class MessagesView: AppCompatActivity() {

	private var id: Int = 0
	private lateinit var name: String
	private lateinit var phone: String
	private var avatar: String? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.messages_view)
		supportActionBar!!.setDisplayHomeAsUpEnabled(true)

		id = intent.getIntExtra("id", 0)
		name = intent.getStringExtra("name")
		phone = intent.getStringExtra("phone")
		avatar = intent.getStringExtra("avatar")

		imageSend.setOnClickListener { sendMessage() }
		this.title = "Message to $name"

		setSmsMessages()

		println(id)
		println(name)
		println(phone)
		println(avatar)
	}

	private fun setSmsMessages() {
		val smsList = ArrayList<SmsData>()

		val cursor = contentResolver.query(Uri.parse("content://sms/"), null, "address LIKE '$phone'", null, null)

		while (cursor.moveToNext()) {
			val nameCur = cursor.getString(cursor.getColumnIndex("protocol"))
			val test1 = cursor.getString(cursor.getColumnIndex("read"))
			val test2 = cursor.getString(cursor.getColumnIndex("status"))
			val type = cursor.getString(cursor.getColumnIndex("type"))
			val test5 = cursor.getString(cursor.getColumnIndex("subject"))
			val test6 = cursor.getString(cursor.getColumnIndex("service_center"))
			val test7 = cursor.getString(cursor.getColumnIndex("locked"))
			val test8 = cursor.getString(cursor.getColumnIndex("sub_id"))
			val test9 = cursor.getString(cursor.getColumnIndex("error_code"))
			val test10 = cursor.getString(cursor.getColumnIndex("creator"))
			val seen = cursor.getString(cursor.getColumnIndex("seen"))
			val phoneCur = cursor.getString(cursor.getColumnIndex("address"))
			val message = cursor.getString(cursor.getColumnIndex("body"))
			val date = cursor.getString(cursor.getColumnIndex("date"))

			smsList.add(SmsData(name, phone, message, Date(date.toLong()).toString()))
		}
		cursor.close()


//		val adapter = ListMessagesAdapter(this@MessagesView, smsList)
//		recycler_view_messages.adapter = adapter
	}

	private fun setMessageRootGravity() {

	}

	private fun sendMessage() {
		val mess = message_text.text
		val smsMan = SmsManager.getDefault()

		smsMan.sendTextMessage(phone, null, mess.toString(), null, null)
		Toast.makeText(this@MessagesView, "SMS Sended!", Toast.LENGTH_LONG).show()
		mess.clear()
	}

	override fun onOptionsItemSelected(item: MenuItem?): Boolean {
		return if (item?.itemId == android.R.id.home) {
			finish()
			true
		} else {
			super.onOptionsItemSelected(item)
		}
	}
}