package com.example.siyakhula

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class Dashboard : AppCompatActivity() {

    private lateinit var upcomingprogramsButton: Button
    private lateinit var donationsButton: Button
    private lateinit var volunteerButton: Button
    private lateinit var aboutusButton: Button
    private lateinit var contactusButton: Button
    private lateinit var signoutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        upcomingprogramsButton = findViewById(R.id.upcomingprogramsButton)
        donationsButton = findViewById(R.id.donationsButton)
        volunteerButton = findViewById(R.id.volunteerButton)
        aboutusButton = findViewById(R.id.aboutusButton)
        contactusButton = findViewById(R.id.contactusButton)
        signoutButton = findViewById(R.id.signoutButton)

        //Setting up click listeners for each button below(Undo the comments for the page that you added)
        upcomingprogramsButton.setOnClickListener{
          startActivity(Intent(this , Upcoming_Programs::class.java))
        }

        donationsButton.setOnClickListener{
            startActivity(Intent(this , Donations::class.java))
        }

        //volunteerButton.setOnClickListener{
        //    startActivity(Intent(this , VolunteerPage::class.java))
        //}

        //aboutusButton.setOnClickListener{
        //    startActivity(Intent(this , AboutUsPage::class.java))
        //}

        //contactusButton.setOnClickListener{
        //    startActivity(Intent(this , ContactUsPage::class.java))
        //}

        signoutButton.setOnClickListener{
        // Sign out the user and return to login screen
          FirebaseAuth.getInstance().signOut()
          startActivity(Intent(this, Login::class.java))
          finish()
        }

    }
}