package ua.com.createsites.ft_hangouts.Adapter

import ua.com.createsites.ft_hangouts.Holder.ViewMessagesHolder
import ua.com.createsites.ft_hangouts.Models.SmsData
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import ua.com.createsites.ft_hangouts.R
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.view.ViewGroup
import android.graphics.Color
import android.app.Activity
import android.view.Gravity

class ListMessagesAdapter(val activity: Activity, val smsList: ArrayList<SmsData>): RecyclerView.Adapter<ViewMessagesHolder>() {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewMessagesHolder {
		return ViewMessagesHolder(LayoutInflater.from(activity).inflate(R.layout.item_text_message, parent, false))
	}

	override fun getItemCount(): Int {
		return smsList.size
	}

	override fun onBindViewHolder(holder: ViewMessagesHolder, position: Int) {
		holder.message_text_wrap.text = smsList[position].message
		holder.message_text_time.text = smsList[position].date

		if (smsList[position].type == 1) {
			val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.START)

			holder.message_root.layoutParams = params
			holder.message_text_wrap.setTextColor(Color.WHITE)
			holder.message_text_time.setTextColor(Color.WHITE)
			holder.message_root.background = ContextCompat.getDrawable(activity, R.drawable.rect_round_primary_color)
		} else {
			val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.END)

			holder.message_root.layoutParams = params
			holder.message_text_wrap.setTextColor(Color.BLACK)
			holder.message_text_time.setTextColor(Color.BLACK)
			holder.message_root.background = ContextCompat.getDrawable(activity, R.drawable.rect_round_white)
		}
	}
}
