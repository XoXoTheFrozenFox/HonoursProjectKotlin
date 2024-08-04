package np.com.bimalkafle.quizonline

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.FirebaseDatabase
import np.com.bimalkafle.quizonline.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var quizModelList: MutableList<QuizModel>
    private lateinit var adapter: QuizListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        quizModelList = mutableListOf()
        setupRecyclerView()
        getDataFromFirebase()
        setupBottomNavigationView()

        // Default activity to launch
        launchMainActivity()
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
                    launchMainActivity()
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

    private fun launchMainActivity() {
        // MainActivity is already the current activity, no need to launch it again
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
