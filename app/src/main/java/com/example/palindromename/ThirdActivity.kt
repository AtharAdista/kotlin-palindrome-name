package com.example.palindromename
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.palindromename.viewmodels.UserViewModel
import com.example.palindromename.widgets.UserAdapter
import android.view.View
import androidx.lifecycle.observe

class ThirdActivity : AppCompatActivity() {

    private val userViewModel: UserViewModel by viewModels()
    private lateinit var userAdapter: UserAdapter
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyTextView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        swipeRefresh = findViewById(R.id.swipeRefreshLayout)
        recyclerView = findViewById(R.id.recyclerView)

        userAdapter = UserAdapter { user ->
            val fullName = "${user.first_name} ${user.last_name}"
            val intent = Intent().apply {
                putExtra("selected_name", fullName)
            }
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        emptyTextView = findViewById(R.id.tvEmpty)

        userViewModel.isEmpty.observe(this) { isEmpty ->
            if (isEmpty) {
                emptyTextView.visibility = View.VISIBLE
            } else {
                emptyTextView.visibility = View.GONE
            }
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = userAdapter

        swipeRefresh.setOnRefreshListener {
            userViewModel.fetchUsers(refresh = true)
        }

        userViewModel.users.observe(this) { users ->
            userAdapter.submitList(users)
        }

        userViewModel.isLoading.observe(this) { loading ->
            swipeRefresh.isRefreshing = loading
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(rv, dx, dy)
                val lm = rv.layoutManager as LinearLayoutManager
                val lastVisible = lm.findLastVisibleItemPosition()
                if (lastVisible >= userAdapter.itemCount - 1) {
                    userViewModel.fetchUsers()
                }
            }
        })

        userViewModel.fetchUsers()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
