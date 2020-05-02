package net.d3b8g.thursdaytime.app.main.network

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import net.d3b8g.thursdaytime.app.main.components.Components.Companion.reports
import net.d3b8g.thursdaytime.app.main.module.Report
import net.d3b8g.thursdaytime.app.main.shared.getReportsModule
import net.d3b8g.thursdaytime.app.main.shared.getUserData
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.net.URL
import java.util.*
import java.util.logging.Handler
import javax.net.ssl.HttpsURLConnection
import kotlin.collections.ArrayList
import kotlin.random.Random.Default.Companion

class FakeRequest(var ct:Context) {

    var nid = 0
    var okHttpClient = OkHttpClient()

    fun start(id:Int) {
        getReportsModule(ct)
        var count = 0
        for(i in calculateSeverity()){
            if(id== reports[i].id){
                val body = JSONObject()
                body.put("al",1)
                body.put("confidential",0)
                body.put("descr",reports[i].steps)
                body.put("hash",getUserData(ct).hash)
                body.put("id",0)
                body.put("issue_type",reports[i].issue_type)
                body.put("phone",null)
                body.put("platforms",3)
                body.put("platforms_versions",6)
                body.put("product",id)
                body.put("region_id",0)
                body.put("severity",reports[i].severity)
                body.put("state_actual",reports[i].sa)
                body.put("state_supposed",reports[i].ss)
                body.put("tags",getFreeTag())
                body.put("title",reports[i].title)
                body.put("user_devices[0]",getUserData(ct).device)
                android.os.Handler().postDelayed({
                    vkPushReport(body.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull()))
                },(count * (15000..23000).random() ).toLong())
                count++
            }
        }
    }

    fun vkPushReport(body:RequestBody){
        val request = Request.Builder()
            .url("https://jopa.com/bugs?act=a_save")
            .method("POST",body)
            .addHeader("accept","*/*")
            .addHeader("accept-language","ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7")
            .addHeader("content-type","application/x-www-form-urlencoded")
            .addHeader("x-requested-with","XMLHttpRequest")
            .addHeader("referrer","https://vk.com/bugs?act=add&product=$nid")
            .build()
        okHttpClient.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                Toast.makeText(ct,"Ошибка отправки",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call, response: Response) {
                Toast.makeText(ct,"Запрос успешно обработан!",Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun getFreeTag(): String {
        return "233%2C187%2C239%2C402"
    }

    private fun calculateSeverity():ArrayList<Int>{
        var intBySeverity:ArrayList<Int> = ArrayList()
        var low:ArrayList<Int> = ArrayList()
        var middle:ArrayList<Int> = ArrayList()
        var high:ArrayList<Int> = ArrayList()
        var ok:ArrayList<Int> = ArrayList()
        for ((count,i) in reports.withIndex()){
            when (i.severity) {
                5 -> low.add(count)
                4 -> middle.add(count)
                3 -> high.add(count)
                else -> ok.add(count)
            }
        }
        intBySeverity.addAll(ok)
        intBySeverity.addAll(high)
        intBySeverity.addAll(middle)
        intBySeverity.addAll(low)
        return intBySeverity
    }
}