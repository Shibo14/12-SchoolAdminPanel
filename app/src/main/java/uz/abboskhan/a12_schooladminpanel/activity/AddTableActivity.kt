package uz.abboskhan.a12_schooladminpanel.activity

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import uz.abboskhan.a12_schooladminpanel.model.ClassData
import uz.abboskhan.a12_schooladminpanel.databinding.ActivityAddTableBinding
import uz.abboskhan.a12_schooladminpanel.model.Progressbar

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

        if (TextUtils.isEmpty(lesson1)) {
            binding.dealogTv1.error = "Fanni kiriting"
        } else if (TextUtils.isEmpty(lesson2)) {
            binding.dealogTv2.error = "Fanni kiriting"
        } else if (TextUtils.isEmpty(lesson3)) {
            binding.dealogTv3.error = "Fanni kiriting"
        } else if (TextUtils.isEmpty(lesson4)) {
            binding.dealogTv4.error = "Fanni kiriting"
        } else if (TextUtils.isEmpty(day)) {
            binding.dealogTv1.error = "Hafta kunini kiriting"
        } else {
            getTableData(lesson1, lesson2, lesson3, lesson4, lesson5, lesson6, lesson7, day)
        }


    }

    private fun getTableData(
        lesson1: String,
        lesson2: String,
        lesson3: String,
        lesson4: String,
        lesson5: String,
        lesson6: String,
        lesson7: String,
        day: String
    ) {

        val timestamp = System.currentTimeMillis()

        val myPrg = Progressbar(this)
        myPrg.startDialog()

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
                myPrg.dismissProgressBar()
                onBackPressed()
                Toast.makeText(this, "Malumotlar qo'shildi.", Toast.LENGTH_SHORT).show()

            }

            .addOnFailureListener {
                myPrg.dismissProgressBar()

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