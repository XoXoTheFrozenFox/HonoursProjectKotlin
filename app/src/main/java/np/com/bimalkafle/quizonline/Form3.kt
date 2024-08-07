package np.com.bimalkafle.quizonline

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.bottomnavigation.BottomNavigationView
import np.com.bimalkafle.quizonline.databinding.Form3Binding

class Form3 : AppCompatActivity() {
    private lateinit var binding: Form3Binding
    private var backPressedTime: Long = 0
    private val doubleBackToExitInterval: Long = 2000 // 2 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = Form3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set default selected item in bottom navigation
        binding.bottomNav.selectedItemId = R.id.nav_c // Highlight the third item

        setupBottomNavigationView()

        // Register OnBackPressedDispatcher callback to handle back press
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val currentTime = SystemClock.elapsedRealtime()
                if (currentTime - backPressedTime < doubleBackToExitInterval) {
                    // Minimize the app and go to the home screen
                    moveTaskToBack(true)
                } else {
                    // Update the back pressed time and optionally show a Toast message
                    backPressedTime = currentTime
                    // Optionally, show a Toast message to indicate double press
                    // Toast.makeText(this@Form3, "Press back again to minimize", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun setupBottomNavigationView() {
        binding.bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_a -> {
                    launchMainActivity()
                    true
                }
                R.id.nav_b -> {
                    launchForm2()
                    true
                }
                R.id.nav_c -> {
                    // Already on Form3, do nothing or handle as needed
                    true
                }
                else -> false
            }
        }
    }

    private fun launchMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Close current activity after launching MainActivity
    }

    private fun launchForm2() {
        val intent = Intent(this, Form2::class.java)
        startActivity(intent)
        finish() // Close current activity after launching Form2
    }
}
