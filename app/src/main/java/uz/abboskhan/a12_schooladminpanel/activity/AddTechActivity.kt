package uz.abboskhan.a12_schooladminpanel.activity

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import uz.abboskhan.a12_schooladminpanel.model.TeacherData
import uz.abboskhan.a12_schooladminpanel.databinding.ActivityAddTechBinding

class AddTechActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddTechBinding
    private val storageReference: StorageReference =
        FirebaseStorage.getInstance().getReference("TeacherImages")
    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("TeacherData")
    private var imageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTechBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addTechBtn.setOnClickListener {
            getTechData()
        }
        binding.addTechImg.setOnClickListener {
            resultLauncher.launch("image/*")
        }


    }

    private fun getTechData() {
        binding.prgAddTech.visibility = View.VISIBLE
        val techName = binding.addTechName.text.toString()
        val techSuraName = binding.addTechNameLas.text.toString()
        val techAge = binding.addTechAge.text.toString()
        val techScience = binding.addTechScience.text.toString()
        val techPhoneNumber = binding.addTechPhoneNumber.text.toString()
        val timestamp = System.currentTimeMillis()
         val id = "$timestamp"
        val imageRef = storageReference.child("${System.currentTimeMillis()}.jpg")

        imageUri?.let {
            imageRef.putFile(it)
                .addOnSuccessListener { taskSnapshot ->
                    imageRef.downloadUrl.addOnSuccessListener { uri ->
                        val imageUrl = uri.toString()

                        saveDataToDatabase(
                            id,
                            timestamp,
                            imageUrl,
                            techName,
                            techSuraName,
                            techAge,
                            techScience,
                            techPhoneNumber
                        )
                        binding.prgAddTech.visibility = View.GONE
                        onBackPressed()
                        Toast.makeText(this, "Teacher data save", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->


                    // Rasmni yuklashda xatolik yuz berdi
                    Toast.makeText(this, "Teacher data error ${e.message}", Toast.LENGTH_SHORT)
                        .show()


                }


        }
    }

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {

        imageUri = it
        binding.addTechImg.setImageURI(it)

    }

    private fun saveDataToDatabase(
        id: String,
        timestamp: Long,
        imageUrl: String,
        techName: String,
        techSuraName: String,
        techAge: String,
        techScience: String,
        techPhoneNumber: String
    ) {
        val key = databaseReference.push().key

        if (key != null) {
            val data =
                TeacherData(id,timestamp,imageUrl, techName, techSuraName, techAge, techScience, techPhoneNumber)
            databaseReference.child(key).setValue(data)
        }


    }

}