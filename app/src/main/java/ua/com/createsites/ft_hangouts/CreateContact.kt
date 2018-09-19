package ua.com.createsites.ft_hangouts

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_create_contact.*

class CreateContact : AppCompatActivity() {

	private val IMAGE_PICK_CODE = 1000
	private val PERMISSION_CODE = 1001

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_create_contact)

		supportActionBar?.setDisplayHomeAsUpEnabled(true)

		userImage.setOnClickListener { accessPermissions() }
	}

	private fun accessPermissions() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
				val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)

				requestPermissions(permissions, PERMISSION_CODE)
			} else {
				imageFromGallery()
			}
		} else {
			imageFromGallery()
		}
	}

	private fun imageFromGallery() {
		val intent = Intent(Intent.ACTION_PICK)
		intent.type = "image/*"
		startActivityForResult(intent, IMAGE_PICK_CODE)
	}

	override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
		when (requestCode) {
			PERMISSION_CODE -> {
				if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					imageFromGallery()
				} else {
					Toast.makeText(this, "Permissions denied", Toast.LENGTH_SHORT).show()
				}
			}
		}
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
			userImage.setImageURI(data?.data)
		}
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
