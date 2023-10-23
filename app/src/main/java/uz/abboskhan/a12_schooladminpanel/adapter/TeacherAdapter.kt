package uz.abboskhan.a12_schooladminpanel.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import uz.abboskhan.a12_schooladminpanel.model.TeacherData
import uz.abboskhan.a12_schooladminpanel.databinding.RewTeacherBinding

class TeacherAdapter(private val mList: List<TeacherData>, val context: Context): RecyclerView.Adapter<TeacherAdapter.TechViewHolder>() {

    inner class TechViewHolder(private var binding:RewTeacherBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bing(data: TeacherData) {

            Picasso.get().load(data.imageUrl).into(binding.itemImg)

            binding.itemFaml.text = data.techSuraName
            binding.itmName.text = data.techName
            binding.techFan.text = data.techScience


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TechViewHolder {
        val binding = RewTeacherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TechViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TechViewHolder, position: Int) {
        val currentItem = mList[position]
        val nameTeacher = currentItem.techName
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