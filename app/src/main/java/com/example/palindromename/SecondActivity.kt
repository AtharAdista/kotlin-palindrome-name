package com.example.palindromename

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.palindromename.R

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        // Setup Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            title = "Second Screen"
            setDisplayHomeAsUpEnabled(true)
        }

        // Ambil data dari Intent
        val name = intent.getStringExtra("user_name") ?: ""
        val tvName = findViewById<TextView>(R.id.tvName)
        tvName.text = name

        // Tombol Choose a User
        val btnChooseUser: Button = findViewById(R.id.btnChooseUser)
        btnChooseUser.setOnClickListener {
            val intent = Intent(this, ThirdActivity::class.java)
            startActivityForResult(intent, 1001)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            val selectedName = data?.getStringExtra("selected_name") ?: return
            val tvName = findViewById<TextView>(R.id.tvSelectedUser)
            tvName.text = selectedName
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
