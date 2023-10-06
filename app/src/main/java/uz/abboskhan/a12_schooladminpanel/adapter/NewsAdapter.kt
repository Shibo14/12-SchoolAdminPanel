package uz.abboskhan.a12_schooladminpanel.adapter

import android.content.Context
import android.text.Spannable
import android.text.style.ImageSpan

import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import uz.abboskhan.a12_schooladminpanel.R
import uz.abboskhan.a12_schooladminpanel.databinding.RewNewsBinding
import uz.abboskhan.a12_schooladminpanel.model.TeacherData
import uz.abboskhan.a12_schooladminpanel.databinding.RewTeacherBinding
import uz.abboskhan.a12_schooladminpanel.model.NewsData

class NewsAdapter(private val mList: List<NewsData>, val context: Context): RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    inner class NewsViewHolder(private var binding: RewNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bing(data: NewsData) {


            val newText = "Yangi matn bu yerga keladi."
            val icon = ContextCompat.getDrawable(context, R.drawable.baseline_more_vert_24) // Ikon resursni olish

// Matnni va ikonni birlashtirish uchun SpannableStringBuilder obyektni yaratamiz
            val spannableStringBuilder = SpannableStringBuilder(data.description)

// Ikoni Spannable obyektda joylashtiramiz
            icon?.let {
                it.setBounds(0, 0, it.intrinsicWidth, it.intrinsicHeight)
                val imageSpan = ImageSpan(it, ImageSpan.ALIGN_BOTTOM)
                spannableStringBuilder.setSpan(imageSpan, newText.length, newText.length + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
            }

            binding.itmNewsDesc.text = spannableStringBuilder






            Picasso.get().load(data.imageUrl).into(binding.itemImg)
          // binding.itmNewsDesc.setTrimExpandedText(" :more")
            binding.itemNewstxt.text = data.title
            binding.itmNewsDesc.text = data.description


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = RewNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentItem = mList[position]

        holder.bing(currentItem)

//        holder.itemView.setOnClickListener {
//            val i  = Intent(context, MainActivity::class.java)
//            i.putExtra("country",nameTeacher)
//            context.startActivity(i)
//
//        }



    }

    override fun getItemCount(): Int {
        return mList.size
    }


}