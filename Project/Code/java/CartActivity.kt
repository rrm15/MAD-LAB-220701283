package com.example.ezbill

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ezbill.models.Product
import com.example.ezbill.utils.FirebaseUtils
import com.google.firebase.firestore.ListenerRegistration

class CartActivity : AppCompatActivity() {
    private lateinit var cartItemsView: TextView
    private lateinit var totalPriceView: TextView
    private lateinit var checkoutBtn: Button

    private val cartItems = mutableListOf<Product>()
    private var cartListener: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        cartItemsView = findViewById(R.id.cartItemsTextView)
        totalPriceView = findViewById(R.id.totalPriceTextView)
        checkoutBtn = findViewById(R.id.checkoutButton)

        listenToCartUpdates()

        checkoutBtn.setOnClickListener {
            startActivity(Intent(this, CheckoutActivity::class.java))
        }
    }

    private fun listenToCartUpdates() {
        val userEmail = FirebaseUtils.auth.currentUser?.email ?: return

        cartListener = FirebaseUtils.db.collection("cart")
            .whereEqualTo("buyerEmail", userEmail)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener

                cartItems.clear()
                for (doc in snapshot.documents) {
                    val product = doc.toObject(Product::class.java)
                    if (product != null) cartItems.add(product)
                }

                updateUI()
            }
    }

    private fun updateUI() {
        val names = cartItems.joinToString("\n") { it.name }
        val total = cartItems.sumOf { it.price }
        cartItemsView.text = names
        totalPriceView.text = "Total: $total"
    }

    override fun onDestroy() {
        cartListener?.remove()
        super.onDestroy()
    }
}
