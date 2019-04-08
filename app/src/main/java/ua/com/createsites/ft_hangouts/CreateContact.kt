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
import android.graphics.Bitmap
import android.os.Environment.getExternalStorageDirectory
import android.provider.MediaStore.Images.Media.getBitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Environment
import ua.com.createsites.ft_hangouts.DBHelper.DBHelper
import ua.com.createsites.ft_hangouts.Models.User
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class CreateContact : AppCompatActivity() {

	private val IMAGE_PICK_CODE = 1000
	private val PERMISSION_CODE = 1001

	internal lateinit var db: DBHelper

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_create_contact)
		db = DBHelper(this)

		supportActionBar?.setDisplayHomeAsUpEnabled(true)

		userImage.setOnClickListener { accessReadPermissions() }
		saveContact.setOnClickListener { accessWritePermissions() }
//		saveContact.setOnClickListener { saveData() }
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

	private fun saveData() {
		val draw = userImage.getDrawable() as? BitmapDrawable
		var image: String? = null

		if (draw != null) {
			image = saveImageToStorage(draw)
		}

		println(image)
		val user = User(
				null,
				nameInput.text.toString(),
				phoneInput.text.toString(),
				image.toString()
		)

		db.addUser(user)

		Toast.makeText(this@CreateContact, "Click Me!", Toast.LENGTH_SHORT).show()
	}

	private fun saveImageToStorage(draw: BitmapDrawable): String {
		val bitmap = draw.bitmap
		val outStream: FileOutputStream?
		val sdCard = Environment.getExternalStorageDirectory()
		val dir = File(sdCard.getAbsolutePath() + "/ft_hangouts/images")
		var outFile: File? = null

		dir.mkdirs()

		val fileName = String.format("%d.jpg", System.currentTimeMillis())

		try {
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
