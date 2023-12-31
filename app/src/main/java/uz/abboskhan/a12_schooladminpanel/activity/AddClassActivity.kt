package uz.abboskhan.a12_schooladminpanel.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import uz.abboskhan.a12_schooladminpanel.adapter.ClassAdapter
import uz.abboskhan.a12_schooladminpanel.model.ClassData
import uz.abboskhan.a12_schooladminpanel.R
import uz.abboskhan.a12_schooladminpanel.databinding.ActivityAddClassBinding
import uz.abboskhan.a12_schooladminpanel.model.Progressbar

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

         binding.prgAddClass.visibility = View.VISIBLE
        getDataRew()

               binding.addClass.setOnClickListener {
                   dialogData()

               }


        binding.rewClass.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && binding.addClass.isShown) {
                    binding.addClass.hide()
                } else if (dy < 0 && ! binding.addClass.isShown) {
                    binding.addClass.show()
                }
            }
        })


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
                binding.prgAddClass.visibility = View.GONE



                classAdapter = ClassAdapter(mList, this@AddClassActivity)
                binding.rewClass.setHasFixedSize(true)
                binding.rewClass.layoutManager = LinearLayoutManager(this@AddClassActivity)
                binding.rewClass.adapter = classAdapter


            }

            override fun onCancelled(error: DatabaseError) {
                binding.prgAddClass.visibility = View.GONE


            }
        })

    }

    private fun dialogData() {

        val editDialog = AlertDialog.Builder(this)
        val editDialogView = layoutInflater.inflate(R.layout.dialog_edd, null)

        dialogText = editDialogView.findViewById<EditText>(R.id.dialog_text)
      val classHarfEdt = editDialogView.findViewById<EditText>(R.id.dialog_class)

        editDialog.setView(editDialogView)
        editDialog.setPositiveButton("Saqlash") { _, _ ->
            classData = dialogText.text.toString().trim()
          val  classHarf = classHarfEdt.text.toString().trim()


  if (classHarf.isEmpty()&&classData.isEmpty()) {
      Toast.makeText(this, "Ma'lumotni to'liq kiriting", Toast.LENGTH_SHORT).show()


  }else{
      getNoteData(classHarf,classData)
  }
        }
        editDialog.setNegativeButton("Bekor qilish") { _, _ ->

        }

        val dialog = editDialog.create()
        dialog.show()


    }

    private fun getNoteData(classHarf: String, classData: String) {
        val timestamp = System.currentTimeMillis()
        val hashMap = HashMap<String, Any>()
        hashMap["id"] = "$timestamp"
        hashMap["classNumber"] = classData
        hashMap["harf"] = classHarf
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


}