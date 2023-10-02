package uz.abboskhan.a12_schooladminpanel.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.tasks.Task
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import uz.abboskhan.a12_schooladminpanel.model.LibraryData
import uz.abboskhan.a12_schooladminpanel.databinding.ActivityAddLibraryBinding

class AddLibraryActivity : AppCompatActivity() {
    private val firebaseData = FirebaseDatabase.getInstance().getReference("BooksData")
    private lateinit var binding: ActivityAddLibraryBinding
    private var pdfUri: Uri? = null
    private lateinit var titleBook: String
    private lateinit var description: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSavePdf.setOnClickListener {
            uploadFireBaseData()

        }
        binding.addPdfLibrary.setOnClickListener {
            pdfAdd()
        }


    }


    @SuppressLint("MissingInflatedId")
    private fun uploadFireBaseData() {

        titleBook = binding.dialogTextTitle.text.toString().trim()
        description = binding.dialogTextDesc.text.toString().trim()

        val timestamp = System.currentTimeMillis()
        val id = timestamp.toString()
        if (validateForm(titleBook, description) && pdfUri != null) {

            val fileName = "BooksPdf/$timestamp"
            val sRef = FirebaseStorage.getInstance().getReference(fileName)

            sRef.putFile(pdfUri!!)
                .addOnSuccessListener { thk ->
                    val uriTask: Task<Uri> = thk.storage.downloadUrl
                    while (!uriTask.isSuccessful);
                    val uploadPdfUri = "${uriTask.result}"
                    saveDataToDatabase(uploadPdfUri, timestamp, id)
                    Toast.makeText(this, "pdf upload successful", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    // Rasmni yuklashda xatolik yuz berdi
                    Toast.makeText(this, "Book ${e.message}", Toast.LENGTH_SHORT)
                        .show()

                }


        }


    }

    private fun saveDataToDatabase(uploadPdfUri: String, timestamp: Long, id: String) {

        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["id"] = id
        hashMap["title"] = "$titleBook"
        hashMap["urlPdf"] = "$uploadPdfUri"
        hashMap["description"] = "$description"
        hashMap["timestamp"] = timestamp

        firebaseData.child("$titleBook")
            .setValue(hashMap)
            .addOnSuccessListener {
                Toast.makeText(this, "pdf data upload successful", Toast.LENGTH_SHORT).show()
                pdfUri = null
            }
            .addOnFailureListener {
                Toast.makeText(this, "pdf data upload error", Toast.LENGTH_SHORT).show()

            }

    }


    private fun validateForm(title: String, description: String): Boolean {
        return when {
            TextUtils.isEmpty(title) && !Patterns.EMAIL_ADDRESS.matcher(title).matches() -> {
                binding.dialogTextTitle.error = "Kitob nomini kiriting"
                false
            }

            TextUtils.isEmpty(description) -> {
                binding.dialogTextDesc.error = "Kitob haqida ma'lumot kiriting"

                false
            }


            else -> {
                binding.dialogTextTitle.error = null
                binding.dialogTextDesc.error = null

                true
            }
        }
    }


    private fun pdfAdd() {
        val i = Intent()
        i.type = "application/pdf"
        i.action = Intent.ACTION_GET_CONTENT
        pdfActivityResultLauncher.launch(i)
    }


    private var pdfActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback { result ->
                if (result.resultCode == RESULT_OK) {
                    pdfUri = result.data!!.data

                } else {
                    Toast.makeText(this, "pdf error", Toast.LENGTH_SHORT).show()
                }

            })


}