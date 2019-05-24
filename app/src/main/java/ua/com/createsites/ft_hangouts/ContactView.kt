package ua.com.createsites.ft_hangouts

import kotlinx.android.synthetic.main.activity_contact_view.*
import ua.com.createsites.ft_hangouts.DBHelper.UserDBHelper
import ua.com.createsites.ft_hangouts.Models.User
import android.support.v7.app.AppCompatActivity
import android.content.pm.PackageManager
import android.annotation.SuppressLint
import android.content.Intent
import android.view.MenuItem
import android.widget.Toast
import android.os.Bundle
import android.os.Build
import android.Manifest
import android.net.Uri

class ContactView : BaseActivity() {

	companion object {
		private const val CALL_PHONE_CODE = 1001
		private const val SMS_CODE = 1002
	}

	private val perm = arrayOf(
			Manifest.permission.SEND_SMS,
			Manifest.permission.READ_SMS,
			Manifest.permission.RECEIVE_SMS
	)

	private var id: Int = 0
	private lateinit var name: String
	private lateinit var phone: String
	private var avatar: String? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_contact_view)
		supportActionBar!!.setDisplayHomeAsUpEnabled(true)

		id = intent.getIntExtra("id", 0)
		name = intent.getStringExtra("name")
		phone = intent.getStringExtra("phone")
		avatar = intent.getStringExtra("avatar")
		this.title = name

		viewData()

		callContact.setOnClickListener { accessCallPermissions() }
		smsContact.setOnClickListener { accessSMSPermissions() }
		editContact.setOnClickListener { editContact() }
		deleteContact.setOnClickListener { deleteContact() }
	}

	override fun onOptionsItemSelected(item: MenuItem?): Boolean {
		return if (item?.itemId == android.R.id.home) {
			finish()
			true
		} else {
			super.onOptionsItemSelected(item)
		}
	}

	override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
		when (requestCode) {
			CALL_PHONE_CODE -> {
				if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					makeCall()
				} else {
					Toast.makeText(this, getString(R.string.perm_den), Toast.LENGTH_SHORT).show()
				}
			}

			SMS_CODE -> {
				if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					sendSMS()
				} else {
					Toast.makeText(this, getString(R.string.perm_den), Toast.LENGTH_SHORT).show()
				}
			}
		}
	}

	@SuppressLint("MissingPermission")
	private fun makeCall() {
		val call = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phone"))

		startActivity(call)
	}

	private fun sendSMS() {
		val message = Intent(this@ContactView, MessagesView::class.java)

		message.putExtra("name", name)
		message.putExtra("phone", phone)
		startActivity(message)
	}

	private fun editContact() {
		val win = Intent(this@ContactView, UpdateContact::class.java)

		win.putExtra("id", id)
		win.putExtra("name", name)
		win.putExtra("phone", phone)
		win.putExtra("avatar", avatar)
		startActivity(win)
	}

	private fun deleteContact() {
		val user = User(id, name, phone, avatar)
		val db = UserDBHelper(this)
		db.deleteUser(user)
		Toast.makeText(this, getString(R.string.user_deleted), Toast.LENGTH_SHORT).show()
		finish()
	}

	private fun updateData() {
		val userDb = UserDBHelper(this)
		val user = userDb.findUser(id)

		name = user!!.name
		phone = user.phone
		avatar = user.avatar
		viewData()
	}

	private fun viewData() {
		nameView.text = name
		phoneView.text = phone

		if (avatar != null && avatar != "null") {
			imageView.setImageURI(Uri.parse(avatar))
		} else {
			imageView.setImageResource(R.drawable.ic_perm_identity_white_150dp)
		}
	}

	private fun accessCallPermissions() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED) {
				val permissions = arrayOf(Manifest.permission.CALL_PHONE)

				requestPermissions(permissions, CALL_PHONE_CODE)
			} else {
				makeCall()
			}
		} else {
			makeCall()
		}
	}

	private fun hasPermissions(permissions: Array<String>): Boolean {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			for (permission in permissions) {
				if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
					return false
				}
			}
		}

		return true
	}

	private fun accessSMSPermissions() {
		if(!hasPermissions(perm)){
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				requestPermissions(perm, SMS_CODE)
			} else {
				sendSMS()
			}
		} else {
			sendSMS()
		}
	}

	override fun onResume() {
		updateData()
		super.onResume()
	}
}
