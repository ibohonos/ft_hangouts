package ua.com.createsites.ft_hangouts

import ua.com.createsites.ft_hangouts.Adapter.ListUserAdapter
import ua.com.createsites.ft_hangouts.DBHelper.UserDBHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import ua.com.createsites.ft_hangouts.Models.User
import android.support.v7.app.AppCompatActivity
import android.content.Intent
import android.view.MenuItem
import android.os.Bundle
import android.view.Menu

class MainActivity : AppCompatActivity() {

	private lateinit var userDb: UserDBHelper
	private var listUsers: List<User> = ArrayList<User>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		setSupportActionBar(toolbar)

		addNew.setOnClickListener { newContact() }

		userDb = UserDBHelper(this)

		refreshData()
	}

	private fun refreshData() {
		listUsers = userDb.allUser

		val adapter = ListUserAdapter(this@MainActivity, listUsers)

		contacts.adapter = adapter

		contacts.setOnItemClickListener {
			_, _, position, _ -> viewContact(position)
		}
	}

	private fun viewContact(position: Int) {
		val win = Intent(this@MainActivity, ContactView::class.java)
		val user: User = listUsers[position]

		val tel: String = when {
			user.phone.count() == 9 -> "+380${user.phone}"
			user.phone.count() == 12 ->
				(when {
					user.phone.take(1) != "+" -> "+${user.phone}"
					else -> user.phone
				}).toString()
			else -> user.phone
		}

		win.putExtra("id", user.id)
		win.putExtra("name", user.name)
		win.putExtra("phone", tel)
		win.putExtra("avatar", user.avatar)
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
