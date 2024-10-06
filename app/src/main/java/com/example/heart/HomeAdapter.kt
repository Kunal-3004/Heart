package com.example.heart

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class HomeAdapter(cxt: Context?, var titles: List<String>, var info: List<String>) :
    RecyclerView.Adapter<HomeAdapter.homeViewHolder>() {
    var inflater: LayoutInflater = LayoutInflater.from(cxt)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): homeViewHolder {
        val view: View = inflater.inflate(R.layout.info_card_layout, parent, false)
        return homeViewHolder(view)
    }

    override fun onBindViewHolder(holder: homeViewHolder, position: Int) {
        val viewHolder = holder
        viewHolder.titleView.text = titles[position]
        viewHolder.infoView.text = info[position]
    }

    override fun getItemCount(): Int {
        return titles.size
    }

    inner class homeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var titleView: TextView = itemView.findViewById<TextView>(R.id.infoItem)
        var infoView: TextView = itemView.findViewById<TextView>(R.id.detailItem)
    }
}