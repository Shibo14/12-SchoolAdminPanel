package uz.abboskhan.a12_schooladminpanel.activity

import android.app.Activity
import android.icu.text.CaseMap.Title
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
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
import uz.abboskhan.a12_schooladminpanel.model.Progressbar
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

        val title = binding.dialogTextTitle.text.toString()
        val description = binding.dialogTextDesc.text.toString()
        if (TextUtils.isEmpty(title)) {
            binding.dialogTextTitle.error = "Yangiliklar Sarlavhasi kiriting"

        } else if ( TextUtils.isEmpty(description)) {

            binding.dialogTextDesc.error = "Yangiliklar tavsifi kiriting"
        } else if (imageUri == null) {
            Toast.makeText(this, "Rasm tanlanmagan", Toast.LENGTH_SHORT).show()
        } else {
            binding.dialogTextTitle.error = null
            binding.dialogTextDesc.error = null
            getNewsDataFireBase(title,description)


        }
    }

    private fun getNewsDataFireBase(title: String, description: String) {
        val myPrg = Progressbar(this)
        myPrg.startDialog()

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
                        myPrg.dismissProgressBar()

                        onBackPressed()
                        Toast.makeText(this, "News data save", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    myPrg.dismissProgressBar()

                    // Rasmni yuklashda xatolik yuz berdi
                    Toast.makeText(this, "News data error ${e.message}", Toast.LENGTH_SHORT)
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
        val hashMap: HashMap<String, Any> = HashMap()

        hashMap["id"] = id
        hashMap["title"] = title
        hashMap["imageUrl"] = imageUrl
        hashMap["description"] = description
        hashMap["timestamp"] = timestamp

        databaseReference.child(id)
            .setValue(hashMap)
            .addOnSuccessListener {
                Toast.makeText(this, "pdf data upload successful", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "pdf data upload error", Toast.LENGTH_SHORT).show()

            }


    }



}