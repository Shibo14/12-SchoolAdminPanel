package uz.abboskhan.a12_schooladminpanel.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.github.barteksc.pdfviewer.PDFView
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import uz.abboskhan.a12_schooladminpanel.activity.ReadBookActivity
import uz.abboskhan.a12_schooladminpanel.databinding.RewLibraryBinding
import uz.abboskhan.a12_schooladminpanel.model.LibraryData
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class LibraryAdapter(var mLibraryList: List<LibraryData>, private val c: Context) :
    RecyclerView.Adapter<LibraryAdapter.LibraryViewHolder>() {
    object Constants {
        const val MAX_PDF_SIZE: Long = 100000000
    }


    inner class LibraryViewHolder(private var binding: RewLibraryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val title = binding.pdfItmTitle
        val description = binding.pdfItmDesc
        val btnMore = binding.pdfItmMore

        val pdfView = binding.pdfView

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibraryViewHolder {
        val binding =
            RewLibraryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LibraryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return mLibraryList.size
    }

    override fun onBindViewHolder(holder: LibraryViewHolder, position: Int) {
        val currentItem = mLibraryList[position]
        val id = currentItem.id
        val title = currentItem.title
        val urlPdf = currentItem.urlPdf
        val imageUrlPdf = currentItem.imageUrlPdf
        val time = currentItem.timesTamp

        Picasso.get().load(imageUrlPdf).into(holder.pdfView)

        val uzTimeZone = TimeZone.getTimeZone("Asia/Tashkent") // O'zbekiston vaqti
        val calendar = Calendar.getInstance(uzTimeZone)
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()) //time HH:mm:ss

        val uzTime = sdf.format(calendar.time)
        holder.description.text = uzTime
        holder.title.text = title


        //  loadLibraryData(null, holder.pdfView, urlPdf, title, description, holder.progressBar)

        holder.itemView.setOnClickListener {
            val i = Intent(c, ReadBookActivity::class.java)
            i.putExtra("id", id)
            i.putExtra("title", title)

            // i.putExtra("pdf", urlPdf)

            c.startActivity(i)

        }
        holder.btnMore.setOnClickListener {
            val builder = AlertDialog.Builder(c)
            builder.setTitle("Delete")
                .setMessage("delete book ")
                .setPositiveButton("yes") { _, _ ->

                    bookDelete(title, urlPdf,imageUrlPdf)
                }
                .setNegativeButton("no") { d, _ ->

                    d.dismiss()
                }
                .create()
                .show()

        }


    }

    private fun bookDelete(id: String, urlPdf: String,imageUrlPdf:String) {

        val ref1 = FirebaseStorage.getInstance().getReferenceFromUrl(urlPdf)
        val ref2 = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrlPdf)

        ref1.delete()
            .addOnSuccessListener {
                ref2.delete().addOnSuccessListener {
                    val firebaseData = FirebaseDatabase.getInstance().getReference("BooksData")

                    firebaseData.child(id)
                   .removeValue()
                        .addOnSuccessListener {
                            Toast.makeText(c, "success delete", Toast.LENGTH_SHORT).show()

                        }
                        .addOnFailureListener {
                            Toast.makeText(c, "delete error", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener {
                Toast.makeText(c, "delete error", Toast.LENGTH_SHORT).show()

            }
    }

    /*
        private fun loadLibraryData(
            pagesTv: TextView?,
            pdfView: PDFView,
            urlPdf: String,
            title: String,
            description: String,
            progressBar: LottieAnimationView
        ) {
            val ref = FirebaseStorage.getInstance().getReferenceFromUrl(urlPdf)
            ref.getBytes(Constants.MAX_PDF_SIZE)
                .addOnSuccessListener { bytes ->
                    pdfView.fromBytes(bytes)
                        .pages(0)
                        .spacing(0)
                        .swipeHorizontal(false)
                        .enableSwipe(false)
                        .onError { t ->
                            // Xato yuzaga kelganda ishlatiladigan kod
                            t.printStackTrace() // Xato haqida ma'lumotni ko'rish uchun
                            Log.e("onError", "PDF onError: ${t.printStackTrace()}")
                            println("PDF xatoligi onError: ${t.message}")
                            progressBar.visibility = View.INVISIBLE
                        }
                        .onPageError { page, t ->
                            // PDF sahifalardan biri yuklanmaganida ishlatiladigan kod
                            t.printStackTrace() // Xato haqida ma'lumotni ko'rish uchun
                            Log.e("onPageError", "PDF onPageError: ${t.message}")
                            println("PDF xatoligi onPageError: ${t.message}")
                            progressBar.visibility = View.INVISIBLE
                        }
                        .onLoad { nbPages ->
                            // PDF yuklandi
                            progressBar.visibility = View.INVISIBLE

                                if (null != pagesTv) {
                                    pagesTv.text = "$nbPages"
                                }
                                Log.e("nbPages", "PDF nbPages : $nbPages")
                                println("nbPages =  $nbPages")


                        }
                        .load()
                }
                .addOnFailureListener { exception ->
                    // Fayl yuklashda xatolik yuzaga keldi
                    exception.printStackTrace() // Xato haqida ma'lumotni ko'rish uchun
                }
        }

    */


}