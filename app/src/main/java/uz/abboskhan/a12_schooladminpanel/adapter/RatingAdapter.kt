package uz.abboskhan.a12_schooladminpanel.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import uz.abboskhan.a12_schooladminpanel.activity.StudentActivity
import uz.abboskhan.a12_schooladminpanel.databinding.RewRatingBinding
import uz.abboskhan.a12_schooladminpanel.model.TeacherData
import uz.abboskhan.a12_schooladminpanel.databinding.RewTeacherBinding
import uz.abboskhan.a12_schooladminpanel.model.RatingData

class RatingAdapter(private val mList: List<RatingData>, val context: Context) :
    RecyclerView.Adapter<RatingAdapter.RatingViewHolder>() {

    inner class RatingViewHolder(private var binding: RewRatingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val deleteStudent = binding.deleteRating


        val sureName = binding.RatingSur
        val ratingName = binding.RatingName
        val age = binding.RatingAge
        val fan = binding.RatingFan
        val classNumber = binding.RatingClass

        val constraintLayout = binding.RatingInfoCon
        val tableLayout = binding.itmTabRating


        fun collapseExpandedView() {
            tableLayout.visibility = View.GONE
        }

        fun bing(data: RatingData) {
            Picasso.get().load(data.imageUrl).into(binding.itemRatingImg)
            binding.itemRatingFaml.text = data.studentSuraName
            binding.itmRatingName.text = data.studentName

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatingViewHolder {
        val binding = RewRatingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RatingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RatingViewHolder, position: Int) {
        val currentItem = mList[position]
        val id = currentItem.id
        val timestamp = currentItem.timestamp
        val studentName = currentItem.studentName
        val studentSureName = currentItem.studentSuraName
        val studentAge = currentItem.studentAge
        val imageUrl = currentItem.imageUrl
        val studentPhoneNumber = currentItem.studentClassNumber
        val studentScience = currentItem.studentScience

        holder.bing(currentItem)
        holder.ratingName.text = studentName
        holder.sureName.text = studentSureName
        holder.age.text = studentAge
        holder.classNumber.text = studentPhoneNumber
        holder.fan.text = studentScience

        holder.itemView.setOnClickListener {
            val i = Intent(context, StudentActivity::class.java)
            i.putExtra("id", id)
            i.putExtra("timestamp", id)
            i.putExtra("studentName", studentName)
            i.putExtra("studentSureName", studentSureName)
            i.putExtra("studentAge", studentAge)
            i.putExtra("imageUrl", imageUrl)
            i.putExtra("studentClassNumber", studentPhoneNumber)
            i.putExtra("studentScience", studentScience)
            context.startActivity(i)

        }

        val isExpandable: Boolean = currentItem.isExpandable
        holder.tableLayout.visibility = if (isExpandable) View.VISIBLE else View.GONE

        holder.constraintLayout.setOnClickListener {
            isAnyItemExpanded(position)
            currentItem.isExpandable = !currentItem.isExpandable
            notifyItemChanged(position, Unit)
        }



        holder.deleteStudent.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Delete")
                .setMessage("delete student ")
                .setPositiveButton("yes") { _, _ ->

                    newsDelete(id, imageUrl)
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

    private fun newsDelete(id: String, urlImage: String) {
        val ref = FirebaseStorage.getInstance().getReferenceFromUrl(urlImage)

        ref.delete()
            .addOnSuccessListener {

                val firebaseData = FirebaseDatabase.getInstance().getReference("RatingData")

                firebaseData.child(id)
                    .removeValue()
                    .addOnSuccessListener {
                        Toast.makeText(context, "success delete", Toast.LENGTH_SHORT).show()

                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "delete error", Toast.LENGTH_SHORT).show()
                    }

            }
            .addOnFailureListener {
                Toast.makeText(context, "delete error", Toast.LENGTH_SHORT).show()

            }
    }

    private fun isAnyItemExpanded(position: Int) {
        val temp = mList.indexOfFirst {
            it.isExpandable
        }
        if (temp >= 0 && temp != position) {
            mList[temp].isExpandable = false
            notifyItemChanged(temp, 0)
        }
    }

    override fun onBindViewHolder(
        holder: RatingViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty() && payloads[0] == 0) {
            holder.collapseExpandedView()
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }


}