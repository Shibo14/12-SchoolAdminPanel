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
    private lateinit var imgPdfBtn: ImageView
    lateinit var dialogTitle: EditText
    lateinit var dialogDescription: EditText
    lateinit var dialogCategory: EditText

    private var uploadPdfDocument: String? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addLibrary.setOnClickListener {
          uploadFireBaseData()
        }


    }



    @SuppressLint("MissingInflatedId")
    private fun uploadFireBaseData() {

        val editDialog = androidx.appcompat.app.AlertDialog.Builder(this)
        val editDialogView = layoutInflater.inflate(R.layout.dialog_add_class_table, null)

        dialogTitle = editDialogView.findViewById<EditText>(R.id.dialog_textTitle)
        dialogDescription = editDialogView.findViewById<EditText>(R.id.dialog_textDesc)
        dialogCategory = editDialogView.findViewById<EditText>(R.id.dialog_textCategory)
        imgPdfBtn = editDialogView.findViewById(R.id.addPdfLibrary)

        imgPdfBtn.setOnClickListener {
            pdfAdd()
        }



        editDialog.setView(editDialogView)
        editDialog.setPositiveButton("Saqlash") { _, _ ->
            val title = dialogTitle.text.toString().trim()
            val description = dialogDescription.text.toString().trim()
            val category = dialogCategory.text.toString().trim()
            val  timestamp = System.currentTimeMillis()
            if (validateForm(title, description, category) && pdfUri != null) {


                val fileName = "Books/$timestamp"
                val sRef = FirebaseStorage.getInstance().getReference(fileName)
                sRef.putFile(pdfUri!!).addOnSuccessListener { taskUri ->
                    val uriTask: Task<Uri> = taskUri.storage.downloadUrl
                    while (!uriTask.isSuccessful) {
                      uploadPdfDocument = "${uriTask.result}"

                    }

                }.addOnFailureListener { e ->
                    Toast.makeText(this, "upload pdf ${e.message}", Toast.LENGTH_SHORT).show()
                }


                val hashMap = HashMap<String, Any>()
                hashMap["id"] = "$timestamp"
                hashMap["title"] = "$title"
                hashMap["description"] = "$description"
                hashMap["category"] = "$category"
                hashMap["pdfUri"] = "$uploadPdfDocument"
                hashMap["timestamp"] = timestamp



                firebaseData.child("$timestamp").setValue(hashMap).addOnSuccessListener {

                    Toast.makeText(this, "Malumotlar qo'shildi.", Toast.LENGTH_SHORT).show()

                }.addOnFailureListener {

                    Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
                }

            }


        }
        editDialog.setNegativeButton("Bekor qilish") { _, _ ->

        }

        val dialog = editDialog.create()
        dialog.show()


    }

    private fun validateForm(title: String, description: String, category: String): Boolean {
        return when {
            TextUtils.isEmpty(title) && !Patterns.EMAIL_ADDRESS.matcher(title).matches() -> {
                dialogTitle.error = "Kitob nomini kiriting"
                false
            }

            TextUtils.isEmpty(description) -> {
                dialogDescription.error = "Kitob haqida ma'lumot kiriting"

                false
            }

            TextUtils.isEmpty(category) -> {
                dialogCategory.error = "Kitob toifasini kiriting"

                false
            }

            else -> {
                dialogTitle.error = null
                dialogDescription.error = null
                dialogCategory.error = null
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