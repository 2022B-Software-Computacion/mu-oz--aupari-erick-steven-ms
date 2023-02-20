package com.esmn.telegramclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ActiveChatActivity : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var chat: Chat
    private val activeMenuManager = ActiveMenuManager(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_activo)
        toggle = activeMenuManager.activeMenu()
        chat = intent.getParcelableExtra<Chat>("chat")!!

        // inicializar RV
        val recyclerView = findViewById<RecyclerView>(R.id.id_recycler_view_messages)
        if (chat != null) {
            initRecyclerView(chat!!.listMessages, recyclerView)
        }

        val sendButton = findViewById<ImageButton>(R.id.send_button)
        sendButton.setOnClickListener { sendMessage() }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun initRecyclerView(
        listMessage: ArrayList<Message>,
        recyclerView: RecyclerView
    ){
        val adapter = MessageAdapter(
            listMessage
        )
        recyclerView.adapter = adapter
        recyclerView.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        adapter.notifyDataSetChanged()
    }

    private fun sendMessage() {
        val messageInput = findViewById<EditText>(R.id.message_input)
        val messageText = messageInput.text.toString()
        if (messageText.isNotEmpty()) {
            val newMessage = Message(chat.name!!,messageText, SimpleDateFormat("HH:mm").format(Date()))
            chat.listMessages.add(newMessage)
            val recyclerView = findViewById<RecyclerView>(R.id.id_recycler_view_messages)
            val adapter = recyclerView.adapter as MessageAdapter
            adapter.notifyItemInserted(chat.listMessages.size - 1)
            messageInput.setText("")
        }
    }


}