package ua.com.createsites.ft_hangouts.Adapter

import kotlinx.android.synthetic.main.contact_list.view.*
import ua.com.createsites.ft_hangouts.Models.User
import ua.com.createsites.ft_hangouts.R
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.widget.BaseAdapter
import android.content.Context
import android.view.ViewGroup
import android.app.Activity
import android.view.View
import android.net.Uri

class ListUserAdapter(activity: Activity, private val listUser: List<User>): BaseAdapter() {

	private var inflater: LayoutInflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

	@SuppressLint("ViewHolder", "InflateParams")
	override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
		val rowView: View  = inflater.inflate(R.layout.contact_list, null)
		val avatarUrl = listUser[position].avatar

		val tel: String = when {
			listUser[position].phone.count() == 9 -> "+380${listUser[position].phone}"
			listUser[position].phone.count() == 12 ->
				(when {
					listUser[position].phone.take(1) != "+" -> "+${listUser[position].phone}"
					else -> listUser[position].phone
				}).toString()
			else -> listUser[position].phone
		}

		if (avatarUrl != null && avatarUrl != "null") {
			rowView.ava.setImageURI(Uri.parse(avatarUrl))
		} else {
			rowView.ava.setImageResource(R.drawable.ic_perm_identity_white_150dp)
		}

		rowView.name.text = listUser[position].name
		rowView.phone.text = tel

		return rowView
	}

	override fun getItem(position: Int): User {
		return listUser[position]
	}

	override fun getItemId(position: Int): Long {
		return listUser[position].id!!.toLong()
	}

	override fun getCount(): Int {
		return listUser.size
	}
}