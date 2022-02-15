package com.example.android.simplebankingapp.ui.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.simplebankingapp.R
import com.example.android.simplebankingapp.databinding.ActivityProfileBinding
import com.example.android.simplebankingapp.pojo.CurrentUserData
import com.example.android.simplebankingapp.pojo.TransfersProcessData

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var viewModel: ProfileViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_profile)
        viewModel = ProfileViewModel(this)
        binding.userDataShimmer.visibility = View.VISIBLE
        processRenderCurrentUserDataList()
        processRenderTransferProcessesDataList()
    }
    private fun processRenderCurrentUserDataList() {
        lifecycleScope.launchWhenStarted {
            viewModel.stateCurrentUserData.collect {
                when (it) {
                    is ProfileViewModel.GetUserDataViewState.Idle -> {
                        viewModel.getCurrentUserDataList()
                    }
                    is ProfileViewModel.GetUserDataViewState.Success -> {
                        for (items in it.currentUserDataList) {
                            Log.d(
                                "ProfileActivity",
                                "GetUserDataViewState isSuccess ${items.name}"
                            )
                        }
                        setUserViews(it.currentUserDataList[0])
                    }
                    is ProfileViewModel.GetUserDataViewState.Loading -> {
                        binding.userDataShimmer.visibility = View.VISIBLE
                        binding.userDataShimmer.startShimmer()
                        Log.d("ProfileActivity","Error GetUserDataViewState Loading")
                    }
                    is ProfileViewModel.GetUserDataViewState.Error -> {
                        Log.d("ProfileActivity","Error GetUserDataViewState ${it.message}")
                    }
                }
            }
        }
    }
    private fun processRenderTransferProcessesDataList() {
        lifecycleScope.launchWhenStarted {
            viewModel.stateTransferProcessData.collect {
                when (it) {
                    is ProfileViewModel.GetTransferProcessDataViewState.Idle -> {
                        viewModel.getTransferProcessDataList()
                    }
                    is ProfileViewModel.GetTransferProcessDataViewState.Success -> {
                        for (items in it.transferProcessesDataList) {
                            Log.d(
                                "ProfileActivity",
                                "GetTransferProcessDataViewState isSuccess ${items.receiver}"
                            )
                        }
                        binding.transferShimmer.stopShimmer()
                        binding.transferShimmer.visibility = View.GONE
                        if(it.transferProcessesDataList.isEmpty()){
                            binding.emptyView.visibility = View.VISIBLE
                        }else {
                            setTransfersProcessesViews(it.transferProcessesDataList)
                        }
                    }
                    is ProfileViewModel.GetTransferProcessDataViewState.Loading -> {
                        binding.transferShimmer.visibility = View.VISIBLE
                        binding.transferShimmer.startShimmer()
                        Log.d("ProfileActivity","Error GetTransferProcessDataViewState Loading")
                    }
                    is ProfileViewModel.GetTransferProcessDataViewState.Error -> {
                        Log.d("ProfileActivity","Error GetTransferProcessDataViewState ${it.message}")
                    }
                }
            }
        }
    }

    private fun setTransfersProcessesViews(transferProcessesDataList: ArrayList<TransfersProcessData>) {
        transferProcessesDataList.reverse()
        val mainAdapter = TransfersProcessesAdapter(transferProcessesDataList)
        binding.transferProcessRv.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.VERTICAL, false
        )
        binding.transferProcessRv.adapter = mainAdapter
        binding.transferNumTv.text = transferProcessesDataList.size.toString()

    }

    private fun setUserViews(currentUserData: CurrentUserData) {
        binding.userDataShimmer.visibility = View.GONE
        binding.userDataShimmer.stopShimmer()
        binding.userNameTv.text = currentUserData.name
        binding.userEmailTv.text = currentUserData.email
        binding.userAmountTv.text = "${currentUserData.balance} $"
    }

}