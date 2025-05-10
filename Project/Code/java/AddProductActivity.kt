package com.example.ezbill

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ezbill.models.Product
import com.example.ezbill.utils.FirebaseUtils

class AddProductActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        val productName = findViewById<EditText>(R.id.productNameInput)
        val productPrice = findViewById<EditText>(R.id.productPriceInput)
        val addButton = findViewById<Button>(R.id.addProductButton)

        addButton.setOnClickListener {
            val name = productName.text.toString().trim()
            val price = productPrice.text.toString().toDoubleOrNull()

            if (name.isEmpty() || price == null) {
                Toast.makeText(this, "Please enter valid name and price", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val productId = FirebaseUtils.db.collection("products").document().id
            val product = Product(id = productId, name = name, price = price)

            FirebaseUtils.db.collection("products")
                .document(productId)
                .set(product)
                .addOnSuccessListener {
                    Toast.makeText(this, "Product Added", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to Add: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
