package uz.abboskhan.a12_schooladminpanel.activity

import android.app.Activity
import android.icu.text.CaseMap.Title
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
import uz.abboskhan.a12_schooladminpanel.R
import uz.abboskhan.a12_schooladminpanel.databinding.ActivityAddNewsBinding
import uz.abboskhan.a12_schooladminpanel.databinding.ActivityAddTechBinding
import uz.abboskhan.a12_schooladminpanel.model.NewsData
import uz.abboskhan.a12_schooladminpanel.model.TeacherData

class AddNewsActivity : AppCompatActivity() {
    private lateinit var binding:ActivityAddNewsBinding

    private val storageReference: StorageReference =
        FirebaseStorage.getInstance().getReference("NewsImages")
    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("NewsData")
    private var imageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSaveNews.setOnClickListener {

            getNewsData()
        }
        binding.addNewsImg.setOnClickListener {
            resultLauncher.launch("image/*")
        }
    }

    private fun getNewsData() {
        binding.prgAddNews.visibility = View.VISIBLE
        val title = binding.dialogTextTitle.text.toString()
        val description = binding.dialogTextDesc.text.toString()

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
                            title,
                            description
                        )
                        binding.prgAddNews.visibility = View.GONE
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
        binding.addNewsImg.setImageURI(it)

    }

    private fun saveDataToDatabase(
        id: String,
        timestamp: Long,
        imageUrl: String,
        title: String,
        description: String,

    ) {
        val key = databaseReference.push().key

        if (key != null) {
            val data =
                NewsData(id,timestamp,imageUrl, title, description)
            databaseReference.child(key).setValue(data)
        }


    }



}