package uz.abboskhan.a12_schooladminpanel.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import uz.abboskhan.a12_schooladminpanel.adapter.TableAdapter
import uz.abboskhan.a12_schooladminpanel.model.TableData
import uz.abboskhan.a12_schooladminpanel.databinding.ActivityClassTableBinding

class ClassTableActivity : AppCompatActivity() {
    private val firebaseData = FirebaseDatabase.getInstance().getReference("Table")
    private val ref = FirebaseDatabase.getInstance().getReference("Class")
    private lateinit var binding: ActivityClassTableBinding
    private var classId = ""
    private var classTitle = ""
    private lateinit var mTableList: ArrayList<TableData>
    private lateinit var spinnerBtn: TextView
    private lateinit var myAdapter: TableAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClassTableBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.prgTable.visibility = View.VISIBLE

        classTitle = intent.getStringExtra("classNumber")!!
        classId = intent.getStringExtra("classNumberId")!!
        loadDataTable()
        binding.addSchedule.setOnClickListener {
            val i = Intent(this, AddTableActivity::class.java)
            i.putExtra("classNumberId", classId)
            startActivity(i)

        }


    }

    private fun loadDataTable() {
        binding.prgTable.visibility = View.VISIBLE
        mTableList = ArrayList()
        firebaseData.orderByChild("classId").equalTo(classId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    mTableList.clear()

                    for (i in snapshot.children) {
                        val m = i.getValue(TableData::class.java)

                        if (m != null) {
                            mTableList.add(m)
                        }


                    }
                    binding.prgTable.visibility = View.GONE
                    binding.prgTable.visibility = View.GONE
                    myAdapter = TableAdapter(mTableList, this@ClassTableActivity)
                    binding.rewClassTable.adapter = myAdapter

                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ClassTableActivity, "error", Toast.LENGTH_SHORT).show()
                }
            })


    }





}