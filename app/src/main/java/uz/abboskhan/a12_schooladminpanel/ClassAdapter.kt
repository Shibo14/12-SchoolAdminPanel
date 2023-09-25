package uz.abboskhan.a12_schooladminpanel

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import uz.abboskhan.a12_schooladminpanel.databinding.RewItemClassBinding

class ClassAdapter
    (private var mList: List<ClassData>, private val c: Context) :
    RecyclerView.Adapter<ClassAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(var binding: RewItemClassBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val classDataTv = binding.itemTxt
        val classDataDelete = binding.itemImgDelete
//        fun bing(data: ClassData) {
//
//
//            binding.itemTxt.text = data.classData
//
//
//        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding =
            RewItemClassBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val currentItem = mList[position]
        val id = currentItem.id
        val classNumber = currentItem.classNumber
        val timestamp = currentItem.timesTamp

        holder.classDataTv.text = classNumber


        holder.classDataDelete.setOnClickListener {
            val builder = AlertDialog.Builder(c)
            builder.setTitle("Delete")
                .setMessage("delete class ")
                .setPositiveButton("yes") { _, _ ->

                    deleteClass(holder, currentItem)
                }
                .setNegativeButton("no") { d, _ ->

                    d.dismiss()
                }
                .create()
                .show()

        }
        holder.itemView.setOnClickListener {
            val i = Intent(c, ClassTableActivity::class.java)
            i.putExtra("classNumber", classNumber)
            i.putExtra("classNumberId", id)

            c.startActivity(i)

        }


    }

    private fun deleteClass(holder: CategoryViewHolder, currentItem: ClassData) {
        val id = currentItem.id
        val ref = FirebaseDatabase.getInstance().getReference("Class")
        ref.child(id)
            .removeValue()
            .addOnSuccessListener {
                Toast.makeText(c, "delete class number", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(c, "failure", Toast.LENGTH_SHORT).show()
            }

    }

    override fun getItemCount(): Int {
        return mList.size
    }



}