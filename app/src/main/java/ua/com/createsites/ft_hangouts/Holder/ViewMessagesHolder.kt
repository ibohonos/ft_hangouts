package ua.com.createsites.ft_hangouts.Holder

import kotlinx.android.synthetic.main.item_text_message.view.*
import android.support.v7.widget.RecyclerView
import android.view.View

class ViewMessagesHolder(view: View): RecyclerView.ViewHolder(view) {
	val message_root = view.message_root
	val message_text_wrap = view.message_text_wrap
	val message_text_time = view.message_text_time
}
