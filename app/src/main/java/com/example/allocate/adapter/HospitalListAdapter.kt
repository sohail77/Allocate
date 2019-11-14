package com.example.allocate.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.allocate.R
import com.example.allocate.model.Hospital
import kotlinx.android.synthetic.main.item.view.*

class HospitalListAdapter (context: Context) : RecyclerView.Adapter<HospitalListAdapter.ViewHolder> () {

    var hospitalList = listOf<Hospital>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ViewHolder(itemView)

    }

    override fun getItemCount() = hospitalList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.hospitalName.text = hospitalList.get(position).name
        holder.addr.text = "6967 Bayers rd"
    }

    fun setUpList(list: List<Hospital>) {
        hospitalList = list
    }



    class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        val hospitalName: TextView
            get() = itemView.name
        val addr: TextView
            get() = itemView.address

    }

}