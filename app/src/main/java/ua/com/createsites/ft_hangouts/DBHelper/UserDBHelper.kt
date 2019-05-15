package ua.com.createsites.ft_hangouts.DBHelper

import ua.com.createsites.ft_hangouts.Models.User
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase
import android.content.ContentValues
import android.content.Context

class UserDBHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VER) {
	companion object {
		private const val DATABASE_VER = 1
		private const val DATABASE_NAME = "ft_hangouts.db"

		private const val TABLE_NAME = "User"
		private const val COL_ID = "id"
		private const val COL_NAME = "name"
		private const val COL_PHONE = "phone"
		private const val COL_AVATAR = "avatar"
	}

	override fun onCreate(db: SQLiteDatabase?) {
		val createTableQuery = ("CREATE TABLE IF NOT EXISTS `$TABLE_NAME` (" +
				"`$COL_ID` INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"`$COL_NAME` VARCHAR(255), " +
				"`$COL_PHONE` VARCHAR(255), " +
				"`$COL_AVATAR` VARCHAR(255) NULL DEFAULT NULL);")

		db!!.execSQL(createTableQuery)
	}

	override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
		db!!.execSQL("DROP TABLE IF EXISTS `$TABLE_NAME`")

		onCreate(db)
	}

	val allUser: List<User>
		get() {
			val listUser = ArrayList<User>()
			val selectQuery = "SELECT * FROM `$TABLE_NAME` ORDER BY `$COL_NAME` ASC"
			val db = this.writableDatabase
			val cursor = db.rawQuery(selectQuery, null)

			while (cursor.moveToNext()) {
				val id = cursor.getInt(cursor.getColumnIndex(COL_ID))
				val name = cursor.getString(cursor.getColumnIndex(COL_NAME))
				val phone = cursor.getString(cursor.getColumnIndex(COL_PHONE))
				val avatar = cursor.getString(cursor.getColumnIndex(COL_AVATAR))

				listUser.add(User(id, name, phone, avatar))
			}
			cursor.close()
			db.close()

			return listUser
		}

	fun addUser(user: User): Long
	{
		val db = this.writableDatabase
		val values = ContentValues()

		values.put(COL_NAME, user.name)
		values.put(COL_PHONE, user.phone)
		values.put(COL_AVATAR, user.avatar)

		val insertID = db.insert(TABLE_NAME, null, values)
		db.close()

		return insertID
	}

	fun updateUser(user: User): Int
	{
		val db = this.writableDatabase
		val values = ContentValues()

		values.put(COL_NAME, user.name)
		values.put(COL_PHONE, user.phone)
		values.put(COL_AVATAR, user.avatar)

		val updateID = db.update(TABLE_NAME, values, "`$COL_ID` = ?", arrayOf(user.id.toString()))
		db.close()

		return updateID
	}

	fun deleteUser(user: User): Int
	{
		val db = this.writableDatabase

		val status = db.delete(TABLE_NAME, "`$COL_ID` = ?", arrayOf(user.id.toString()))
		db.close()

		return status
	}
}