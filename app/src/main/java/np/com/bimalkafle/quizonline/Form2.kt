package np.com.bimalkafle.quizonline

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.bottomnavigation.BottomNavigationView
import np.com.bimalkafle.quizonline.databinding.Form2Binding

class Form2 : AppCompatActivity() {
    private lateinit var binding: Form2Binding
    private var backPressedTime: Long = 0
    private val doubleBackToExitInterval: Long = 2000 // 2 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = Form2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set default selected item in bottom navigation
        binding.bottomNav.selectedItemId = R.id.nav_b // Highlight the second item

        setupBottomNavigationView()

        // Register OnBackPressedDispatcher callback
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val currentTime = SystemClock.elapsedRealtime()
                if (currentTime - backPressedTime < doubleBackToExitInterval) {
                    // Minimize the app and go to the home screen
                    moveTaskToBack(true)
                } else {
                    // Show a message to the user or perform other actions
                    backPressedTime = currentTime
                    // Optionally, show a Toast message to indicate double press
                    // Toast.makeText(this@Form2, "Press back again to minimize", Toast.LENGTH_SHORT).show()
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
                    // Do nothing since this is the current activity
                    true
                }
                R.id.nav_c -> {
                    launchForm3()
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

    private fun launchForm3() {
        val intent = Intent(this, Form3::class.java)
        startActivity(intent)
        finish() // Close current activity after launching Form3
    }
}
