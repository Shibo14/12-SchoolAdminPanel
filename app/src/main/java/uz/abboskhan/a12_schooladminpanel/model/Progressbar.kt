package uz.abboskhan.a12_schooladminpanel.model

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import uz.abboskhan.a12_schooladminpanel.R

class Progressbar(var context: Context) {
    private val prgDialog = AlertDialog.Builder(context)

    fun startDialog(){
        val prgDialogView = LayoutInflater.from(context).inflate(R.layout.progressbar_dialog, null)
            prgDialog.setView(prgDialogView)
             prgDialog.setCancelable(false)

        val dialog = prgDialog.create()
        dialog.show()
    }
    fun dismissProgressBar(){
        val dialog = prgDialog.create()
        dialog.dismiss()
    }

}