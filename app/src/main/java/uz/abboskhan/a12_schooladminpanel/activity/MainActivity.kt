package uz.abboskhan.a12_schooladminpanel.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsetsController
import uz.abboskhan.a12_schooladminpanel.R
import uz.abboskhan.a12_schooladminpanel.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    @SuppressLint("ObsoleteSdkInt")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            window.insetsController?.setSystemBarsAppearance(0, WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS)
//            window.statusBarColor = resources.getColor(R.color.white, theme)
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            window.statusBarColor = resources.getColor(R.color.white)
//        }

        binding.classInfo.setOnClickListener {

            startActivity(Intent(this, AddClassActivity::class.java))
        }
        binding.teacher.setOnClickListener {

            startActivity(Intent(this, TeacherActivity::class.java))

        }
        binding.library.setOnClickListener {

            startActivity(Intent(this, LibraryActivity::class.java))

        }
        binding.news.setOnClickListener {

            startActivity(Intent(this, NewsActivity::class.java))

        }

   binding.notification.setOnClickListener {

            startActivity(Intent(this, AddNotificationActivity::class.java))

        }

   binding.tournament.setOnClickListener {

            startActivity(Intent(this, RatingActivity::class.java))

        }
          binding.notiInfo.setOnClickListener {

            startActivity(Intent(this, NotificationActivity::class.java))

        }



    }
}