package ua.com.createsites.ft_hangouts

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_create_contact.*

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.contact_list.*
import kotlinx.android.synthetic.main.content_main.*
import ua.com.createsites.ft_hangouts.Adapter.ListUserAdapter
import ua.com.createsites.ft_hangouts.DBHelper.DBHelper
import ua.com.createsites.ft_hangouts.Models.User

class MainActivity : AppCompatActivity() {

	internal lateinit var db: DBHelper
	internal var listUsers: List<User> = ArrayList<User>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		setSupportActionBar(toolbar)

		addNew.setOnClickListener { view -> newContact(view) }

		db = DBHelper(this)

		refreshData()
	}

	private fun refreshData() {
		listUsers = db.allUser

		val adapter = ListUserAdapter(this@MainActivity, listUsers)

		println("list Users: " + listUsers.first().id.toString() + " " + listUsers.first().name  + " " + listUsers.first().phone + " " + listUsers.first().avatar)

		contacts.adapter = adapter

		contacts.setOnItemClickListener(){adapterView, view, position, id ->
			val itemId = adapterView.getItemIdAtPosition(position)
			Toast.makeText(this, "Click on position $position its item id $itemId", Toast.LENGTH_LONG).show()
		}
//		listUsers.adapter = adapter
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		// Inflate the menu; this adds items to the action bar if it is present.
		menuInflater.inflate(R.menu.menu_main, menu)
		return true
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		return when (item.itemId) {
			R.id.action_settings -> true
			else -> super.onOptionsItemSelected(item)
		}
	}

	fun newContact(view: View) {
		val newWin = Intent(this, CreateContact::class.java)

		startActivity(newWin)
	}
}
