package uz.abboskhan.a12_schooladminpanel.activity

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import uz.abboskhan.a12_schooladminpanel.model.TeacherData
import uz.abboskhan.a12_schooladminpanel.databinding.ActivityAddTechBinding
import uz.abboskhan.a12_schooladminpanel.model.Progressbar

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

        val techName = binding.addTechName.text.toString()
        val techSuraName = binding.addTechNameLas.text.toString()
        val techAge = binding.addTechAge.text.toString()
        val techScience = binding.addTechScience.text.toString()
        val techPhoneNumber = binding.addTechPhoneNumber.text.toString()
        val timestamp = System.currentTimeMillis()
        val id = "$timestamp"
        if (TextUtils.isEmpty(techSuraName)) {
            binding.addTechNameLas.error = "Familyani kiriting"
        } else if (TextUtils.isEmpty(techName)) {
            binding.addTechName.error = "Isimni kiriting"
        } else if (TextUtils.isEmpty(techAge)) {
            binding.addTechAge.error = "Yoshni kiriting"
        } else if (TextUtils.isEmpty(techScience)) {
            binding.addTechScience.error = "Fanni kiriting"
        } else if (TextUtils.isEmpty(techPhoneNumber)) {
            binding.addTechPhoneNumber.error = "Telefon raqamini kiriting"
        } else if (imageUri == null) {
            Toast.makeText(this, "Rasm tanlanmagan", Toast.LENGTH_SHORT).show()
        } else {
            getTeacherFirebaseData(id,timestamp,techSuraName, techName,techAge,  techScience, techPhoneNumber)
        }


    }

    private fun getTeacherFirebaseData(
        id: String,
        timestamp:Long,
        techSuraName: String,
        techName: String,
        techAge: String,
        techScience: String,
        techPhoneNumber: String
    ) {
        val myPrg = Progressbar(this)
        myPrg.startDialog()

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
                        myPrg.dismissProgressBar()
                        onBackPressed()
                        Toast.makeText(this, "Teacher data save", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->

                    myPrg.dismissProgressBar()
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
        val hashMap = HashMap<String, Any>()
        hashMap["id"] = "$timestamp"
        hashMap["techName"] = "$techName"
        hashMap["techSuraName"] = "$techSuraName"
        hashMap["imageUrl"] = "$imageUrl"
        hashMap["techAge"] = "$techAge"
        hashMap["techScience"] = "$techScience"
        hashMap["techPhoneNumber"] = "$techPhoneNumber"

        hashMap["timestamp"] = timestamp



        databaseReference.child("$timestamp")
            .setValue(hashMap)
            .addOnSuccessListener {

                Toast.makeText(this, "Malumotlar qo'shildi.", Toast.LENGTH_SHORT).show()

            }
            .addOnFailureListener {

                Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
            }
    }






    }

