package com.example.pig_marco_ramos.firebase

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.pig_marco_ramos.R

class GridAdapter(private val data: List<String>) : BaseAdapter() {
    override fun getCount(): Int = data.size

    override fun getItem(p0: Int): String = data[p0]

    override fun getItemId(p0: Int): Long = p0.toLong()

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val view: View = p1 ?: LayoutInflater.from(p2?.context)
            .inflate(R.layout.grid_item, p2, false)

        // Configura el texto del ítem
        val textView: TextView = view.findViewById(R.id.gridItemText)
        textView.text = data[p0]

        return view
    }
}