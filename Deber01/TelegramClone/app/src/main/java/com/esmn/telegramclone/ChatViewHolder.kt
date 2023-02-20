package com.esmn.telegramclone

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val chatThumbnail: ImageView = itemView.findViewById(R.id.chat_thumbnail)
    val chatName: TextView = itemView.findViewById(R.id.chat_name)
    val chatMessage: TextView = itemView.findViewById(R.id.chat_message)

}
