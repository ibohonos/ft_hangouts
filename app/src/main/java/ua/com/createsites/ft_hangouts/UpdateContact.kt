package ua.com.createsites.ft_hangouts

import kotlinx.android.synthetic.main.activity_update_contact.*
import ua.com.createsites.ft_hangouts.DBHelper.UserDBHelper
import ua.com.createsites.ft_hangouts.Models.User
import android.support.v7.app.AppCompatActivity
import android.graphics.drawable.BitmapDrawable
import android.content.pm.PackageManager
import java.io.FileOutputStream
import android.graphics.Bitmap
import android.content.Intent
import android.os.Environment
import android.view.MenuItem
import android.app.Activity
import android.widget.Toast
import java.io.IOException
import android.os.Bundle
import android.Manifest
import android.net.Uri
import android.os.Build
import java.io.File


class UpdateContact : AppCompatActivity() {

	companion object {
		private const val IMAGE_PICK_CODE = 1000
		private const val PERMISSION_CODE = 1001
	}

	private lateinit var userDb: UserDBHelper

	private var id: Int = 0
	private lateinit var name: String
	private lateinit var phone: String
	private var avatar: String? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_update_contact)
		userDb = UserDBHelper(this)

		supportActionBar?.setDisplayHomeAsUpEnabled(true)

		id = intent.getIntExtra("id", 0)
		name = intent.getStringExtra("name")
		phone = intent.getStringExtra("phone")
		avatar = intent.getStringExtra("avatar")

		this.title = getString(R.string.update_contact) + " $name"

		if (avatar != null && avatar != "null") {
			userImage.setImageURI(Uri.parse(avatar))
		}
		nameInput.setText(name)
		phoneInput.setText(phone)
		userImage.setOnClickListener { accessReadPermissions() }
		saveContact.setOnClickListener { accessWritePermissions() }
		updateImage.setOnClickListener { editAvatar() }
		deleteImage.setOnClickListener { deleteAvatar() }
	}

	private fun accessReadPermissions() {
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

	private fun accessWritePermissions() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
				val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)

				requestPermissions(permissions, PERMISSION_CODE)
			} else {
				saveData()
			}
		} else {
			saveData()
		}
	}

	private fun imageFromGallery() {
		val intent = Intent(Intent.ACTION_PICK)
		intent.type = "image/*"
		startActivityForResult(intent, IMAGE_PICK_CODE)
	}

	private fun editAvatar() {
		val draw = userImage.drawable as? BitmapDrawable
		val image: String?

		if (draw != null) {
			image = saveImageToStorage(draw)
			avatar = image
			Toast.makeText(this, getString(R.string.avatar_updated), Toast.LENGTH_LONG).show()
		} else {
			Toast.makeText(this, getString(R.string.first_select_image), Toast.LENGTH_LONG).show()
		}
	}

	private fun deleteAvatar() {
		avatar = null
		userImage.setImageResource(R.drawable.ic_perm_identity_white_150dp)
		Toast.makeText(this, getString(R.string.avatar_deleted), Toast.LENGTH_LONG).show()
	}

	private fun saveData() {
		val user = User(
				id,
				nameInput.text.toString(),
				phoneInput.text.toString(),
				avatar.toString()
		)

		userDb.updateUser(user)
		finish()
	}

	private fun saveImageToStorage(draw: BitmapDrawable): String {
		val bitmap = draw.bitmap
		val outStream: FileOutputStream?
		val sdCard = Environment.getExternalStorageDirectory()
		val dir = File(sdCard.absolutePath + "/ft_hangouts/images")
		var outFile: File? = null
		val fileName = String.format("%d.jpg", System.currentTimeMillis())

		try {
			dir.mkdirs()
			outFile = File(dir, fileName)

			outStream = FileOutputStream(outFile)
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
			outStream.flush()
			outStream.close()
		} catch (e: IOException) {
			e.printStackTrace()
		}

		return outFile.toString()
	}

	override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
		when (requestCode) {
			PERMISSION_CODE -> {
				if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					if (permissions[0] == Manifest.permission.READ_EXTERNAL_STORAGE) {
						imageFromGallery()
					} else if (permissions[0] == Manifest.permission.WRITE_EXTERNAL_STORAGE) {
						saveData()
					}
				} else {
					Toast.makeText(this, getString(R.string.perm_den), Toast.LENGTH_SHORT).show()
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
