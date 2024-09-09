package np.com.bimalkafle.quizonline

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.FirebaseDatabase
import np.com.bimalkafle.quizonline.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var quizModelList: MutableList<QuizModel>
    private lateinit var adapter: QuizListAdapter
    private var backPressedTime: Long = 0
    private val doubleBackToExitInterval: Long = 2000 // 2 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Disable night mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        // Set up view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize quiz model list and set up RecyclerView
        quizModelList = mutableListOf()
        setupRecyclerView()

        // Fetch data from Firebase
        getDataFromFirebase()

        // Set up bottom navigation view
        setupBottomNavigationView()

        // Handle back press to minimize the app
        handleBackPress()
    }

    // Set up the RecyclerView with an adapter and layout manager
    private fun setupRecyclerView() {
        adapter = QuizListAdapter(quizModelList)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    // Fetch data from Firebase and populate the RecyclerView
    private fun getDataFromFirebase() {
        FirebaseDatabase.getInstance().reference
            .get()
            .addOnSuccessListener { dataSnapshot ->
                if (dataSnapshot.exists()) {
                    for (snapshot in dataSnapshot.children) {
                        val quizModel = snapshot.getValue(QuizModel::class.java)
                        quizModel?.let { quizModelList.add(it) }
                    }
                    adapter.notifyDataSetChanged() // Notify adapter about data changes
                }
            }
    }

    // Set up the bottom navigation view and handle item selection
    private fun setupBottomNavigationView() {
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_a -> true // Current activity, do nothing
                R.id.nav_b -> {
                    launchFormActivity(Form2::class.java)
                    true
                }
                R.id.nav_c -> {
                    launchFormActivity(Form3::class.java)
                    true
                }
                else -> false
            }
        }
    }

    // Launch a new activity
    private fun launchFormActivity(activityClass: Class<*>) {
        val intent = Intent(this, activityClass)
        startActivity(intent)
    }

    // Handle back press to minimize the app
    private fun handleBackPress() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val currentTime = SystemClock.elapsedRealtime()
                if (currentTime - backPressedTime < doubleBackToExitInterval) {
                    // Minimize the app and go to the home screen
                    moveTaskToBack(true)
                } else {
                    // Update the back pressed time and optionally show a Toast message
                    backPressedTime = currentTime
                    // Uncomment to show a Toast message
                    // Toast.makeText(this@MainActivity, "Press back again to minimize", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}
