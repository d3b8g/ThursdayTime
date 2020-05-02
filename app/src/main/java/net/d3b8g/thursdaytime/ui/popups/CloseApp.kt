package net.d3b8g.thursdaytime.ui.popups

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.Button
import net.d3b8g.thursdaytime.R
import kotlin.system.exitProcess

class CloseApp {
    fun uSure(ct: Context): Boolean{
        var status = false
        val frame = Dialog(ct)
        frame.setContentView(R.layout.popup_forceclose)
        val y = frame.findViewById<Button>(R.id.y_close)
        var n = frame.findViewById<Button>(R.id.n_close)
        y.setOnClickListener {
            status = true
            exitProcess(0)
        }
        n.setOnClickListener {
            status = false
            frame.hide()
        }
        frame.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        frame.show()
        return status
    }
}