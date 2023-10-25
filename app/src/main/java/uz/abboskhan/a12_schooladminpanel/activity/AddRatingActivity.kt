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
import uz.abboskhan.a12_schooladminpanel.R
import uz.abboskhan.a12_schooladminpanel.databinding.ActivityAddClassBinding
import uz.abboskhan.a12_schooladminpanel.databinding.ActivityAddNotificationBinding
import uz.abboskhan.a12_schooladminpanel.databinding.ActivityAddRatingBinding
import uz.abboskhan.a12_schooladminpanel.databinding.ActivityAddTechBinding
import uz.abboskhan.a12_schooladminpanel.model.Progressbar
import uz.abboskhan.a12_schooladminpanel.model.TeacherData

class AddRatingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddRatingBinding
    private val storageReference: StorageReference =
        FirebaseStorage.getInstance().getReference("RatingImages")
    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("RatingData")
    private var imageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddRatingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addStudentBtn.setOnClickListener {
            getStudentData()
        }
        binding.addRAtingImg.setOnClickListener {
            resultLauncher.launch("image/*")
        }


    }

    private fun getStudentData() {


            val studentName = binding.addStudentName.text.toString()
            val studentSuraName = binding.addStuNameLas.text.toString()
            val studentAge = binding.addStudentAge.text.toString()
            val studentScience = binding.addStudentScience.text.toString()
            val studentPhoneNumber = binding.stdClassNumber.text.toString()

        if (TextUtils.isEmpty(studentSuraName)){
            binding.addStuNameLas.error = "Familyani kiriting"
        }else if (TextUtils.isEmpty(studentName)){
            binding.addStudentName.error = "Isimni kiriting"
        }
        else if (TextUtils.isEmpty(studentAge)){
            binding.addStudentAge.error = "Yoshni kiriting"
        }
        else if (TextUtils.isEmpty(studentScience)){
            binding.addStudentScience.error = "Fanni kiriting"
        }
        else if (TextUtils.isEmpty(studentPhoneNumber)){
            binding.stdClassNumber.error = "Sinfni kiriting"
        }
        else if (imageUri==null){
            Toast.makeText(this, "Rasm tanlanmagan", Toast.LENGTH_SHORT).show()
        }else{
            getRatingData(studentSuraName,studentName,studentAge,studentPhoneNumber,studentScience)
        }


        }

    private fun getRatingData(studentSuraName: String, studentName: String, studentAge: String, studentScience: String, studentPhoneNumber: String) {
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
                            studentName,
                            studentSuraName,
                            studentAge,
                            studentPhoneNumber,
                            studentScience
                        )
                   myPrg.dismissProgressBar()
                        onBackPressed()
                        Toast.makeText(this, "Student data save", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->

                    myPrg.dismissProgressBar()

                    // Rasmni yuklashda xatolik yuz berdi
                    Toast.makeText(this, "Student data error ${e.message}", Toast.LENGTH_SHORT)
                        .show()


                }


        }
    }

    private val resultLauncher = registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) {

            imageUri = it
            binding.addRAtingImg.setImageURI(it)

        }

        private fun saveDataToDatabase(
            id: String,
            timestamp: Long,
            imageUrl: String,
            studentName: String,
            studentSuraName: String,
            studentAge: String,
            studentScience: String,
            studentPhoneNumber: String
        ) {
            val hashMap: HashMap<String, Any> = HashMap()

            hashMap["id"] = id
            hashMap["imageUrl"] = "$imageUrl"
            hashMap["studentName"] = "$studentName"
            hashMap["studentSuraName"] = "$studentSuraName"
            hashMap["studentAge"] = "$studentAge"
            hashMap["studentScience"] = "$studentScience"
            hashMap["studentClassNumber"] = "$studentPhoneNumber"

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