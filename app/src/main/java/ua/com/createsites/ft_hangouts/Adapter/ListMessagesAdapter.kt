package ua.com.createsites.ft_hangouts.Adapter

import kotlinx.android.synthetic.main.item_text_message.view.*
import ua.com.createsites.ft_hangouts.DBHelper.SmsData
import ua.com.createsites.ft_hangouts.R
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.widget.BaseAdapter
import android.content.Context
import android.view.ViewGroup
import android.app.Activity
import android.view.View

class ListMessagesAdapter(activity: Activity, val smsList: ArrayList<SmsData>): BaseAdapter() {
	private var inflater: LayoutInflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

	@SuppressLint("ViewHolder", "InflateParams")
	override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
		val rowView: View  = inflater.inflate(R.layout.item_text_message, null)

		rowView.message_text_wrap.text = smsList[position].message
		rowView.message_text_time.text = smsList[position].date

		return rowView
	}

	override fun getItem(position: Int): SmsData{
		return smsList[position]
	}

	override fun getItemId(position: Int): Long {
		return position.toLong()
	}

	override fun getCount(): Int {
		return smsList.size
	}
}