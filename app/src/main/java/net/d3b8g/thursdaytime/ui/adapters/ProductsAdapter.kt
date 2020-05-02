package net.d3b8g.thursdaytime.ui.adapters


import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import net.d3b8g.thursdaytime.MainActivity
import net.d3b8g.thursdaytime.R
import net.d3b8g.thursdaytime.app.main.components.Components.Companion.ids
import net.d3b8g.thursdaytime.app.main.network.FakeRequest


class ProductsAdapter:RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.cell_products,parent,false)
        return  ProductsAdapterView(itemView)
    }

    override fun getItemCount(): Int {
        return MainActivity.map.count()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ProductsAdapterView)holder.bind(ids[position], MainActivity.map[ids[position]]!!)
    }

    class ProductsAdapterView(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var r = itemView.findViewById<LinearLayout>(R.id.container_id)
        var start = itemView.findViewById<ImageButton>(R.id.cell_pr_action)
        var product = itemView.findViewById<TextView>(R.id.product_text)
        var counter = itemView.findViewById<TextView>(R.id.comp_all)

        fun bind(id:Int,cn: Int){
            product.text = "$id"
            if (cn!=0) counter.text = "$cn"
            var clicked = true
            start.setOnClickListener {
                if(clicked) {
                    FakeRequest(itemView.context).start(id)
                    clicked = false
                }
            }
        }

    }
}


