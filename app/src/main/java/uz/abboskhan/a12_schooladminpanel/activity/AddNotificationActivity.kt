package uz.abboskhan.a12_schooladminpanel.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import uz.abboskhan.a12_schooladminpanel.R
import uz.abboskhan.a12_schooladminpanel.databinding.ActivityAddNotificationBinding

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

        val timestamp = System.currentTimeMillis()

        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["id"] = "$timestamp"
        hashMap["title"] = title
        hashMap["description"] = description
        hashMap["timestamp"] = timestamp


        databaseReference.child("$timestamp").setValue(hashMap)
            .addOnSuccessListener {
                binding.prgAddNotification.visibility = View.GONE
                onBackPressed()
                Toast.makeText(this, "getNotificationData success", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                binding.prgAddNotification.visibility = View.GONE
                onBackPressed()
                Toast.makeText(this, "Error getNotificationData", Toast.LENGTH_SHORT).show()

            }


    }
}
