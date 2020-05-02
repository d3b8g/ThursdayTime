package net.d3b8g.thursdaytime.app.main.shared

import android.content.Context
import android.util.Log
import com.google.gson.JsonParser
import net.d3b8g.thursdaytime.MainActivity.Companion.map
import net.d3b8g.thursdaytime.app.main.components.Components.Companion.ids
import net.d3b8g.thursdaytime.app.main.components.Components.Companion.reports
import net.d3b8g.thursdaytime.app.main.components.Components.Companion.sNameDevice
import net.d3b8g.thursdaytime.app.main.components.Components.Companion.sNameHash
import net.d3b8g.thursdaytime.app.main.components.Components.Companion.sReport
import net.d3b8g.thursdaytime.app.main.components.Components.Companion.sSetting
import net.d3b8g.thursdaytime.app.main.module.Report

fun hadLogin(ct: Context):Boolean{
    val shared = ct.getSharedPreferences(sSetting,0)
    val result = shared.getLong(sNameDevice,0)
    return result > 0
}
fun doingLogin(ct:Context,hash:String,device:Long){
    val shared = ct.getSharedPreferences(sSetting,0)
    val editor = shared!!.edit()
    editor.putString(sNameHash,hash)
    editor.putLong(sNameDevice,device)
    editor.apply()
}
data class datauserclass(val hash: String,val device: Long)
fun getUserData(ct: Context):datauserclass{
    val shared = ct.getSharedPreferences(sSetting,0)
    val device = shared.getLong(sNameDevice,0)
    val hash = shared.getString(sNameHash,"dick")
    return datauserclass(hash!!,device)
}
fun saveReport(ct:Context,id:String){
    val shared = ct.getSharedPreferences(sReport,0)
    val editor = shared!!.edit()
    editor.putString("report[${reports.count()}]",id)
    editor.apply()
    Log.e("RRR","saveReport")
}
fun getCount(ct:Context):Int{
    val shared = ct.getSharedPreferences(sReport,0)
    return shared.getInt("count",0)
}
fun saveReportIds(ct: Context,rrm:String){
    val shared = ct.getSharedPreferences(sReport,0)
    val editor = shared!!.edit()
    editor.putString("reports_id",rrm)
    editor.apply()
    Log.e("RRR","saveReportIds")
}
fun saveCount(ct:Context,cit:Int){
    val shared = ct.getSharedPreferences(sReport,0)
    val editor = shared!!.edit()
    editor.putInt("count",cit)
    editor.apply()
    Log.e("RRR","saveCount")
}
fun getReportsModule(ct:Context){
    val parser = JsonParser()
    val shared = ct.getSharedPreferences(sReport,0)
    if(map.size>0){
        reports.clear()
        for(i in 0 until getCount(ct)){
            var h1 = parser.parse(shared.getString("report[$i]",""))
            reports.add(
                Report(
                    id = h1.asJsonObject.get("id").asInt,
                    title = h1.asJsonObject.get("title").asString,
                    steps = h1.asJsonObject.get("steps").asString,
                    sa = h1.asJsonObject.get("state_actual").asString,
                    ss = h1.asJsonObject.get("state_supposed").asString,
                    severity = h1.asJsonObject.get("severity").asInt,
                    issue_type = h1.asJsonObject.get("issue_type").asInt))

        }
    }
}
fun getReportsIds(ct: Context):HashMap<Int,Int>{
    val shared = ct.getSharedPreferences(sReport,0)
    var map = hashMapOf<Int,Int>()
    JsonParser().parse(shared.getString("reports_id","{\"maps\":[{\"key\":\"0\",\"value\":\"0\"}]}")).asJsonObject.getAsJsonArray("maps").forEach {
        if(it.asJsonObject.get("key").asInt!=0){
            map[it.asJsonObject.get("key").asInt] = it.asJsonObject.get("value").asInt
            ids.add(it.asJsonObject.get("key").asInt)
        }
    }
    return map
}
fun clearReports(ct: Context){
    val shared = ct.getSharedPreferences(sReport,0)
    val editor = shared!!.edit()
    editor.clear()
    editor.apply()
}

