package uz.abboskhan.a12_schooladminpanel.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import uz.abboskhan.a12_schooladminpanel.activity.NewsInfo
import uz.abboskhan.a12_schooladminpanel.databinding.RewNewsBinding
import uz.abboskhan.a12_schooladminpanel.databinding.RewNotificationBinding
import uz.abboskhan.a12_schooladminpanel.model.NewsData
import uz.abboskhan.a12_schooladminpanel.model.NotificationData
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class NotificationAdapter(private val mList: List<NotificationData>, val context: Context) :
    RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    inner class NotificationViewHolder(private var binding: RewNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val btnDelete = binding.notiDelete
        val noteData = binding.notDate
        val noteTime = binding.noteTime
        fun bing(data: NotificationData) {


            // binding.itmNewsDesc.setTrimExpandedText(" :more")
            binding.itemNotiTxt.text = data.title
            binding.itmNotiDesc.text = data.description


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding =
            RewNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val currentItem = mList[position]
        val id = currentItem.id
        val title = currentItem.title
        val description = currentItem.description
        val time = currentItem.timestamp

        holder.bing(currentItem)

        /*
          val uzbekistanTimeZone = TimeZone.getTimeZone("Asia/Tashkent")


        val getNewsDate = SimpleDateFormat("EEEE d-MMMM-yyyy", Locale.getDefault())
        val getNewsTime = SimpleDateFormat("EEEE d-MMMM-yyyy", Locale.getDefault())

        getNewsDate.timeZone = uzbekistanTimeZone


        val newsDate = getNewsDate.format(Date(time))
        val newsTime = getNewsTime.format(Date(time))
         */

        val uzbekistanTimeZone = TimeZone.getTimeZone("Asia/Tashkent")


        val sdf1 = SimpleDateFormat("EEEE d-MMMM-yyyy", Locale.getDefault())
        val sdf2 = SimpleDateFormat("HH:mm", Locale.getDefault())

        sdf1.timeZone = uzbekistanTimeZone
        sdf2.timeZone = uzbekistanTimeZone


        val newsDate = sdf1.format(Date(time))
        val newsTime = sdf2.format(Date(time))

        holder.noteData.text = newsDate
        holder.noteTime.text = newsTime


        holder.btnDelete.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Delete")
                .setMessage("delete news ")
                .setPositiveButton("yes") { _, _ ->

                    newsDelete(id)
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

    private fun newsDelete(id: String) {

        val firebaseData = FirebaseDatabase.getInstance().getReference("NotificationData")

        firebaseData.child(id)
            .removeValue()
            .addOnSuccessListener {
                Toast.makeText(context, "success delete", Toast.LENGTH_SHORT).show()

            }
            .addOnFailureListener {
                Toast.makeText(context, "delete error", Toast.LENGTH_SHORT).show()
            }

    }

}





