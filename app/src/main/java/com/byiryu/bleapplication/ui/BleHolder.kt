package com.byiryu.bleapplication.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.clj.fastble.data.BleDevice

import kotlinx.android.synthetic.main.view_adapteritem.view.*

class BleHolder(itemView: View, private val listener: BleAdapter.ItemClickListener) : RecyclerView.ViewHolder(itemView) {

    fun bind(bleDevice: BleDevice, pos : Int){
        itemView.textView.text = "[$pos] Mac Address : " + bleDevice.mac

        itemView.textView.setOnClickListener {
                listener.onClickItem(bleDevice)
        }


    }
}