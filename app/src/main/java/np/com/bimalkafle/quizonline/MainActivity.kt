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
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        quizModelList = mutableListOf()
        setupRecyclerView()
        getDataFromFirebase()
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
                    // Toast.makeText(this@MainActivity, "Press back again to minimize", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun setupRecyclerView() {
        adapter = QuizListAdapter(quizModelList)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun getDataFromFirebase() {
        FirebaseDatabase.getInstance().reference
            .get()
            .addOnSuccessListener { dataSnapshot ->
                if (dataSnapshot.exists()) {
                    for (snapshot in dataSnapshot.children) {
                        val quizModel = snapshot.getValue(QuizModel::class.java)
                        if (quizModel != null) {
                            quizModelList.add(quizModel)
                        }
                    }
                    adapter.notifyDataSetChanged() // Notify adapter about data changes
                }
            }
    }

    private fun setupBottomNavigationView() {
        val bottomNavigationView: BottomNavigationView = binding.bottomNav
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_a -> {
                    // Do nothing since this is the current activity
                    true
                }
                R.id.nav_b -> {
                    launchForm2()
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

    private fun launchForm2() {
        val intent = Intent(this, Form2::class.java)
        startActivity(intent)
    }

    private fun launchForm3() {
        val intent = Intent(this, Form3::class.java)
        startActivity(intent)
    }
}
