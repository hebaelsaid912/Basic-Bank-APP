package com.example.android.simplebankingapp.ui.profile

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.simplebankingapp.R
import com.example.android.simplebankingapp.pojo.CustomersData
import com.example.android.simplebankingapp.pojo.TransfersProcessData

class TransfersProcessesAdapter(val transfersProcessDataList: ArrayList<TransfersProcessData>)
    : RecyclerView.Adapter<TransfersProcessesAdapter.TransfersProcessesViewHolder>(){

    private lateinit var context: Context
    /*var listener: OnItemClickListener?=null
    fun setOnClickListener(listener: OnItemClickListener){
        this.listener = listener
    }*/


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransfersProcessesViewHolder {
        context = parent.context
        return TransfersProcessesViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.transfers_to_users_list,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TransfersProcessesViewHolder, position: Int) {
        holder.bind(transfersProcessesData = transfersProcessDataList[position])
        holder.itemView.setOnClickListener {
           // listener!!.onClick(customersList[position])
        }
    }

    override fun getItemCount(): Int {
        return transfersProcessDataList.size
    }

    inner class TransfersProcessesViewHolder(view: View):RecyclerView.ViewHolder(view){
        val customerName = view.findViewById<TextView>(R.id.user_name)
        val customerEmail = view.findViewById<TextView>(R.id.user_email)
        val transferStatusText = view.findViewById<TextView>(R.id.transfer_stats_tv)
        val transferStatusImage = view.findViewById<ImageView>(R.id.transfer_status_image)
        @SuppressLint("SetTextI18n")
        fun bind(transfersProcessesData: TransfersProcessData){
            customerName.text = transfersProcessesData.receiver
            customerEmail.text = transfersProcessesData.receiverEmail
            if(transfersProcessesData.status == 0){
                transferStatusImage.setImageResource(R.drawable.ic_wrong)
                transferStatusText.text = "Incomplete"
                transferStatusText.setTextColor(Color.RED)
            }else{
                transferStatusImage.setImageResource(R.drawable.ic_done)
                transferStatusText.text = "Complete"
                transferStatusText.setTextColor(Color.GREEN)
            }
        }
    }
    interface OnItemClickListener{
        fun onClick(customerData:CustomersData)
    }
}