package uz.abboskhan.a12_schooladminpanel.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.squareup.picasso.Picasso
import uz.abboskhan.a12_schooladminpanel.R
import uz.abboskhan.a12_schooladminpanel.databinding.ActivityNewsInfoBinding

class NewsInfo : AppCompatActivity() {
    private lateinit var binding:ActivityNewsInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
     val id = intent.getStringExtra("id")
     val title = intent.getStringExtra("title")
     val description = intent.getStringExtra("description")
     val time = intent.getStringExtra("time")
     val date = intent.getStringExtra("date")

     val image = intent.getStringExtra("image")

        binding.newsDate.text = date
     //   binding.newsTime.text = time

        binding.newsTitle.text = title
        binding.newsDesc.text = description
        Picasso.get().load(image).into(binding.newsImg)




    }
}