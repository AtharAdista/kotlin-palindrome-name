package com.example.palindromename.widgets

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.palindromename.R
import com.example.palindromename.model.User

class UserAdapter(private val onClick: (User) -> Unit) :
    ListAdapter<User, UserAdapter.UserViewHolder>(UserDiffCallback()) {

    class UserViewHolder(itemView: View, val onClick: (User) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val nameText: TextView = itemView.findViewById(R.id.userName)
        private val emailText: TextView = itemView.findViewById(R.id.userEmail)
        private val avatarImage: ImageView = itemView.findViewById(R.id.avatar)
        private var currentUser: User? = null

        fun bind(user: User) {
            currentUser = user
            nameText.text = "${user.first_name} ${user.last_name}"
            emailText.text = user.email
            Glide.with(itemView.context).load(user.avatar).into(avatarImage)

            itemView.setOnClickListener {
                currentUser?.let { onClick(it) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class UserDiffCallback : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean = oldItem == newItem
}
