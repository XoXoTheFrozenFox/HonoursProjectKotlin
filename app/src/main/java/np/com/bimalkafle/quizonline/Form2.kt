// Form2.kt
package np.com.bimalkafle.quizonline

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import np.com.bimalkafle.quizonline.databinding.Form2Binding

class Form2 : AppCompatActivity() {
    private lateinit var binding: Form2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = Form2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set default selected item in bottom navigation
        binding.bottomNav.selectedItemId = R.id.nav_b // Highlight the second item

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
        finish() // Optional: Close current activity after launching MainActivity
    }

    private fun launchForm3() {
        val intent = Intent(this, Form3::class.java)
        startActivity(intent)
        finish() // Optional: Close current activity after launching Form3
    }
}
