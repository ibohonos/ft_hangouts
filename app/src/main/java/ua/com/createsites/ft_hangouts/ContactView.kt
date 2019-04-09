package ua.com.createsites.ft_hangouts

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import ua.com.createsites.ft_hangouts.DBHelper.DBHelper
import ua.com.createsites.ft_hangouts.Models.User
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_contact_view.*

class ContactView : AppCompatActivity() {

	companion object {
		private const val IMAGE_PICK_CODE = 1000
		private const val PERMISSION_CODE = 1001
	}

	private lateinit var db: DBHelper
	private var listUsers: List<User> = ArrayList<User>()
	private var position: Int = 0
	private var user: User? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_contact_view)
		supportActionBar!!.setDisplayHomeAsUpEnabled(true)

		db = DBHelper(this)
		listUsers = db.allUser
		position = intent.getIntExtra("position", 0)
		user = listUsers[position]

		viewData(user!!)

		callContact.setOnClickListener { accessCallPermissions() }
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
			PERMISSION_CODE -> {
				if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					if (permissions[0] == Manifest.permission.CALL_PHONE) {
						makeCall(user!!.phone)
					}
				} else {
					Toast.makeText(this, "Permissions denied", Toast.LENGTH_SHORT).show()
				}
			}
		}
	}

	private fun makeCall(phone: String) {
		val call = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phone"))

		startActivity(call)
	}

	private fun viewData(user: User) {
		nameView.text = user.name
		phoneView.text = user.phone

		if (user.avatar != "null") {
			imageView.setImageURI(Uri.parse(user.avatar))
		}
	}

	private fun accessCallPermissions() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED) {
				val permissions = arrayOf(Manifest.permission.CALL_PHONE)

				requestPermissions(permissions, PERMISSION_CODE)
			} else {
				makeCall(user!!.phone)
			}
		} else {
			makeCall(user!!.phone)
		}
	}
}
