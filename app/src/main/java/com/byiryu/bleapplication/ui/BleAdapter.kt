package com.byiryu.bleapplication.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.byiryu.bleapplication.R
import com.clj.fastble.data.BleDevice

class BleAdapter : RecyclerView.Adapter<BleHolder>() {
    lateinit var listener: ItemClickListener
    var items = ArrayList<BleDevice>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BleHolder {
        return BleHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.view_adapteritem, parent, false), listener
        )
    }

    override fun getItemCount(): Int {
        return this.items.size
    }

    override fun onBindViewHolder(holder: BleHolder, position: Int) {
        holder.bind(items.get(position) , position)

    }

    fun addItem(items : ArrayList<BleDevice>){
        this.items = items
    }
    fun setOnclickListener(listener: ItemClickListener){
        this.listener = listener
    }

    fun notifyAdater(){
        notifyDataSetChanged()
    }
    interface ItemClickListener{
        fun onClickItem(bleDevice: BleDevice)
    }

}