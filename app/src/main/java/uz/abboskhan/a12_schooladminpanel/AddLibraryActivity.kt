package uz.abboskhan.a12_schooladminpanel

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Instrumentation.ActivityResult
import android.content.ClipDescription
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.tasks.Task
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import uz.abboskhan.a12_schooladminpanel.databinding.ActivityAddLibraryBinding
import uz.abboskhan.a12_schooladminpanel.databinding.ActivityClassTableBinding

class AddLibraryActivity : AppCompatActivity() {
    private val firebaseData = FirebaseDatabase.getInstance().getReference("Books")
    private lateinit var binding: ActivityAddLibraryBinding
    private var pdfUri: Uri? = null


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

        val title = binding.dialogTextTitle.text.toString().trim()
        val description = binding.dialogTextDesc.text.toString().trim()
        val category = binding.dialogTextCategory.text.toString().trim()
        val timestamp = System.currentTimeMillis()
        val id = timestamp.toString()
        if (validateForm(title, description, category) && pdfUri != null) {

            val fileName = "Books/$timestamp"
            val sRef = FirebaseStorage.getInstance().getReference(fileName)
            val imageRef = sRef.child("${System.currentTimeMillis()}.pdf")

            pdfUri?.let {
                imageRef.putFile(it)
                    .addOnSuccessListener { _ ->
                        imageRef.downloadUrl.addOnSuccessListener { uri ->
                            val urlPdf = uri.toString()

                            saveDataToDatabase(
                                urlPdf,
                                id,
                                title,
                                description,
                                category,
                                timestamp
                            )
                            startActivity(Intent(this, LibraryActivity::class.java))
                            finish()
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


    }

    private fun saveDataToDatabase(
        urlPdf: String,
        id: String,
        title: String,
        description: String,
        category: String,
        timestamp: Long
    ) {
        val key = firebaseData.push().key

        if (key != null) {
            val data =
                LibraryData(id, title, description, category, timestamp, urlPdf)
            firebaseData.child(key).setValue(data)

        }

    }


    private fun validateForm(title: String, description: String, category: String): Boolean {
        return when {
            TextUtils.isEmpty(title) && !Patterns.EMAIL_ADDRESS.matcher(title).matches() -> {
                binding.dialogTextTitle.error = "Kitob nomini kiriting"
                false
            }

            TextUtils.isEmpty(description) -> {
                binding.dialogTextDesc.error = "Kitob haqida ma'lumot kiriting"

                false
            }

            TextUtils.isEmpty(category) -> {
                binding.dialogTextCategory.error = "Kitob toifasini kiriting"

                false
            }

            else -> {
                binding.dialogTextTitle.error = null
                binding.dialogTextDesc.error = null
                binding.dialogTextCategory.error = null
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