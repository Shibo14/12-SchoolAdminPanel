package uz.abboskhan.a12_schooladminpanel.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import uz.abboskhan.a12_schooladminpanel.R
import uz.abboskhan.a12_schooladminpanel.databinding.ActivityAddNotificationBinding
import uz.abboskhan.a12_schooladminpanel.model.Progressbar

class AddNotificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNotificationBinding
    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("NotificationData")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnNotification.setOnClickListener {

            getNotificationData()
        }

    }

    private fun getNotificationData() {
        binding.prgAddNotification.visibility = View.VISIBLE
        val title = binding.notificationTextTitle.text.toString()
        val description = binding.notificationTextDesc.text.toString()
   if (TextUtils.isEmpty(title)){
       binding.notificationTextTitle.error = "Bildirishnoma Qo'shilmadi"
   }else if (TextUtils.isEmpty(description)){
       binding.notificationTextDesc.error = "Bildirishnoma tavsifi kiritilmadi"
   }else{
        getNotificationDataFireBase(title,description)
   }


    }

    private fun getNotificationDataFireBase(title: String, description: String) {

        val timestamp = System.currentTimeMillis()
   val myPrg = Progressbar(this)
        myPrg.startDialog()
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["id"] = "$timestamp"
        hashMap["title"] = title
        hashMap["description"] = description
        hashMap["timestamp"] = timestamp


        databaseReference.child("$timestamp").setValue(hashMap)
            .addOnSuccessListener {

                onBackPressed()
                myPrg.dismissProgressBar()
                Toast.makeText(this, "getNotificationData success", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                myPrg.dismissProgressBar()
                onBackPressed()
                Toast.makeText(this, "Error getNotificationData", Toast.LENGTH_SHORT).show()

            }

    }
}
