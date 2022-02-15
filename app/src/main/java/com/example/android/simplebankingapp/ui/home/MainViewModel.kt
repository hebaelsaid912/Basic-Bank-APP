package com.example.android.simplebankingapp.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.simplebankingapp.database.SQLiteHelper
import com.example.android.simplebankingapp.pojo.CurrentUserData
import com.example.android.simplebankingapp.pojo.CustomersData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainViewModel(context: Context): ViewModel() {
    private val _viewStateCustomersData = MutableStateFlow<GetCustomersListViewState>(GetCustomersListViewState.Idle)
    val stateCustomersData: StateFlow<GetCustomersListViewState> get() = _viewStateCustomersData
    private val _viewStateCurrentUserData = MutableStateFlow<GetCurrentUserViewState>(GetCurrentUserViewState.Idle)
    val stateCurrentUserData: StateFlow<GetCurrentUserViewState> get() = _viewStateCurrentUserData

    private var sqLiteHelper: SQLiteHelper = SQLiteHelper(context)

    sealed class GetCustomersListViewState {
        data class Success(val customersDataList: ArrayList<CustomersData>) : GetCustomersListViewState()
        data class Error(val message: String) : GetCustomersListViewState()
        object Loading : GetCustomersListViewState()
        object Idle : GetCustomersListViewState()
    }
    sealed class GetCurrentUserViewState {
        data class Success(val currentUserData: ArrayList<CurrentUserData>) : GetCurrentUserViewState()
        data class Error(val message: String) : GetCurrentUserViewState()
        object Loading : GetCurrentUserViewState()
        object Idle : GetCurrentUserViewState()
    }

     fun getCustomersList() = viewModelScope.launch {
        _viewStateCustomersData.value = GetCustomersListViewState.Loading
        delay(2000L)
        _viewStateCustomersData.value = try {
            GetCustomersListViewState.Success(getCustomersListData())
        }catch (ex : Exception){
            GetCustomersListViewState.Error(ex.message!!)
        }
    }
    private fun getCustomersListData() : ArrayList<CustomersData> = runBlocking{
        return@runBlocking sqLiteHelper.getCustomersData()
    }
    fun getCurrentUser() = viewModelScope.launch {
        _viewStateCurrentUserData.value = GetCurrentUserViewState.Loading
        delay(2000L)
        _viewStateCurrentUserData.value = try {
            GetCurrentUserViewState.Success(getCurrentUserData())
        }catch (ex : Exception){
            GetCurrentUserViewState.Error(ex.message!!)
        }
    }
    private fun getCurrentUserData() : ArrayList<CurrentUserData> = runBlocking{
        return@runBlocking sqLiteHelper.getCurrentUserData()
    }
}