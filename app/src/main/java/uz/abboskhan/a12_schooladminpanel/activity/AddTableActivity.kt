package uz.abboskhan.a12_schooladminpanel.activity

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import uz.abboskhan.a12_schooladminpanel.model.ClassData
import uz.abboskhan.a12_schooladminpanel.databinding.ActivityAddTableBinding

class AddTableActivity : AppCompatActivity() {
    private val firebaseData = FirebaseDatabase.getInstance().getReference("Table")
  //  private val ref = FirebaseDatabase.getInstance().getReference("Class")
    private lateinit var binding: ActivityAddTableBinding

    private lateinit var classArrayList: ArrayList<ClassData>
    private var classId = ""
    private var classTitle = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTableBinding.inflate(layoutInflater)
        setContentView(binding.root)
        classId = intent.getStringExtra("classNumberId")!!
       val getClassHarf = intent.getStringExtra("classHarf")!!

        loadDataAddFirebase()
        binding.addTableSave.setOnClickListener {
            addDataFirebase()
        }
//        binding.addTableCategory.setOnClickListener {
//            spinnerDialog()
//        }

    }

    private fun addDataFirebase() {

        val lesson1 = binding.dialogText1.text.toString().trim()
        val lesson2 = binding.dialogText2.text.toString().trim()
        val lesson3 = binding.dialogText3.text.toString().trim()
        val lesson4 = binding.dialogText4.text.toString().trim()
        val lesson5 = binding.dialogText5.text.toString().trim()
        val lesson6 = binding.dialogText6.text.toString().trim()
        val lesson7 = binding.dialogText7.text.toString().trim()
        val day = binding.day.text.toString().trim()


        val timestamp = System.currentTimeMillis()


        val hashMap = HashMap<String, Any>()
        hashMap["id"] = "$timestamp"
        hashMap["day"] = "$day"
        hashMap["lesson1"] = "$lesson1"
        hashMap["lesson2"] = "$lesson2"
        hashMap["lesson3"] = "$lesson3"
        hashMap["lesson4"] = "$lesson4"
        hashMap["lesson5"] = "$lesson5"
        hashMap["lesson6"] = "$lesson6"
        hashMap["lesson7"] = "$lesson7"
        hashMap["classId"] = "$classId"

        hashMap["timestamp"] = timestamp



        firebaseData.child("$timestamp")
            .setValue(hashMap)
            .addOnSuccessListener {
     onBackPressed()
                Toast.makeText(this, "Malumotlar qo'shildi.", Toast.LENGTH_SHORT).show()

            }

            .addOnFailureListener {
                Toast.makeText(this, "table data error.", Toast.LENGTH_SHORT).show()

            }

    }


    private fun loadDataAddFirebase() {
        classArrayList = ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("Class")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                classArrayList.clear()

                for (i in snapshot.children) {
                    val m = i.getValue(ClassData::class.java)
                    classArrayList.add(m!!)


                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AddTableActivity, "data error", Toast.LENGTH_SHORT)
                    .show()

            }
        })
    }






}