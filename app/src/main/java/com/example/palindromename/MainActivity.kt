package com.example.palindromename

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.palindromename.ui.theme.PalindromeNameTheme

class MainActivity : AppCompatActivity() {
    private lateinit var etName: EditText
    private lateinit var etSentence: EditText
    private lateinit var btnCheck: Button
    private lateinit var btnNext: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etName = findViewById(R.id.etName)
        etSentence = findViewById(R.id.etPalindrome)
        btnCheck = findViewById(R.id.btnCheck)
        btnNext = findViewById(R.id.btnNext)

        btnCheck.setOnClickListener {
            val sentence = etSentence.text.toString()
            val cleaned = sentence.replace(" ", "").lowercase()
            val reversed = cleaned.reversed()
            val isPalindrome = cleaned == reversed

            val message = if (isPalindrome) "isPalindrome" else "not palindrome"
            AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show()
        }

        btnNext.setOnClickListener {
            val name = etName.text.toString()
            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtra("user_name", name)
            startActivity(intent)
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PalindromeNameTheme {
        Greeting("Android")
    }
}