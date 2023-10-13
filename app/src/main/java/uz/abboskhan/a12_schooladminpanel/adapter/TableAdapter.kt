package uz.abboskhan.a12_schooladminpanel.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import uz.abboskhan.a12_schooladminpanel.model.TableData
import uz.abboskhan.a12_schooladminpanel.activity.EditTableActivity
import uz.abboskhan.a12_schooladminpanel.databinding.RewTebleClassBinding

class TableAdapter
    (private var mTAbleList: List<TableData>, private val c: Context) :
    RecyclerView.Adapter<TableAdapter.TableViewHolder>() {

    inner class TableViewHolder(private var binding: RewTebleClassBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val classTableTv = binding.itemTxtClassNumber
        val classHarfTv = binding.classHarf
        val day = binding.itmTxtDay
        val lesson1 = binding.itmLesson1
        val lesson2 = binding.itmLesson2
        val lesson3 = binding.itmLesson3
        val lesson4 = binding.itmLesson4
        val lesson5 = binding.itmLesson5
        val lesson6 = binding.itmLesson6
        val lesson7 = binding.itmLesson7
        val constraintLayout = binding.constraintLayout
        val tableLayout = binding.itmTab
        val classTableMoreBtn = binding.itemImgMore

        fun collapseExpandedView() {
            tableLayout.visibility = View.GONE
        }

        /*
           fun bing(data: ClassData) {

               binding.itemTxt.text = data.classData

           }

         */
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TableViewHolder {
        val binding =
            RewTebleClassBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TableViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TableViewHolder, position: Int) {
        val currentItem = mTAbleList[position]
        val id = currentItem.id
        val classId = currentItem.classId
        val day = currentItem.day
        val lesson1 = currentItem.lesson1
        val lesson2 = currentItem.lesson2
        val lesson3 = currentItem.lesson3
        val lesson4 = currentItem.lesson4
        val lesson5 = currentItem.lesson5
        val lesson6 = currentItem.lesson6
        val lesson7 = currentItem.lesson7

        val timestamp = currentItem.timesTamp
        holder.day.text = day
        holder.lesson1.text = lesson1
        holder.lesson2.text = lesson2
        holder.lesson3.text = lesson3
        holder.lesson4.text = lesson4
        holder.lesson5.text = lesson5
        holder.lesson6.text = lesson6
        holder.lesson7.text = lesson7

        loadClassData(classId, holder.classTableTv, holder.classHarfTv)


        holder.classTableMoreBtn.setOnClickListener {
            moreDialog(holder, currentItem)


        }


        val isExpandable: Boolean = currentItem.isExpandable
        holder.tableLayout.visibility = if (isExpandable) View.VISIBLE else View.GONE

        holder.constraintLayout.setOnClickListener {
            isAnyItemExpanded(position)
            currentItem.isExpandable = !currentItem.isExpandable
            notifyItemChanged(position, Unit)
        }

    }

    private fun moreDialog(holder: TableViewHolder, currentItem: TableData) {
        val classId = currentItem.id
        val classCategory = currentItem.classId

        val setting = arrayOf("Tahrirlash", "O'chirish")

        AlertDialog.Builder(c)
            .setItems(setting)
            { d, p ->
                if (p == 0) {

                    val i = Intent(c, EditTableActivity::class.java)
                    i.putExtra("classIdEdt", classId)
                    i.putExtra("classCategory", classCategory)

                    c.startActivity(i)

                } else if (p == 1) {
                    deleteClass(holder, currentItem)

                }

            }
            .show()

    }

    private fun deleteClass(holder: TableViewHolder, currentItem: TableData) {
        val id = currentItem.id
        val ref = FirebaseDatabase.getInstance().getReference("Table")
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
        return mTAbleList.size
    }

    private fun loadClassData(classId: String, classTv: TextView, classHarfTv: TextView) {

        val ref = FirebaseDatabase.getInstance().getReference("Class")
        ref.child(classId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val classNumber = "${snapshot.child("classNumber").value}"
                    val classHarf = "${snapshot.child("harf").value}"

                    classHarfTv.text = classHarf.lowercase().toString()
                    classTv.text = classNumber.toString()

                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

    }

    private fun isAnyItemExpanded(position: Int) {
        val temp = mTAbleList.indexOfFirst {
            it.isExpandable
        }
        if (temp >= 0 && temp != position) {
            mTAbleList[temp].isExpandable = false
            notifyItemChanged(temp, 0)
        }
    }

    override fun onBindViewHolder(
        holder: TableViewHolder,
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