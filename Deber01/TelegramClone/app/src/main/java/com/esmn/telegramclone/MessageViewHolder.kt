package com.esmn.telegramclone

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val messageText: TextView = itemView.findViewById(R.id.message_text)
    val messageTime: TextView = itemView.findViewById(R.id.message_time)
}

