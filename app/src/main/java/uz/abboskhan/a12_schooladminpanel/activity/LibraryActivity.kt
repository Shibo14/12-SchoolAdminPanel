package uz.abboskhan.a12_schooladminpanel.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import uz.abboskhan.a12_schooladminpanel.databinding.ActivityLibraryBinding

class LibraryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLibraryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.addLibrary.setOnClickListener {
            startActivity(Intent(this, AddLibraryActivity::class.java))
        }



    }
}