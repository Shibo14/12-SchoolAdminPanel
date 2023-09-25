package uz.abboskhan.a12_schooladminpanel

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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
                    myAdapter = TableAdapter(mTableList, this@ClassTableActivity)
                    binding.rewClassTable.adapter = myAdapter

                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ClassTableActivity, "error", Toast.LENGTH_SHORT).show()
                }
            })


    }


//    private fun dialogClassTableData() {
//        val editDialog = androidx.appcompat.app.AlertDialog.Builder(this)
//        val editDialogView = layoutInflater.inflate(R.layout.dialog_add_class_table, null)
//
//        val dialogText1 = editDialogView.findViewById<EditText>(R.id.dialog_text1)
//        val dialogText2 = editDialogView.findViewById<EditText>(R.id.dialog_text2)
//        val dialogText3 = editDialogView.findViewById<EditText>(R.id.dialog_text3)
//        val dialogText4 = editDialogView.findViewById<EditText>(R.id.dialog_text4)
//        val dialogText5 = editDialogView.findViewById<EditText>(R.id.dialog_text5)
//        val dialogText6 = editDialogView.findViewById<EditText>(R.id.dialog_text6)
//        val dialogText7 = editDialogView.findViewById<EditText>(R.id.dialog_text7)
//        val addDay = editDialogView.findViewById<EditText>(R.id.day)
//
//
//        spinnerBtn = editDialogView.findViewById<TextView>(R.id.addTableCategory)
//        spinnerBtn.setOnClickListener {
//            Toast.makeText(this, "dialog", Toast.LENGTH_SHORT).show()
//            sDialog()
//        }
//        editDialog.setView(editDialogView)
//        editDialog.setPositiveButton("Saqlash") { _, _ ->
//            val lesson1 = dialogText1.text.toString().trim()
//            val lesson2 = dialogText2.text.toString().trim()
//            val lesson3 = dialogText3.text.toString().trim()
//            val lesson4 = dialogText4.text.toString().trim()
//            val lesson5 = dialogText5.text.toString().trim()
//            val lesson6 = dialogText6.text.toString().trim()
//            val lesson7 = dialogText7.text.toString().trim()
//            val day = addDay.text.toString().trim()
//
//
//            val timestamp = System.currentTimeMillis()
//
//
//            val hashMap = HashMap<String, Any>()
//            hashMap["id"] = "$timestamp"
//            hashMap["day"] = "$day"
//            hashMap["lesson1"] = "$lesson1"
//            hashMap["lesson2"] = "$lesson2"
//            hashMap["lesson3"] = "$lesson3"
//            hashMap["lesson4"] = "$lesson4"
//            hashMap["lesson5"] = "$lesson5"
//            hashMap["lesson6"] = "$lesson6"
//            hashMap["lesson7"] = "$lesson7"
//            hashMap["classId"] = "$classId"
//
//            hashMap["timestamp"] = timestamp
//
//
//
//            firebaseData.child("$timestamp")
//                .setValue(hashMap)
//                .addOnSuccessListener {
//
//                    Toast.makeText(this, "Malumotlar qo'shildi.", Toast.LENGTH_SHORT).show()
//
//                }
//                .addOnFailureListener {
//
//                    Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
//                }
//
//
//        }
//        editDialog.setNegativeButton("Bekor qilish") { _, _ ->
//
//        }
//
//        val dialog = editDialog.create()
//        dialog.show()
//
//
//    }
//
//    private fun sDialog() {
//        classArrayList = ArrayList()
//        val ref = FirebaseDatabase.getInstance().getReference("Class")
//
//        ref.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                classArrayList.clear()
//
//                for (i in snapshot.children) {
//                    val m = i.getValue(ClassData::class.java)
//                    classArrayList.add(m!!)
//                    Log.d(TD, "ref_add: ${m.classNumber}")
//
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//
//            }
//        })
//
//
//        val classArray = arrayOfNulls<String>(classArrayList.size)
//
//        for (i in classArrayList.indices) {
//            classArray[i] = classArrayList[i].classNumber
//        }
//        val builder = AlertDialog.Builder(this)
//        builder.setTitle("Sinf tanlash")
//            .setItems(classArray) { d, which ->
//                classTitle = classArrayList[which].classNumber
//                classId = classArrayList[which].id
//
//                spinnerBtn.text = classTitle
//
//            }.show()
//
//
//    }
//

}