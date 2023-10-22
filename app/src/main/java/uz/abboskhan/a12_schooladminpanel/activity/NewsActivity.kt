package uz.abboskhan.a12_schooladminpanel.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import uz.abboskhan.a12_schooladminpanel.R
import uz.abboskhan.a12_schooladminpanel.adapter.NewsAdapter
import uz.abboskhan.a12_schooladminpanel.adapter.TeacherAdapter
import uz.abboskhan.a12_schooladminpanel.databinding.ActivityNewsBinding
import uz.abboskhan.a12_schooladminpanel.model.NewsData
import uz.abboskhan.a12_schooladminpanel.model.TeacherData

class NewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsBinding
    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("NewsData")
    private lateinit var myNewsAdapter: NewsAdapter
    private lateinit var mList: ArrayList<NewsData>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mList = ArrayList()
        getNewsRewData()


        binding.addNewsFloBtn.setOnClickListener {
            startActivity(Intent(this, AddNewsActivity::class.java))

        }

        binding.rewNews.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && binding.addNewsFloBtn.isShown) {
                    binding.addNewsFloBtn.hide()
                } else if (dy < 0 && ! binding.addNewsFloBtn.isShown) {
                    binding.addNewsFloBtn.show()
                }
            }
        })

    }


    private fun getNewsRewData() {
        binding.prgNews.visibility = View.VISIBLE
        mList = ArrayList()
        databaseReference.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                mList.clear()
                for (i in snapshot.children) {
                    val data = i.getValue(NewsData::class.java)
                    mList.add(data!!)
                }
                binding.prgNews.visibility = View.GONE

                myNewsAdapter = NewsAdapter(mList, this@NewsActivity)
                binding.rewNews.setHasFixedSize(true)
                binding.rewNews.layoutManager = LinearLayoutManager(this@NewsActivity)
                binding.rewNews.adapter = myNewsAdapter

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@NewsActivity, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}