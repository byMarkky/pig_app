package com.example.pig_marco_ramos.PIG

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pig_marco_ramos.R

class CustomAdapter(
    private val data: Array<String>,
    private val index: Int,
    private val onItemSelected: (Int, Boolean, String) -> Unit,
) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    class ViewHolder(
        view: View,
        private val index: Int,
        private val onItemSelected: (Int, Boolean, String) -> Unit) : RecyclerView.ViewHolder(view) {

        val textView: TextView
        init {
            textView = view.findViewById(R.id.playerNameTemplate)
            textView.setOnClickListener {
                onItemSelected(index, true, textView.text.toString())
            }
        }

        fun bind(data: String) {
            itemView.findViewById<TextView>(R.id.playerNameTemplate).text = data
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.player_name_text, parent, false)
        return ViewHolder(view, index, onItemSelected)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //holder.textView.text = data[position]
        holder.bind(data[position])
    }

    override fun getItemCount() = data.size

}