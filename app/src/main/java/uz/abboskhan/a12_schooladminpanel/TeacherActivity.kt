package uz.abboskhan.a12_schooladminpanel

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import uz.abboskhan.a12_schooladminpanel.databinding.ActivityTeacherBinding

class TeacherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTeacherBinding
    private lateinit var myAdapter: TeacherAdapter
    private lateinit var mList: ArrayList<TeacherData>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher)
        mList = ArrayList()


        getRewData()

        binding.addFloBtn.setOnClickListener {
            startActivity(Intent(this, AddTechActivity::class.java))
        }

    }

    private fun getRewData() {
        mList = ArrayList()
        val firebaseData = FirebaseDatabase.getInstance().getReference("Teacher")
        firebaseData.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                mList.clear()
                for (i in snapshot.children) {
                    val data = i.getValue(TeacherData::class.java)
                    mList.add(data!!)
                }
                myAdapter = TeacherAdapter(mList, this@TeacherActivity)
                binding.rewTech.setHasFixedSize(true)
                binding.rewTech.layoutManager = LinearLayoutManager(this@TeacherActivity)
                binding.rewTech.adapter = myAdapter

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}