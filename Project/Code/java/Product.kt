package com.example.ezbill.models

data class Product(
    val id: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val timestamp: Long = System.currentTimeMillis()
)

// File: Order.kt
package com.example.ezbill.models

data class Order(
    val productNames: List<String> = listOf(),
    val totalPrice: Double = 0.0,
    val timestamp: Long = System.currentTimeMillis(),
    val buyerEmail: String = ""
)

// Next, we’ll write the utility class for Firebase operations.

// File: FirebaseUtils.kt
package com.example.ezbill.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object FirebaseUtils {
    val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
}

// Now we proceed to the activities (UI logic) one by one, starting with LoginActivity.

// File: LoginActivity.kt
package com.example.ezbill

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ezbill.utils.FirebaseUtils

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val email = findViewById<EditText>(R.id.loginEmail)
        val password = findViewById<EditText>(R.id.loginPassword)
        val loginBtn = findViewById<Button>(R.id.loginButton)

        loginBtn.setOnClickListener {
            FirebaseUtils.auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnSuccessListener {
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Login Failed: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
