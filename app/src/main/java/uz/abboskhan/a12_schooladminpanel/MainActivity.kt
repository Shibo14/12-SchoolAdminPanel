package uz.abboskhan.a12_schooladminpanel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import uz.abboskhan.a12_schooladminpanel.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.classInfo.setOnClickListener {

            startActivity(Intent(this, AddClassActivity::class.java))
        }
        binding.teacher.setOnClickListener {

            startActivity(Intent(this, AddTechActivity::class.java))

        }
        binding.library.setOnClickListener {

            startActivity(Intent(this, AddLibraryActivity::class.java))

        }



    }
}