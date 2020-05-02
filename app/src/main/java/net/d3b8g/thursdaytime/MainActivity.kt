package net.d3b8g.thursdaytime

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_main.*
import net.d3b8g.thursdaytime.app.main.components.Components.Companion.ids
import net.d3b8g.thursdaytime.app.main.components.Components.Companion.issue_type_name
import net.d3b8g.thursdaytime.app.main.components.Components.Companion.reports
import net.d3b8g.thursdaytime.app.main.components.Components.Companion.severity_name
import net.d3b8g.thursdaytime.app.main.module.Report
import net.d3b8g.thursdaytime.app.main.shared.*
import net.d3b8g.thursdaytime.ui.adapters.ProductsAdapter
import net.d3b8g.thursdaytime.ui.main.LoginActivity
import net.d3b8g.thursdaytime.ui.popups.CloseApp

class MainActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var adapter:ProductsAdapter
    lateinit var severity_btn:AutoCompleteTextView
    lateinit var issue_btn:AutoCompleteTextView
    lateinit var id:TextInputEditText
    lateinit var title:TextInputEditText
    lateinit var steps:TextInputEditText
    lateinit var sa:TextInputEditText
    lateinit var ss:TextInputEditText
    lateinit var clear:Button

    var workingProcess = false

    var firstStart = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(!hadLogin(this)){
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }
        else {
            map = getReportsIds(this)
            loadComponent()
        }
    }



    private fun loadComponent(){
        setContentView(R.layout.activity_main)

        workingProcess = true

        //saveCount(this,6)

        adapter = ProductsAdapter()
        var rcv = findViewById<RecyclerView>(R.id.rcv_list)
        rcv.adapter = adapter
        rcv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL,false)
        rcv.setHasFixedSize(true)

        severity_btn = findViewById(R.id.severitY)
        val adapterSev = ArrayAdapter(this,R.layout.list_item,severity_name)
        (severity_btn as? AutoCompleteTextView)?.setAdapter(adapterSev)
        severity_btn.showSoftInputOnFocus = false

        issue_btn = findViewById(R.id.issue_type)
        var adapterIssue = ArrayAdapter(this,R.layout.list_item,issue_type_name)
        (issue_btn as? AutoCompleteTextView)?.setAdapter(adapterIssue)
        severity_btn.onItemClickListener = AdapterView.OnItemClickListener { _, _, index, id -> severity = index+1 }
        issue_btn.onItemClickListener = AdapterView.OnItemClickListener { _, _, index, id -> issueType = index+1 }
        issue_btn.showSoftInputOnFocus = false

        var start = findViewById<Button>(R.id.create_report)
        start.setOnClickListener(this)

        clear = findViewById(R.id.clear_data)
        clear.setOnClickListener(this)
        if(map.size>0) clear.visibility = View.VISIBLE

    }

    override fun onResume(){
        super.onResume()
        if(hadLogin(this) && !workingProcess) loadComponent()
    }

    private fun saveID(){
        var pid = id_product.text.toString().toInt()
        if(ids.contains(pid)){
            map[pid] = map[pid]!!+1
        }else{
            ids.add(pid)
            map[pid] = 1
        }
        adapter.notifyDataSetChanged()
        firstStart = false
        var report = "{\"id\":\"${pid}\",\"title\":\"${title.text.toString()}\",\"steps\":\"${steps.text.toString()}\",\"state_actual\":\"${sa.text.toString()}\",\"state_supposed\":\"${ss.text.toString()}\",\"severity\":\"$severity\",\"issue_type\":\"$issueType\"}"
        saveReport(this,report)
        reports.add(Report(pid,title.text.toString(),steps.text.toString(),sa.text.toString(),ss.text.toString(), severity, issueType))
        line1.visibility = View.VISIBLE

    }

    private fun checkPosition(): Boolean {
        id = findViewById(R.id.id_product)
        title = findViewById(R.id.title)
        steps = findViewById(R.id.steps)
        sa = findViewById(R.id.state_actual)
        ss = findViewById(R.id.state_supposed)

        var status = true
        if (id.text.isNullOrBlank() || !id.text.toString().matches("^[0-9]*\$".toRegex())){
            t_id.error = "Без этого далеко не уедешь"
            status = false
        }
        if (title.text.isNullOrBlank()){
            t_title.error = "Не пропустит валидация"
            status = false
        }
        if (steps.text.isNullOrBlank()){
            t_steps.error = "Не одобряем 0-point отчеты"
            status = false
        }
        if (sa.text.isNullOrBlank()){
            t_sa.error = "Без ФР - отклонен."
            status = false
        }
        if (ss.text.isNullOrBlank()){
            t_ss.error = "Без ОР - отклонен."
            status = false
        }
        if(severity==0) {
            status = false
        }
        if(issueType==0){
            status = false
        }
        detectingAfterFallSubmit()
        return status
    }

    private fun detectingAfterFallSubmit(){
        id.doOnTextChanged { text, start, count, after -> if(count>0) t_id.error = null }
        title.doOnTextChanged { text, start, count, after ->  if(count>0) {
            t_title.error = null
            t_title.setEndIconDrawable(R.drawable.ic_close)
            t_title.setEndIconOnClickListener { title.text?.clear() }
        }else t_title.endIconDrawable = null }
        steps.doOnTextChanged { text, start, count, after ->  if(count>0) {
            t_steps.error = null
            t_steps.setEndIconDrawable(R.drawable.ic_close)
            t_steps.setEndIconOnClickListener { steps.text?.clear() }
        }else t_steps.endIconDrawable = null }
        sa.doOnTextChanged { text, start, count, after -> if(count>0) {
            t_sa.error = null
            t_sa.setEndIconDrawable(R.drawable.ic_close)
            t_sa.setEndIconOnClickListener { sa.text?.clear() }
        }else t_sa.endIconDrawable = null  }
        ss.doOnTextChanged { text, start, count, after -> if(count>0) {
            t_ss.error = null
            t_ss.setEndIconDrawable(R.drawable.ic_close)
            t_ss.setEndIconOnClickListener { ss.text?.clear() }
        }else t_ss.endIconDrawable = null  }
    }

    override fun onClick(view: View?) {
        when(view!!.id){
            R.id.create_report->{
                if(checkPosition()) saveID()
                Log.e("z","${checkPosition()}")
            }
            R.id.clear_data->{
                clear.visibility = View.GONE
                clearReports(this@MainActivity)
                map.clear()
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        var ids_string = "{\"maps\":["
        var mxS = map.size
        var bb=1
        if(mxS!=0 && checkPosition()) {
            for(i in map) {
                ids_string+="{\"key\":\"${i.key}\",\"value\":\"${i.value}\"}${if(bb==mxS) "]}" else "," }"
                bb++
            }
            saveReportIds(this,ids_string)
            getReportsModule(this)
            saveCount(this, reports.size)
        }
    }

    companion object{
        var severity:Int = 0
        var issueType:Int = 0
        var map = hashMapOf<Int,Int>()
    }

    override fun onBackPressed() { if(CloseApp().uSure(this)) super.onBackPressed() }
}
