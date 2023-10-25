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

import uz.abboskhan.a12_schooladminpanel.activity.TeacherInfo
import uz.abboskhan.a12_schooladminpanel.model.TeacherData
import uz.abboskhan.a12_schooladminpanel.databinding.RewTeacherBinding
import uz.abboskhan.a12_schooladminpanel.model.Progressbar

class TeacherAdapter(private val mList: ArrayList<TeacherData>, val context: Context): RecyclerView.Adapter<TeacherAdapter.TechViewHolder>() {

    inner class TechViewHolder(private var binding:RewTeacherBinding) :
        RecyclerView.ViewHolder(binding.root) {
  val btnDelTech = binding.deleteTech
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
        val id = currentItem.id
        val sureName = currentItem.techSuraName
        val techScience = currentItem.techScience
        val techAge = currentItem.techAge
        val techPhoneNumber = currentItem.techPhoneNumber
        val image = currentItem.imageUrl

        holder.bing(currentItem)

        holder.itemView.setOnClickListener {
            val i  = Intent(context, TeacherInfo::class.java)
            i.putExtra("TechId",id)
            i.putExtra("name",nameTeacher)
            i.putExtra("sureName",sureName)
            i.putExtra("science",techScience)
            i.putExtra("age",techAge)
            i.putExtra("phone",techPhoneNumber)
            i.putExtra("techImage",image)
            context.startActivity(i)
        }

        holder.btnDelTech.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("O'chirish")
                .setMessage("$nameTeacher haqidagi ma'lumotlarni o'chirish ")
                .setPositiveButton("Ha") { _, _ ->

                    techDelete(id, image)
                }
                .setNegativeButton("Yo'q") { d, _ ->

                    d.dismiss()
                }
                .create()
                .show()
        }



    }

    private fun techDelete(id: String, image: String) {
        val ref = FirebaseStorage.getInstance().getReferenceFromUrl(image)
//          val myPgr = Progressbar(context)
//        myPgr.startDialog()
        ref.delete()
            .addOnSuccessListener {

                val firebaseData = FirebaseDatabase.getInstance().getReference("TeacherData")

                firebaseData.child(id)
                    .removeValue()
                    .addOnSuccessListener {
                        Toast.makeText(context, "success delete", Toast.LENGTH_SHORT).show()
                       // myPgr.dismissProgressBar()
                    }
                    .addOnFailureListener {e->
                      //  myPgr.dismissProgressBar()
                        Toast.makeText(context, "delete error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }

            }
            .addOnFailureListener {e->
               // myPgr.dismissProgressBar()
                Toast.makeText(context, "delete error:${e.message}", Toast.LENGTH_SHORT).show()

            }
    }

    override fun getItemCount(): Int {
        return mList.size
    }


}