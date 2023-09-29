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

class ReadBookActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReadBookBinding
    private val firebaseData = FirebaseDatabase.getInstance().getReference("Books")

    var id = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReadBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        id = intent.getStringExtra("id")!!

        loadReadData()

    }

    private fun loadReadData() {
        firebaseData.child(id)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val pdfUri = snapshot.child("urlPdf").value
                    loadBookData("$pdfUri")
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ReadBookActivity, "error pdf", Toast.LENGTH_SHORT).show()
                }
            })


    }

    private fun loadBookData(pdfUri: String) {
        binding.readPrg.visibility = View.VISIBLE
        val ref = FirebaseStorage.getInstance().getReferenceFromUrl(pdfUri)
        ref.getBytes(Constants.MAX_PDF_SIZE)
            .addOnSuccessListener { byt ->

                binding.readBook.fromBytes(byt)
                    .swipeHorizontal(false)
                    .load()
                binding.readPrg.visibility = View.GONE

            }
            .addOnFailureListener {
                Toast.makeText(this, "read pdf error", Toast.LENGTH_SHORT).show()
            }

    }

    object Constants {
        const val MAX_PDF_SIZE: Long = 35000000
    }


}