package uz.abboskhan.a12_schooladminpanel.filter

import android.widget.Filter
import uz.abboskhan.a12_schooladminpanel.adapter.LibraryAdapter
import uz.abboskhan.a12_schooladminpanel.model.LibraryData


class FilterLibrary : Filter {

    private var filterList: ArrayList<LibraryData>
    private var adapterLibrary: LibraryAdapter


    constructor(filterList: ArrayList<LibraryData>, adapterLibrary: LibraryAdapter) {
        this.filterList = filterList
        this.adapterLibrary = adapterLibrary
    }

    override fun performFiltering(constraint: CharSequence?): FilterResults {
        var constraint = constraint
        val result = FilterResults()

        if (constraint != null && constraint.isNotEmpty()) {
            constraint = constraint.toString().lowercase()
            val filterModel = ArrayList<LibraryData>()
            for (i in filterList.indices) {

                if (filterList[i].title.lowercase().contains(constraint)) {
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