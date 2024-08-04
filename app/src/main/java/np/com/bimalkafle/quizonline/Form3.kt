// Form3.kt
package np.com.bimalkafle.quizonline

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import np.com.bimalkafle.quizonline.databinding.Form3Binding

class Form3 : AppCompatActivity() {
    private lateinit var binding: Form3Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = Form3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set default selected item in bottom navigation
        binding.bottomNav.selectedItemId = R.id.nav_c // Highlight the third item

        setupBottomNavigationView()
    }

    private fun setupBottomNavigationView() {
        val bottomNavigationView: BottomNavigationView = binding.bottomNav
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
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
        finish() // Optional: Close current activity after launching MainActivity
    }

    private fun launchForm2() {
        val intent = Intent(this, Form2::class.java)
        startActivity(intent)
        finish() // Optional: Close current activity after launching Form2
    }
}
