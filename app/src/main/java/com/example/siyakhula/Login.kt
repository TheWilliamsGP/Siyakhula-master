package com.example.siyakhula

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class Login : AppCompatActivity() {

    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var loginBtn: Button
    private lateinit var gotoRegister: Button
    private var valid = true
    private lateinit var fAuth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        fAuth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()

        email = findViewById(R.id.loginEmail)
        password = findViewById(R.id.loginPassword)
        loginBtn = findViewById(R.id.loginBtn)
        gotoRegister = findViewById(R.id.gotoRegister)

        loginBtn.setOnClickListener {
            checkField(email)
            checkField(password)

            if (valid) {
                fAuth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                    .addOnSuccessListener { authResult: AuthResult ->
                        if (authResult.user?.isEmailVerified == true) {
                            Toast.makeText(this, "Logged in successfully.", Toast.LENGTH_SHORT).show()
                            checkUserAccessLevel(authResult.user?.uid ?: "")
                        } else {
                            Toast.makeText(this, "Please verify your email address before logging in.", Toast.LENGTH_SHORT).show()
                            fAuth.signOut()
                        }
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(this, "Login failed: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        gotoRegister.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }
    }

    private fun checkUserAccessLevel(uid: String) {
        val df: DocumentReference = fStore.collection("Users").document(uid)
        df.get().addOnSuccessListener { documentSnapshot ->
            Log.d("TAG", "onSuccess: ${documentSnapshot.data}")
            val isAdmin = documentSnapshot.get("isAdmin") as? Boolean ?: false
            val isUser = documentSnapshot.get("isUser") as? Boolean ?: false

            if (isAdmin && email.text.toString().endsWith("@siyakhula.org")) {
                startActivity(Intent(this, Admin::class.java))
                finish()
            } else if (isUser) {
                startActivity(Intent(this, Dashboard::class.java))
                finish()
            } else {
                Toast.makeText(this, "Invalid credentials or access level", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(this, "Failed to fetch user data: ${exception.message}", Toast.LENGTH_SHORT).show()
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

    override fun onStart() {
        super.onStart()
        if (fAuth.currentUser != null) {
            val uid = fAuth.currentUser?.uid ?: return
            checkUserAccessLevel(uid)
        }
    }
}
