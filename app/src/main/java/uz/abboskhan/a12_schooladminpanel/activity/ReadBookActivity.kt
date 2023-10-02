package uz.abboskhan.a12_schooladminpanel.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import uz.abboskhan.a12_schooladminpanel.R
import uz.abboskhan.a12_schooladminpanel.databinding.ActivityReadBookBinding
import uz.abboskhan.a12_schooladminpanel.model.LibraryData

class ReadBookActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReadBookBinding

    var id =""
    var title =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReadBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        id =intent.getStringExtra("id")!!
        title =intent.getStringExtra("title")!!

        loadReadData()

    }

    private fun loadReadData() {
        val firebaseData = FirebaseDatabase.getInstance().getReference("BooksData")

        firebaseData.child(title)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    println("Id= $title")
                    val urlPdf = snapshot.child("urlPdf").value.toString()
                    println("Pdf= $urlPdf")

                        loadBookData("$urlPdf")

                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ReadBookActivity, "error pdf", Toast.LENGTH_SHORT).show()
                }
            })


    }

    private fun loadBookData(urlPdf: String) {
        binding.prgReadBook.visibility = View.VISIBLE
//        val ref = FirebaseStorage.getInstance().getReferenceFromUrl(urlPdf)
        val storage = FirebaseStorage.getInstance()  //getReferenceFromUrl
        val reference = storage.getReferenceFromUrl(urlPdf)
        reference.getBytes(Constants.MAX_PDF_SIZE)
            .addOnSuccessListener { byt ->

                binding.pdfRead.fromBytes(byt)
                    .swipeHorizontal(true)
                    .onError { e->
                        Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()

                    }
                    .load()
                binding.prgReadBook.visibility = View.GONE

            }
            .addOnFailureListener {
                Toast.makeText(this, "read pdf error", Toast.LENGTH_SHORT).show()
            }

    }

    object Constants {
        const val MAX_PDF_SIZE: Long = 35000000
    }


}