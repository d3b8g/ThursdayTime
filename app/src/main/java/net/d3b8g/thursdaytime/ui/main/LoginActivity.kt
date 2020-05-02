package net.d3b8g.thursdaytime.ui.main

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.ClipboardManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import net.d3b8g.thursdaytime.MainActivity
import net.d3b8g.thursdaytime.R
import net.d3b8g.thursdaytime.app.main.shared.doingLogin
import net.d3b8g.thursdaytime.app.main.shared.hadLogin

class LoginActivity:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        Typeface.createFromAsset(assets,"fonts/d3.ttf").let { tf->
            t_device.typeface = tf
            t_hash.typeface = tf
            login.typeface = tf
            //btn_copy_github.typeface = tf
            //btn_github.typeface = tf
        }

        login.setOnClickListener {
            if(e_device.text!!.isNotBlank() && e_hash.text!!.isNotBlank()){
                var tt = true
                tt = if(!e_device.text.toString().matches("^[0-9_.-]*\$".toRegex())){
                    t_device.error = "Только цифры"
                    false
                }else true
                if(!e_hash.text.toString().matches("^[a-zA-Z0-9_.-]*\$".toRegex())){
                    t_hash.error = "a-zA-Z0-9, ввел недопустимые символы"
                    tt = false
                }else{
                    tt = true
                }
                if(tt){
                    doingLogin(
                        this@LoginActivity,
                        e_hash.text.toString(),
                        e_device.text.toString().toLong()
                    )
                    loginMain()
                }
            }
        }

        var clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        t_device.setEndIconDrawable(R.drawable.insert_text)
        t_device.setEndIconOnClickListener {
            clipboard.text.toString()?.let { pasteData->
                if(pasteData.matches("^[0-9_.-]*\$".toRegex()) && pasteData.length<20 ) e_device.setText(pasteData)
            }
        }
        t_hash.setEndIconDrawable(R.drawable.insert_text)
        t_hash.setEndIconOnClickListener {
            clipboard.text.toString().let { pasteData->
                if(pasteData.matches("^[a-zA-Z0-9_.-]*\$".toRegex()) && pasteData.length<20 ) e_hash.setText(pasteData)
            }
        }

        var productVersion = "${this.packageManager.getPackageInfo(this.packageName,0).versionName} (${this.packageManager.getPackageInfo(this.packageName,0).versionCode})"
        t_product_version.text = productVersion

        var githubLink = "https://github.com/d3b8g/ThursdayTime"

        btn_github.setOnClickListener {
            var openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse(githubLink)
            startActivity(openURL)
        }
        btn_copy_github.setOnClickListener {
            clipboard.text = githubLink
            Toast.makeText(this,"Ссылка скопирована.",Toast.LENGTH_SHORT).show()
        }
    }

    fun loaderAnimation(count:Int){
        var parent = LinearLayout(this)
        parent.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)

    }

    private fun loginMain() {
        if(hadLogin(this)) startActivity(Intent(this,MainActivity::class.java))
        this.finish()
    }

    override fun onBackPressed() {
        if(hadLogin(this)) super.onBackPressed()
    }

}