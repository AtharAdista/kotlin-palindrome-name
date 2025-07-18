package com.example.palindromename.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.palindromename.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class UserViewModel : ViewModel() {
    private val _users = MutableLiveData<List<User>>(emptyList())
    val users: LiveData<List<User>> get() = _users

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _isEmpty = MutableLiveData(false)
    val isEmpty: LiveData<Boolean> get() = _isEmpty

    private val _hasMore = MutableLiveData(true)
    val hasMore: LiveData<Boolean> get() = _hasMore

    private val _selectedUserName = MutableLiveData("")
    val selectedUserName: LiveData<String> get() = _selectedUserName

    private var page = 1


    fun fetchUsers(refresh: Boolean = false) {

        if (_isLoading.value == true || _hasMore.value == false) return


        if (refresh) {
            page = 1
            _users.value = emptyList()
            _hasMore.value = true
            _isEmpty.value = false
        }

        _isLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            val url = URL("https://reqres.in/api/users?page=$page&per_page=10")
            val connection = url.openConnection() as HttpURLConnection
            connection.setRequestProperty("x-api-key", "reqres-free-v1")

            try {
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                val json = JSONObject(response)
                val dataArray = json.getJSONArray("data")


                val fetchedUsers = mutableListOf<User>()
                for (i in 0 until dataArray.length()) {
                    val userJson = dataArray.getJSONObject(i)
                    val user = User(
                        id = userJson.getInt("id"),
                        email = userJson.getString("email"),
                        first_name = userJson.getString("first_name"),
                        last_name = userJson.getString("last_name"),
                        avatar = userJson.getString("avatar")
                    )
                    fetchedUsers.add(user)
                }

                val currentUsers = _users.value ?: emptyList()

                if (fetchedUsers.isEmpty()) {
                    if (currentUsers.isEmpty()) _isEmpty.postValue(true)
                    _hasMore.postValue(false)
                } else {
                    _users.postValue(currentUsers + fetchedUsers)
                    page++
                }

            } catch (e: Exception) {

                e.printStackTrace()
                _users.postValue(emptyList())
                _isEmpty.postValue(true)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun selectUser(name: String) {
        _selectedUserName.value = name
    }
}
