package uz.abboskhan.a12_schooladminpanel.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.squareup.picasso.Picasso
import uz.abboskhan.a12_schooladminpanel.R
import uz.abboskhan.a12_schooladminpanel.databinding.ActivityTeacherInfoBinding

class TeacherInfo : AppCompatActivity() {
    private lateinit var binding :ActivityTeacherInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeacherInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra("TechId")
        val nameTeacher = intent.getStringExtra("name")
        val sureName = intent.getStringExtra("sureName")
        val techScience = intent.getStringExtra("science")
        val techAge = intent.getStringExtra("age")
        val techPhoneNumber = intent.getStringExtra("phone")

        val image = intent.getStringExtra("techImage")

        binding.techSur.text = sureName
        binding.techName.text = nameTeacher
        binding.techFan.text = techScience
        binding.techAge.text = techAge

        val phoneNumber = "+998$techPhoneNumber"
        val formattedPhoneNumber = StringBuilder()
        for (i in phoneNumber.indices) {
            formattedPhoneNumber.append(phoneNumber[i])
            if (i < phoneNumber.length - 1) {
                formattedPhoneNumber.append("\u002D")
            }
        }
        binding.techPhone.text  = formattedPhoneNumber.toString()

        binding.techPhone.setOnClickListener {
            val number = binding.techPhone.text.toString()
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number"))
            startActivity(intent)
        }

        Picasso.get().load(image).into(binding.techInfoImg)

    }
}