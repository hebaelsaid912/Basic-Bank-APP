package com.example.android.simplebankingapp.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.simplebankingapp.ui.profile.ProfileActivity
import com.example.android.simplebankingapp.R
import com.example.android.simplebankingapp.databinding.ActivityMainBinding
import com.example.android.simplebankingapp.pojo.CurrentUserData
import com.example.android.simplebankingapp.pojo.CustomersData
import com.example.android.simplebankingapp.ui.transfer_dialog.TransferDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel:MainViewModel
    private val customersNameList = ArrayList<String>()
    private val customersEmailList = ArrayList<String>()
    private val customersBalanceList = ArrayList<String>()
    private var currentUserName:String?=null
    private var currentUserBalance:String?=null
    private var currentUserEmail:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.customersShimmer.stopShimmer()
        binding.customersShimmer.visibility = View.GONE
        viewModel = MainViewModel(this)
        processRenderCurrentUserData()
        processRenderCustomersList()
        binding.userProfileBtn.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
        binding.transferNowBtn.setOnClickListener {
            TransferDialogFragment.newInstance(currentUserName!!,customersNameList,
                customersEmailList,customersBalanceList,currentUserBalance!!,currentUserEmail!!)
            val dialog = TransferDialogFragment(this)
            dialog.show(supportFragmentManager,"custom_dialog")

        }
        binding.swapToRefresh.setOnRefreshListener {
            lifecycleScope.launch {
                binding.customersRv.adapter = null
                processRenderCustomersList()
                delay(1000L)
            }
            binding.swapToRefresh.isRefreshing = false
        }
    }
    private fun processRenderCustomersList() {
        lifecycleScope.launchWhenStarted {
            viewModel.stateCustomersData.collect{
                when (it) {
                    is MainViewModel.GetCustomersListViewState.Idle ->{
                         viewModel.getCustomersList()
                    }
                    is MainViewModel.GetCustomersListViewState.Success ->{
                        for (items in it.customersDataList) {
                            customersNameList.add(items.name)
                            customersEmailList.add(items.email)
                            customersBalanceList.add(items.balance.toString())
                        }
                        binding.customersShimmer.stopShimmer()
                        binding.customersShimmer.visibility = View.GONE
                        setCustomersListView(it.customersDataList)

                    }
                    is MainViewModel.GetCustomersListViewState.Loading ->{
                        binding.customersShimmer.startShimmer()
                        binding.customersShimmer.visibility = View.VISIBLE
                        Log.d("MainActivity","GetCustomersListViewState in loading")

                    }
                    is MainViewModel.GetCustomersListViewState.Error ->{
                        Log.d("MainActivity","Error GetCustomersListViewState ${it.message}")
                    }
                }
            }
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun setCustomersListView(customersList:ArrayList<CustomersData>) {
        customersList.reverse()
        val mainAdapter = CustomersAdapter(customersList)
        binding.customersRv.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.VERTICAL, false
        )
        binding.customersRv.adapter = mainAdapter
        binding.customersRv.startLayoutAnimation()
    }
    @SuppressLint("SetTextI18n")
    private fun processRenderCurrentUserData() {
        lifecycleScope.launchWhenStarted {
            viewModel.stateCurrentUserData.collect{
                when (it) {
                    is MainViewModel.GetCurrentUserViewState.Idle ->{
                        viewModel.getCurrentUser()
                    }
                    is MainViewModel.GetCurrentUserViewState.Success ->{
                            Log.d(
                                "MainActivity",
                                "GetCurrentUserViewState Success User Name : ${it.currentUserData[0].name}"
                            )
                        binding.customersShimmer.stopShimmer()
                        binding.customersShimmer.visibility = View.GONE
                        binding.textView.text = "Welcome, ${it.currentUserData[0].name.split(" ")[0]}"
                        currentUserName = it.currentUserData[0].name
                        currentUserBalance = it.currentUserData[0].balance.toString()
                        currentUserEmail = it.currentUserData[0].email
                    }
                    is MainViewModel.GetCurrentUserViewState.Loading ->{
                        binding.customersShimmer.startShimmer()
                        binding.customersShimmer.visibility = View.VISIBLE
                        Log.d("MainActivity","GetCurrentUserViewState in loading")

                    }
                    is MainViewModel.GetCurrentUserViewState.Error ->{
                        Log.d("MainActivity","Error GetCurrentUserViewState ${it.message}")
                    }
                }
            }
        }
    }




}