package ua.com.createsites.ft_hangouts.Adapter

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.contact_list.view.*
import ua.com.createsites.ft_hangouts.Models.User
import ua.com.createsites.ft_hangouts.R
import kotlinx.android.synthetic.main.row_layout.view.*

class ListUserAdapter(internal var activity: Activity,
					  internal var listUser: List<User>): BaseAdapter() {

	internal var inflater: LayoutInflater

	init {
		inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
	}

	override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
		val rowView: View  = inflater.inflate(R.layout.contact_list, null)

		val avatarUrl = listUser[position].avatar

		rowView.ava.setImageURI(Uri.parse(avatarUrl.toString()))
		rowView.name.text = listUser[position].name.toString()
		rowView.phone.text = listUser[position].phone.toString()

		return rowView
	}

	override fun getItem(position: Int): Any {
		return listUser[position]
	}

	override fun getItemId(position: Int): Long {
		return listUser[position].id!!.toLong()
	}

	override fun getCount(): Int {
		return listUser.size
	}
}