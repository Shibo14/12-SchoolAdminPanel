package uz.abboskhan.a12_schooladminpanel.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.tasks.Task
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import uz.abboskhan.a12_schooladminpanel.databinding.ActivityAddLibraryBinding
import uz.abboskhan.a12_schooladminpanel.model.Progressbar

class AddLibraryActivity : AppCompatActivity() {
    private val firebaseData = FirebaseDatabase.getInstance().getReference("BooksData")
    private lateinit var binding: ActivityAddLibraryBinding
    private var pdfUri: Uri? = null
    private lateinit var titleBook: String
    private var imageUri: Uri? = null
    private var uploadPdfUri = ""
    private var imageUrlPdf = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSavePdf.setOnClickListener {

            uploadFireBaseData()

        }
        binding.addPdfImg.setOnClickListener {
            resultLauncher.launch("image/*")
        }

        binding.addPdfLibrary.setOnClickListener {
            pdfAdd()

        }


    }


    @SuppressLint("MissingInflatedId", "SuspiciousIndentation")
    private fun uploadFireBaseData() {
        titleBook = binding.dialogTextTitle.text.toString().trim()

        if ( TextUtils.isEmpty(title)) {
            binding.dialogTextTitle.error = "Kitob nomini kiriting"
        } else if (null == pdfUri) {
            Toast.makeText(this, "pdf qo'shing", Toast.LENGTH_SHORT).show()
        } else if (null == imageUri) {
            Toast.makeText(this, "Kitob rasmni qo'shing", Toast.LENGTH_SHORT).show()

        } else {

            saveData(titleBook)

        }


    }

    private fun saveData(titleBook: String) {
        val myPrg = Progressbar(this)
        myPrg.startDialog()

        val timestamp = System.currentTimeMillis()
        val id = timestamp.toString()
        val fileName = "BooksPdf/$timestamp"
        val sRef = FirebaseStorage.getInstance().getReference(fileName)
        sRef.putFile(pdfUri!!)
            .addOnSuccessListener { thk ->
                val uriTask: Task<Uri> = thk.storage.downloadUrl
                while (!uriTask.isSuccessful);
                uploadPdfUri = "${uriTask.result}"
                val storageReference: StorageReference =
                    FirebaseStorage.getInstance().getReference("BookImages")

                val imageRef = storageReference.child("${System.currentTimeMillis()}.jpg")

                imageUri?.let {
                    imageRef.putFile(it)
                        .addOnSuccessListener { taskSnapshot ->
                            imageRef.downloadUrl.addOnSuccessListener { uri ->
                                imageUrlPdf = uri.toString()
                                saveDataToDatabase(titleBook,uploadPdfUri, timestamp, id, imageUrlPdf)
                                onBackPressed()
                                myPrg.dismissProgressBar()
                                Toast.makeText(this, "Teacher data save", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .addOnFailureListener { e ->
                            myPrg.dismissProgressBar()
                            // Rasmni yuklashda xatolik yuz berdi
                            Toast.makeText(
                                this,
                                "Teacher data error ${e.message}",
                                Toast.LENGTH_SHORT
                            )
                                .show()

                        }


                }


                myPrg.dismissProgressBar()
                Toast.makeText(this, "pdf upload successful", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                myPrg.dismissProgressBar()

                // Rasmni yuklashda xatolik yuz berdi
                Toast.makeText(this, "Book ${e.message}", Toast.LENGTH_SHORT)
                    .show()

            }
    }


    private fun saveDataToDatabase(
        titleBook: String,
        uploadPdfUri: String,
        timestamp: Long,
        id: String,
        imageUrlPdf: String
    ) {

        val hashMap: HashMap<String, Any> = HashMap()
        println("imageUrlPdf = $imageUrlPdf")
        hashMap["id"] = id
        hashMap["title"] = "$titleBook"
        hashMap["urlPdf"] = "$uploadPdfUri"
        hashMap["imageUrlPdf"] = "$imageUrlPdf"
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


    private fun pdfAdd() {
        val i = Intent()
        i.type = "application/pdf"
        i.action = Intent.ACTION_GET_CONTENT
        pdfActivityResultLauncher.launch(i)





        binding.imgAddPdf.visibility = View.GONE
    }

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {

        imageUri = it
        binding.addPdfImg.setImageURI(it)

    }

    private var pdfActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback { result ->
                if (result.resultCode == RESULT_OK) {
                    pdfUri = result.data!!.data


                    if (pdfUri != null) {
                        binding.addPdfLibrary.fromUri(pdfUri)
                            .pages(0)
                            .spacing(0)
                            .swipeHorizontal(false)
                            .enableSwipe(false)
                            .onError { t ->
                                // Xatolik yuzaga kelganda ishlatiladigan kod
                                t.printStackTrace() // Xato haqida ma'lumotni ko'rish uchun
                                Log.e("onError", "PDF onError: ${t.message}")
                            }
                            .onPageError { page, t ->
                                // PDF sahifalardan biri yuklanmaganida ishlatiladigan kod
                                t.printStackTrace() // Xato haqida ma'lumotni ko'rish uchun
                                Log.e("onPageError", "PDF onPageError: ${t.message}")
                            }
                            .onLoad { nbPages ->
                                // PDF yuklandi
                            }
                            .load()
                    } else {
                        // PDF Uri null bo'lgan holatda, foydalanuvchiga xabar berish
                        Toast.makeText(this, "PDF fayl topilmadi", Toast.LENGTH_SHORT).show()
                    }

                }


            })
}