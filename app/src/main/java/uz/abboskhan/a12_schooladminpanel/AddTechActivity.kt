package uz.abboskhan.a12_schooladminpanel

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
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
        setContentView(R.layout.activity_add_tech)

        binding.addTechBtn.setOnClickListener {
            getTechData()
        }
        binding.addTechImg.setOnClickListener {
            resultLauncher.launch("image/*")
        }


    }
    private fun getTechData() {
        val techName = binding.addTechName.text.toString()
        val techSuraName = binding.addTechNameLas.text.toString()
        val techAge = binding.addTechAge.text.toString()
        val techScience = binding.addTechScience.text.toString()
        val techPhoneNumber = binding.addTechPhoneNumber.text.toString()

        val imageRef = storageReference.child("${System.currentTimeMillis()}.jpg")

        imageUri?.let {
            imageRef.putFile(it)
                .addOnSuccessListener { taskSnapshot ->
                    imageRef.downloadUrl.addOnSuccessListener { uri ->
                        val imageUrl = uri.toString()

                        saveDataToDatabase(
                            imageUrl,
                            techName,
                            techSuraName,
                            techAge,
                            techScience,
                            techPhoneNumber
                        )
                        Toast.makeText(this, "Teacher data save", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    // Rasmni yuklashda xatolik yuz berdi
                    Toast.makeText(this, "Teacher data error ${e.message}", Toast.LENGTH_SHORT).show()


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
                TeacherData(imageUrl, techName, techSuraName, techAge, techScience, techPhoneNumber)
            databaseReference.child(key).setValue(data)
        }


    }

}