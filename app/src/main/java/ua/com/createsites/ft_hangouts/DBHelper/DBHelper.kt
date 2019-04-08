package ua.com.createsites.ft_hangouts.DBHelper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import ua.com.createsites.ft_hangouts.Models.User

class DBHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VER) {
	companion object {
		private val DATABASE_VER = 1
		private val DATABASE_NAME = "ft_hangouts.db"

		private val TABLE_NAME = "User"
		private val COL_ID = "id"
		private val COL_NAME = "name"
		private val COL_PHONE = "phone"
		private val COL_AVATAR = "avatar"
	}

	override fun onCreate(db: SQLiteDatabase?) {
		val CREATE_TABLE_QUERY = ("CREATE TABLE `$TABLE_NAME` (" +
				"`$COL_ID` INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"`$COL_NAME` VARCHAR(255), " +
				"`$COL_PHONE` INTEGER, " +
				"`$COL_AVATAR` VARCHAR(255) NULL DEFAULT NULL);")

		db!!.execSQL(CREATE_TABLE_QUERY)
	}

	override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
		db!!.execSQL("DROP TABLE IF EXISTS `$TABLE_NAME`")

		onCreate(db)
	}

	val allUser: List<User>
		get() {
			val listUser = ArrayList<User>()
			val selectQuery = "SELECT * FROM `$TABLE_NAME`"
			val db = this.writableDatabase
			val cursor = db.rawQuery(selectQuery, null)

			if (cursor.moveToFirst()) {
				while (cursor.moveToNext()) {
					val id = cursor.getInt(cursor.getColumnIndex(COL_ID))
					val name = cursor.getString(cursor.getColumnIndex(COL_NAME))
					val phone = cursor.getString(cursor.getColumnIndex(COL_PHONE))
					val avatar = cursor.getString(cursor.getColumnIndex(COL_AVATAR))

					listUser.add(User(id, name, phone, avatar))
				}
			}
			db.close()

			return listUser
		}

	fun addUser(user: User)
	{
		val db = this.writableDatabase
		val values = ContentValues()

		values.put(COL_NAME, user.name)
		values.put(COL_PHONE, user.phone)
		values.put(COL_AVATAR, user.avatar)

		val test = db.insert(TABLE_NAME, null, values)
		Log.v("insertID", test.toString())
		db.close()
	}

	fun updateUser(user: User): Int
	{
		val db = this.writableDatabase
		val values = ContentValues()

		values.put(COL_NAME, user.name)
		values.put(COL_PHONE, user.phone)
		values.put(COL_AVATAR, user.avatar)

		return db.update(TABLE_NAME, values, "`$COL_ID` = ?", arrayOf(user.id.toString()))
	}

	fun deleteUser(user: User)
	{
		val db = this.writableDatabase

		db.delete(TABLE_NAME, "`$COL_ID` = ?", arrayOf(user.id.toString()))
		db.close()
	}
}