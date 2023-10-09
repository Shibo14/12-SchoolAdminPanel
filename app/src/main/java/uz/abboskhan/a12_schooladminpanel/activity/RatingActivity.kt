package uz.abboskhan.a12_schooladminpanel.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import uz.abboskhan.a12_schooladminpanel.R
import uz.abboskhan.a12_schooladminpanel.adapter.RatingAdapter
import uz.abboskhan.a12_schooladminpanel.adapter.TeacherAdapter
import uz.abboskhan.a12_schooladminpanel.databinding.ActivityRatingBinding
import uz.abboskhan.a12_schooladminpanel.databinding.ActivityTeacherBinding
import uz.abboskhan.a12_schooladminpanel.model.RatingData
import uz.abboskhan.a12_schooladminpanel.model.TeacherData

class RatingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRatingBinding
    private lateinit var myAdapter: RatingAdapter
    private lateinit var mList: ArrayList<RatingData>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRatingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getRewRatingData()

        binding.addRatingBtn.setOnClickListener {

            startActivity(Intent(this, AddRatingActivity::class.java))
        }

    }

    private fun getRewRatingData() {
        binding.prgRating.visibility = View.VISIBLE
        mList = ArrayList()
        val firebaseData = FirebaseDatabase.getInstance().getReference("RatingData")
        firebaseData.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                mList.clear()
                for (i in snapshot.children) {
                    val data = i.getValue(RatingData::class.java)
                    mList.add(data!!)
                }
                binding.prgRating.visibility = View.GONE

                myAdapter = RatingAdapter(mList, this@RatingActivity)
                binding.rewRating.setHasFixedSize(true)
                binding.rewRating.layoutManager = LinearLayoutManager(this@RatingActivity)
                binding.rewRating.adapter = myAdapter

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}