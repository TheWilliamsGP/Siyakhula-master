package com.example.siyakhula

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Register : AppCompatActivity() {

    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var registerBtn: Button
    private lateinit var gotoLogin: Button
    private lateinit var fAuth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore
    private var valid = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        email = findViewById(R.id.registerEmail)
        password = findViewById(R.id.registerPassword)
        registerBtn = findViewById(R.id.registerBtn)
        gotoLogin = findViewById(R.id.gotoLogin)

        fAuth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()

        registerBtn.setOnClickListener {
            checkField(email)
            checkField(password)

            if (valid) {
                registerUser(email.text.toString(), password.text.toString())
            }
        }

        gotoLogin.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
        }
    }

    private fun checkField(textField: EditText): Boolean {
        return if (textField.text.toString().isEmpty()) {
            textField.error = "This field is required"
            valid = false
            false
        } else {
            true
        }
    }

    private fun registerUser(email: String, password: String) {
        if (email.endsWith("@siyakhula.org")) {
            // Register as admin
            createUser(email, password, isAdmin = true)
        } else {
            // Register as normal user
            createUser(email, password, isAdmin = false)
        }
    }

    private fun createUser(email: String, password: String, isAdmin: Boolean) {
        fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = fAuth.currentUser
                user?.sendEmailVerification()?.addOnSuccessListener {
                    Toast.makeText(this, "Verification email sent. Please verify your email before logging in.", Toast.LENGTH_SHORT).show()
                }?.addOnFailureListener {
                    Toast.makeText(this, "Failed to send verification email.", Toast.LENGTH_SHORT).show()
                }

                val userData = hashMapOf(
                    "email" to email,
                    if (isAdmin) "isAdmin" to true else "isUser" to true
                )

                fStore.collection("Users").document(user?.uid!!).set(userData).addOnSuccessListener {
                    Log.d("TAG", "User data saved")
                    // Log out the user after registration to require email verification
                    FirebaseAuth.getInstance().signOut()
                    Toast.makeText(this, "Please verify your email and then log in.", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, Login::class.java))
                    finish()
                }.addOnFailureListener { e ->
                    Log.e("TAG", "Error saving user data", e)
                }
            } else {
                Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
