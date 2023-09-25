package uz.abboskhan.a12_schooladminpanel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import uz.abboskhan.a12_schooladminpanel.databinding.ActivityAddClassBinding

class AddClassActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddClassBinding
    private lateinit var classAdapter: ClassAdapter
    private lateinit var mList: ArrayList<ClassData>

    private lateinit var dialogText: EditText
    private var classData: String = ""
    private val firebaseData = FirebaseDatabase.getInstance().getReference("Class")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddClassBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getDataRew()
              binding.prgClass.visibility = View.VISIBLE

           if (btnGone(binding.addClass)) {
               binding.addClass.setOnClickListener {
                   dialogData()
               }

           }


    }
    private fun btnGone(btn: FloatingActionButton): Boolean {
        firebaseData.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if (dataSnapshot.exists()) {
                    btn.visibility = View.VISIBLE
                } else {
                  btn.visibility = View.GONE
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
        return true
    }

    private fun getDataRew() {
        mList = ArrayList()

        firebaseData.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mList.clear()
                for (i in snapshot.children) {
                    val data = i.getValue(ClassData::class.java)
                    mList.add(data!!)

                }

                binding.prgClass.visibility = View.GONE

                classAdapter = ClassAdapter(mList, this@AddClassActivity)
                binding.rewClass.setHasFixedSize(true)
                binding.rewClass.layoutManager = LinearLayoutManager(this@AddClassActivity)
                binding.rewClass.adapter = classAdapter

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }

    private fun dialogData() {
        val editDialog = AlertDialog.Builder(this)
        val editDialogView = layoutInflater.inflate(R.layout.dialog_edd, null)

        dialogText = editDialogView.findViewById<EditText>(R.id.dialog_text)

        editDialog.setView(editDialogView)
        editDialog.setPositiveButton("Saqlash") { _, _ ->
            classData = dialogText.text.toString().trim()

            val timestamp = System.currentTimeMillis()


            val hashMap = HashMap<String, Any>()
            hashMap["id"] = "$timestamp"
            hashMap["classNumber"] = classData
            hashMap["timestamp"] = timestamp



            firebaseData.child("$timestamp")
                .setValue(hashMap)
                .addOnSuccessListener {

                    Toast.makeText(this, "Malumotlar qo'shildi.", Toast.LENGTH_SHORT).show()

                }
                .addOnFailureListener {

                    Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
                }


        }
        editDialog.setNegativeButton("Bekor qilish") { _, _ ->

        }

        val dialog = editDialog.create()
        dialog.show()


    }


}