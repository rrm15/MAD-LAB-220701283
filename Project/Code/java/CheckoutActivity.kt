package com.example.ezbill

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ezbill.models.Order
import com.example.ezbill.models.Product
import com.example.ezbill.utils.FirebaseUtils
import com.google.firebase.firestore.FieldValue

class CheckoutActivity : AppCompatActivity() {
    private lateinit var placeOrderButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        placeOrderButton = findViewById(R.id.placeOrderButton)

        placeOrderButton.setOnClickListener {
            placeOrder()
        }
    }

    private fun placeOrder() {
        val userEmail = FirebaseUtils.auth.currentUser?.email ?: return

        FirebaseUtils.db.collection("cart")
            .whereEqualTo("buyerEmail", userEmail)
            .get()
            .addOnSuccessListener { snapshot ->
                val products = snapshot.documents.mapNotNull { it.toObject(Product::class.java) }
                val productNames = products.map { it.name }
                val total = products.sumOf { it.price }

                val order = Order(productNames = productNames, totalPrice = total, buyerEmail = userEmail)

                FirebaseUtils.db.collection("orders")
                    .add(order)
                    .addOnSuccessListener {
                        clearCart(userEmail)
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Order Failed: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            }
    }

    private fun clearCart(userEmail: String) {
        FirebaseUtils.db.collection("cart")
            .whereEqualTo("buyerEmail", userEmail)
            .get()
            .addOnSuccessListener { snapshot ->
                val batch = FirebaseUtils.db.batch()
                for (doc in snapshot.documents) {
                    batch.delete(doc.reference)
                }
                batch.commit().addOnSuccessListener {
                    Toast.makeText(this, "Order Placed Successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
    }
}
