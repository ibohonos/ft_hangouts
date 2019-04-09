package ua.com.createsites.ft_hangouts

import ua.com.createsites.ft_hangouts.Adapter.ListUserAdapter
import ua.com.createsites.ft_hangouts.DBHelper.DBHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import ua.com.createsites.ft_hangouts.Models.User
import android.support.v7.app.AppCompatActivity
import android.content.Intent
import android.view.MenuItem
import android.os.Bundle
import android.view.Menu

class MainActivity : AppCompatActivity() {

	private lateinit var db: DBHelper
	private var listUsers: List<User> = ArrayList<User>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		setSupportActionBar(toolbar)

		addNew.setOnClickListener { newContact() }

		db = DBHelper(this)

		refreshData()
	}

	private fun refreshData() {
		listUsers = db.allUser

		val adapter = ListUserAdapter(this@MainActivity, listUsers)

		contacts.adapter = adapter

		contacts.setOnItemClickListener { _, _, position, _ ->
			viewContact(position)
		}
	}

	private fun viewContact(position: Int) {
		val win = Intent(this@MainActivity, ContactView::class.java)

		win.putExtra("position", position)
		startActivity(win)
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

	private fun newContact() {
		val newWin = Intent(this, CreateContact::class.java)

		startActivity(newWin)
	}

	override fun onResume() {
		super.onResume()
		refreshData()
	}
}
