package uz.abboskhan.a12_schooladminpanel.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.github.barteksc.pdfviewer.PDFView
import com.google.firebase.storage.FirebaseStorage
import uz.abboskhan.a12_schooladminpanel.databinding.RewLibraryBinding
import uz.abboskhan.a12_schooladminpanel.filter.FilterLibrary
import uz.abboskhan.a12_schooladminpanel.model.LibraryData

class LibraryAdapter(var mLibraryList: List<LibraryData>, private val c: Context) :
    RecyclerView.Adapter<LibraryAdapter.LibraryViewHolder>(), Filterable {
    object Constants {
        const val MAX_PDF_SIZE: Long = 35000000
    }

    private lateinit var filter: FilterLibrary
    private lateinit var filterList: List<LibraryData>


    inner class LibraryViewHolder(private var binding: RewLibraryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val title = binding.pdfItmTitle
        val description = binding.pdfItmDesc
        val btnMore = binding.pdfItmMore
        val progressBar = binding.loadPdf
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

        val description = currentItem.description

        holder.title.text = title
        holder.description.text = description
        loadLibraryData(null, holder.pdfView, urlPdf, title, description, holder.progressBar)

    }


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
                    .onError {
                        progressBar.visibility = View.INVISIBLE
                    }
                    .onPageError { page, t ->
                        progressBar.visibility = View.INVISIBLE

                    }
                    .onLoad { nbPages ->
                        progressBar.visibility = View.INVISIBLE

                        if (pagesTv != null) {
                            pagesTv.text = "$nbPages"
                        }
                    }
                    .load()
            }
            .addOnFailureListener {

            }
    }


    override fun getFilter(): Filter {
        if (filter == null) {
            filter = FilterLibrary(filterList, this)
        }
        return filter as FilterLibrary
    }


}