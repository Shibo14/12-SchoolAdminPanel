package uz.abboskhan.a12_schooladminpanel.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.TextureView
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import uz.abboskhan.a12_schooladminpanel.adapter.LibraryAdapter
import uz.abboskhan.a12_schooladminpanel.adapter.TeacherAdapter
import uz.abboskhan.a12_schooladminpanel.databinding.ActivityLibraryBinding
import uz.abboskhan.a12_schooladminpanel.model.LibraryData
import uz.abboskhan.a12_schooladminpanel.model.TeacherData

class LibraryActivity : AppCompatActivity() {
    private val firebaseData = FirebaseDatabase.getInstance().getReference("BooksData")
    private lateinit var myAdapter: LibraryAdapter
    private lateinit var mList: ArrayList<LibraryData>
    private lateinit var binding: ActivityLibraryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.addLibrary.setOnClickListener {
            startActivity(Intent(this, AddLibraryActivity::class.java))
        }
         
        getRewDataLibrary()

    }


    private fun getRewDataLibrary() {
        mList = ArrayList()

        firebaseData.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                mList.clear()
                for (i in snapshot.children) {
                    val data = i.getValue(LibraryData::class.java)
                    mList.add(data!!)
                }

                myAdapter = LibraryAdapter(mList, this@LibraryActivity)
                binding.rewClassLibrary.setHasFixedSize(true)
                binding.rewClassLibrary.layoutManager = LinearLayoutManager(this@LibraryActivity)
                binding.rewClassLibrary.adapter = myAdapter

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}