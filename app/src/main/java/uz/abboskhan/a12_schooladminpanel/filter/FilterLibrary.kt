package uz.abboskhan.a12_schooladminpanel.filter

import android.widget.Filter
import uz.abboskhan.a12_schooladminpanel.adapter.LibraryAdapter
import uz.abboskhan.a12_schooladminpanel.model.LibraryData


class FilterLibrary : Filter {

    var filterList: ArrayList<LibraryData>
    var adapterLibrary: LibraryAdapter


    constructor(filterList: List<LibraryData>?, adapterLibrary: LibraryAdapter) {
        this.filterList = filterList as ArrayList<LibraryData>
        this.adapterLibrary = adapterLibrary
    }

    override fun performFiltering(constraint: CharSequence?): FilterResults {
        var c = constraint
        val result = FilterResults()

        if (c != null && c.isNotEmpty()) {
            c = c.toString().lowercase()
            val filterModel = ArrayList<LibraryData>()
            for (i in filterList.indices) {

                if (filterList[i].title.lowercase().contains(c)) {
                    filterModel.add(filterList[i])
                }
            }


            result.count = filterModel.size
            result.values = filterModel
        } else {

            result.count = filterList.size
            result.values = filterList
        }
        return result

    }

    override fun publishResults(constraint: CharSequence, results: FilterResults?) {
        adapterLibrary.mLibraryList = results!!.values as ArrayList<LibraryData>

        adapterLibrary.notifyDataSetChanged()
    }


}