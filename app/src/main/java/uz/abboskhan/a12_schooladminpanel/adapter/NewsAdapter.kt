package uz.abboskhan.a12_schooladminpanel.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import uz.abboskhan.a12_schooladminpanel.activity.NewsInfo
import uz.abboskhan.a12_schooladminpanel.databinding.RewNewsBinding
import uz.abboskhan.a12_schooladminpanel.model.NewsData
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class NewsAdapter(private val mList: ArrayList<NewsData>, val context: Context) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    inner class NewsViewHolder(private var binding: RewNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {
   val btnDelete = binding.newsDelete
        val newsDate = binding.timeNews
        val newsTime = binding.textDate
        fun bing(data: NewsData) {

            Picasso.get().load(data.imageUrl).into(binding.itemImg)
            // binding.itmNewsDesc.setTrimExpandedText(" :more")
            binding.itemNewstxt.text = data.title
            binding.itmNewsDesc.text = data.description
           binding.itmNewsDesc.setTrimExpandedText("")

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = RewNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentItem = mList[position]
       val id = currentItem.id
       val title = currentItem.title
       val description = currentItem.description
       val time = currentItem.timestamp
       val image = currentItem.imageUrl
        holder.bing(currentItem)


        val uzbekistanTimeZone = TimeZone.getTimeZone("Asia/Tashkent")


        val getNewsDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val getNewsTime = SimpleDateFormat("HH : mm", Locale.getDefault())

        getNewsDate.timeZone = uzbekistanTimeZone


        val newsDate = getNewsDate.format(Date(time))
        val newsTime = getNewsTime.format(Date(time))
        holder.newsDate.text = newsDate
        holder.newsTime.text = newsTime


        holder.itemView.setOnClickListener {
            val i  = Intent(context, NewsInfo::class.java)
            i.putExtra("id",id)
            i.putExtra("title",title)
            i.putExtra("description",description)
            i.putExtra("date",newsDate)
            i.putExtra("time",newsTime)
            i.putExtra("image",image)
            context.startActivity(i)

        }

        holder.btnDelete.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Delete")
                .setMessage("delete news ")
                .setPositiveButton("yes") { _, _ ->

                    newsDelete(id, image)
                }
                .setNegativeButton("no") { d, _ ->

                    d.dismiss()
                }
                .create()
                .show()

        }


    }
    override fun getItemCount(): Int {
        return mList.size
    }

    private fun newsDelete(id: String, urlImage:String) {
        val ref = FirebaseStorage.getInstance().getReferenceFromUrl(urlImage)

        ref.delete()
            .addOnSuccessListener {

                    val firebaseData = FirebaseDatabase.getInstance().getReference("NewsData")

                    firebaseData.child(id)
                        .removeValue()
                        .addOnSuccessListener {
                            Toast.makeText(context, "success delete", Toast.LENGTH_SHORT).show()

                        }
                        .addOnFailureListener {e->
                            Toast.makeText(context, "delete error: ${e.message}", Toast.LENGTH_SHORT).show()
                        }

            }
            .addOnFailureListener {e->
                Toast.makeText(context, "delete error:${e.message}", Toast.LENGTH_SHORT).show()

            }
    }





}