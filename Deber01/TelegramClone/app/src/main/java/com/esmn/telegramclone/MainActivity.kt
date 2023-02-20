package com.esmn.telegramclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle
    private val activeMenuManager = ActiveMenuManager(this)
    lateinit var listChats: ArrayList<Chat>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toggle = activeMenuManager.activeMenu()






        val listMessage1 = arrayListOf<Message>()
        listMessage1.add(
            Message("Mickaela", "Es todo.", "2:30")
        )
        listMessage1.add(
            Message("Mickaela", "No te preocupes", "2:31")
        )
        val listMessage2 = arrayListOf<Message>()
        listMessage2.add(
            Message("Alejandra", "Es todo.", "2:30")
        )
        listMessage2.add(
            Message("Alejandra", "No te preocupes", "2:31")
        )
        val listMessage3 = arrayListOf<Message>()
        listMessage3.add(
            Message("Tio Carlos", "Es todo.", "2:30")
        )
        listMessage3.add(
            Message("Tio Carlos", "No te preocupes", "2:31")
        )
        listChats = arrayListOf<Chat>()
        listChats.add(Chat("Mickaela", listMessage1 , R.drawable.img1))
        listChats.add(Chat("Alejandra", listMessage2 , R.drawable.img2))
        listChats.add(Chat("Tio Carlos", listMessage3 , R.drawable.img3))

        // inicializar RV
        val recyclerView = findViewById<RecyclerView>(R.id.id_recycler_view_chats)
        initRecyclerView(listChats, recyclerView)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredList = filter(listChats, newText!!)
                initRecyclerView(filteredList, findViewById(R.id.id_recycler_view_chats))
                return false
            }
        })
        return true
    }




    fun initRecyclerView(
        listChats: ArrayList<Chat>,
        recyclerView: RecyclerView
    ){
        val adapter = ChatAdapter(
            this,
            listChats
        )
        recyclerView.adapter = adapter
        recyclerView.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        recyclerView.addItemDecoration(
            DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL)
        )
        adapter.notifyDataSetChanged()
    }
    fun filter(list: ArrayList<Chat>, text: String): ArrayList<Chat> {
        val filteredList = ArrayList<Chat>()
        for (chat in list) {
            if (chat.name!!.lowercase(Locale.getDefault()).contains(text.lowercase(Locale.getDefault()))) {
                filteredList.add(chat)
            }
        }
        return filteredList
    }







}