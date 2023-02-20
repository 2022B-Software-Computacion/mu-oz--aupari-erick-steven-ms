package com.esmn.telegramclone

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ChatAdapter(
    private val context: Context,
    private val chats: List<Chat>) : RecyclerView.Adapter<ChatViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item_chat_rv, parent, false)

        return ChatViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chat = chats[position]
        holder.chatThumbnail.setImageResource(chat.thumbnail)
        holder.chatName.text = chat.name
        holder.chatMessage.text = chat.lastMessage

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ActiveChatActivity::class.java)
            intent.putExtra("chat", chat)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return chats.size
    }
}
