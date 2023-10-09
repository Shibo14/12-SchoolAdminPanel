package uz.abboskhan.a12_schooladminpanel.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import uz.abboskhan.a12_schooladminpanel.R
import uz.abboskhan.a12_schooladminpanel.adapter.NotificationAdapter
import uz.abboskhan.a12_schooladminpanel.adapter.TeacherAdapter
import uz.abboskhan.a12_schooladminpanel.databinding.ActivityNotificationBinding
import uz.abboskhan.a12_schooladminpanel.databinding.ActivityTeacherBinding
import uz.abboskhan.a12_schooladminpanel.model.NotificationData
import uz.abboskhan.a12_schooladminpanel.model.TeacherData

class NotificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotificationBinding
    private lateinit var myAdapter: NotificationAdapter
    private lateinit var mList: ArrayList<NotificationData>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getNotiData()
    }

    private fun getNotiData() {
        binding.prgNoti.visibility = View.VISIBLE
        mList = ArrayList()
        val firebaseData = FirebaseDatabase.getInstance().getReference("NotificationData")
        firebaseData.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                mList.clear()
                for (i in snapshot.children) {
                    val data = i.getValue(NotificationData::class.java)
                    mList.add(data!!)
                }
                binding.prgNoti.visibility = View.GONE

                myAdapter = NotificationAdapter(mList, this@NotificationActivity)
                binding.rewNotification.setHasFixedSize(true)
                binding.rewNotification.layoutManager =
                    LinearLayoutManager(this@NotificationActivity)
                binding.rewNotification.adapter = myAdapter

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }


}