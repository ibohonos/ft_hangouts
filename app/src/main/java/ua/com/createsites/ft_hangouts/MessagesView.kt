package ua.com.createsites.ft_hangouts

import ua.com.createsites.ft_hangouts.Adapter.ListMessagesAdapter
import ua.com.createsites.ft_hangouts.Models.SmsData
import kotlinx.android.synthetic.main.messages_view.*
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.app.AppCompatActivity
import android.telephony.SmsManager
import android.view.MenuItem
import android.widget.Toast
import android.os.Bundle
import android.net.Uri
import android.preference.PreferenceManager
import java.util.Date

class MessagesView: BaseActivity() {

	private lateinit var name: String
	private lateinit var phone: String

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.messages_view)
		supportActionBar!!.setDisplayHomeAsUpEnabled(true)

		name = intent.getStringExtra("name")
		phone = intent.getStringExtra("phone")

		imageSend.setOnClickListener { sendMessage() }
		this.title = getString(R.string.mess_to) + " $name"

		setSmsMessages()
	}

	private fun setSmsMessages() {
		val smsList = ArrayList<SmsData>()

		val cursor = contentResolver.query(Uri.parse("content://sms/"), null, "address LIKE '$phone'", null, null)

		while (cursor.moveToNext()) {
			val message = cursor.getString(cursor.getColumnIndex("body"))
			val date = cursor.getLong(cursor.getColumnIndex("date"))
			val read = cursor.getInt(cursor.getColumnIndex("read"))
			val seen = cursor.getInt(cursor.getColumnIndex("seen"))
			val type = cursor.getInt(cursor.getColumnIndex("type"))

			smsList.add(SmsData(name, phone, message, Date(date).toLocaleString(), read, seen, type))
		}
		cursor.close()
		smsList.reverse()

		val adapter = ListMessagesAdapter(this@MessagesView, smsList)
		recycler_view_messages.layoutManager = LinearLayoutManager(this@MessagesView)
		recycler_view_messages.adapter = adapter
		recycler_view_messages.scrollToPosition(recycler_view_messages.adapter.itemCount - 1)
	}

	private fun sendMessage() {
		val mess = message_text.text
		val smsMan = SmsManager.getDefault()

		smsMan.sendTextMessage(phone, null, mess.toString(), null, null)
		Toast.makeText(this@MessagesView, getString(R.string.sms_sended), Toast.LENGTH_LONG).show()
		mess.clear()
		Thread.sleep(10)
		setSmsMessages()
	}

	override fun onOptionsItemSelected(item: MenuItem?): Boolean {
		return if (item?.itemId == android.R.id.home) {
			finish()
			true
		} else {
			super.onOptionsItemSelected(item)
		}
	}

	override fun onResume() {
		setSmsMessages()
		super.onResume()
	}
}