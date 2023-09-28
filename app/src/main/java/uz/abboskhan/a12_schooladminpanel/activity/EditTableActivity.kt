package uz.abboskhan.a12_schooladminpanel.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import uz.abboskhan.a12_schooladminpanel.databinding.ActivityEditTableBinding

class EditTableActivity : AppCompatActivity() {
    private val ref = FirebaseDatabase.getInstance().getReference("Class")
    private val firebaseData = FirebaseDatabase.getInstance().getReference("Table")
    private lateinit var binding: ActivityEditTableBinding
    private var classIdEdt = ""
    private var classCategoryEdt = ""

    private lateinit var classIdList: ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditTableBinding.inflate(layoutInflater)
        setContentView(binding.root)

        classIdEdt = intent.getStringExtra("classIdEdt")!!
        classCategoryEdt = intent.getStringExtra("classCategory")!!

        loadEdtData()
        loadInfo()
        binding.EdtTableSave.setOnClickListener {
            getEditData()
        }
    }

    private fun loadInfo() {
        firebaseData.child(classIdEdt)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val lesson1 = snapshot.child("lesson1").value.toString()
                    val lesson2 = snapshot.child("lesson2").value.toString()
                    val lesson3 = snapshot.child("lesson3").value.toString()
                    val lesson4 = snapshot.child("lesson4").value.toString()
                    val lesson5 = snapshot.child("lesson5").value.toString()
                    val lesson6 = snapshot.child("lesson6").value.toString()
                    val lesson7 = snapshot.child("lesson7").value.toString()
                    val day = snapshot.child("day").value.toString()

                    binding.dialogText1Edt.setText(lesson1)
                    binding.dialogText2Edt.setText(lesson2)
                    binding.dialogText3Edt.setText(lesson3)
                    binding.dialogText4Edt.setText(lesson4)
                    binding.dialogText5Edt.setText(lesson5)
                    binding.dialogText6Edt.setText(lesson6)
                    binding.dialogText7Edt.setText(lesson7)
                    binding.dayEdt.setText(day)


                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

    }

    private fun getEditData() {

        val lesson1 = binding.dialogText1Edt.text.toString().trim()
        val lesson2 = binding.dialogText2Edt.text.toString().trim()
        val lesson3 = binding.dialogText3Edt.text.toString().trim()
        val lesson4 = binding.dialogText4Edt.text.toString().trim()
        val lesson5 = binding.dialogText5Edt.text.toString().trim()
        val lesson6 = binding.dialogText6Edt.text.toString().trim()
        val lesson7 = binding.dialogText7Edt.text.toString().trim()
        val day = binding.dayEdt.text.toString().trim()

        val hashMap = HashMap<String, Any>()
        hashMap["id"] = "$classIdEdt"
        hashMap["classId"] = "$classCategoryEdt"
        hashMap["day"] = "$day"
        hashMap["lesson1"] = "$lesson1"
        hashMap["lesson2"] = "$lesson2"
        hashMap["lesson3"] = "$lesson3"
        hashMap["lesson4"] = "$lesson4"
        hashMap["lesson5"] = "$lesson5"
        hashMap["lesson6"] = "$lesson6"
        hashMap["lesson7"] = "$lesson7"


        firebaseData.child(classIdEdt)
            .setValue(hashMap)
            .addOnSuccessListener {

                Toast.makeText(this, "Malumotlar Edit.", Toast.LENGTH_SHORT).show()
               startActivity(Intent(this, EditTableActivity::class.java))
                finish()
            }

            .addOnFailureListener {
                Toast.makeText(this, "Edit data error.", Toast.LENGTH_SHORT).show()

            }

    }

    private fun loadEdtData() {
        classIdList = ArrayList()
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                classIdList.clear()
                for (i in snapshot.children) {
                    val id = "${i.child("id").value}"
                    classIdList.add(id)
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }


}