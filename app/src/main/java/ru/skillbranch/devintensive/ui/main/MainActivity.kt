package ru.skillbranch.devintensive.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.extensions.setBackgroundDrawable
import ru.skillbranch.devintensive.extensions.setTextColor
import ru.skillbranch.devintensive.models.data.ChatType
import ru.skillbranch.devintensive.ui.adapters.ChatAdapter
import ru.skillbranch.devintensive.ui.adapters.ChatItemTouchHelperCallback
import ru.skillbranch.devintensive.ui.archive.ArchiveActivity
import ru.skillbranch.devintensive.ui.group.GroupActivity
import ru.skillbranch.devintensive.viewmodels.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var chatAdapter: ChatAdapter
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initToolbar()
        initViews()
        initViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView

        searchView.queryHint = "Введите имя пользователя"
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.handleSearchQuery(newText)
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.handleSearchQuery(query)
                return true
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
    }

    private fun initViews() {
        val typedValue = TypedValue()
        val theme = this.theme
        theme.resolveAttribute(R.attr.colorSnackbarText, typedValue, true)

        chatAdapter = ChatAdapter {
            if (it.chatType == ChatType.ARCHIVE) {
                val intent = Intent(this, ArchiveActivity::class.java)
                startActivity(intent)
            } else {
                Snackbar.make(rv_chat_list, "Click on ${it.title}", Snackbar.LENGTH_LONG)
                        .setBackgroundDrawable(R.drawable.bg_snackbar)
                        .setTextColor(typedValue.data)
                        .show()
            }
        }

        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        val touchCallback = ChatItemTouchHelperCallback(chatAdapter, false) {
            val chatItemId = it.id
            viewModel.addToArchive(chatItemId)

            Snackbar.make(rv_chat_list, "Вы точно хотите добавить ${it.title} в архив?", Snackbar.LENGTH_LONG)
                    .setAction("Отмена") {
                        viewModel.restoreFromArchive(chatItemId)
                    }
                    .setBackgroundDrawable(R.drawable.bg_snackbar)
                    .setTextColor(typedValue.data)
                    .show()
        }
        val touchHelper = ItemTouchHelper(touchCallback)

        touchHelper.attachToRecyclerView(rv_chat_list)

        with(rv_chat_list) {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            addItemDecoration(divider)
        }

        fab.setOnClickListener {
            val intent = Intent(this, GroupActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.getChatData().observe(this, Observer { chatAdapter.updateData(it) })
    }
}
