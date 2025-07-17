package com.example.palindromename
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.palindromename.viewmodels.UserViewModel
import com.example.palindromename.widgets.UserAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ThirdActivity : AppCompatActivity() {

    private val userViewModel: UserViewModel by viewModels()
    private lateinit var userAdapter: UserAdapter
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)

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
}
