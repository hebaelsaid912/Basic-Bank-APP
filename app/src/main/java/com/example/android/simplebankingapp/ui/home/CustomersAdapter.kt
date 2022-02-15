package com.example.android.simplebankingapp.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.simplebankingapp.R
import com.example.android.simplebankingapp.pojo.CustomersData

class CustomersAdapter(val customersList: ArrayList<CustomersData>)
    : RecyclerView.Adapter<CustomersAdapter.CustomersViewHolder>(){

    private lateinit var context: Context
    /*var listener: OnItemClickListener?=null
    fun setOnClickListener(listener: OnItemClickListener){
        this.listener = listener
    }*/


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomersViewHolder {
        context = parent.context
        return CustomersViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.call_users_list,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CustomersViewHolder, position: Int) {
        holder.bind(customersData = customersList[position])
        holder.itemView.setOnClickListener {
           // listener!!.onClick(customersList[position])
        }
    }

    override fun getItemCount(): Int {
        return customersList.size
    }

    inner class CustomersViewHolder(view: View):RecyclerView.ViewHolder(view){
        val customerName = view.findViewById<TextView>(R.id.user_name)
        val customerEmail = view.findViewById<TextView>(R.id.user_email)
        val customerBalance = view.findViewById<TextView>(R.id.user_amount)
        @SuppressLint("SetTextI18n")
        fun bind(customersData: CustomersData){
            customerName.text = customersData.name
            customerEmail.text = customersData.email
            customerBalance.text = "${customersData.balance} $"
        }
    }
    interface OnItemClickListener{
        fun onClick(customerData:CustomersData)
    }
}