package com.example.android.simplebankingapp.ui.splash

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

class SplashViewModel(context: Context):ViewModel() {
    private val _viewStateInsertCustomerList = MutableStateFlow<InsertCustomersListViewState>(InsertCustomersListViewState.Idle)
    val stateCustomerList: StateFlow<InsertCustomersListViewState> get() = _viewStateInsertCustomerList
    private val _viewStateInsertCurrentUserData = MutableStateFlow<InsertCurrentUserViewState>(InsertCurrentUserViewState.Idle)
    val stateCurrentUserData: StateFlow<InsertCurrentUserViewState> get() = _viewStateInsertCurrentUserData

    private var sqLiteHelper: SQLiteHelper = SQLiteHelper(context)

    sealed class InsertCustomersListViewState {
        data class Success(val isInserted: Boolean) : InsertCustomersListViewState()
        data class Error(val message: String) : InsertCustomersListViewState()
        object Loading : InsertCustomersListViewState()
        object Idle : InsertCustomersListViewState()
    }
    sealed class InsertCurrentUserViewState {
        data class Success(val isInserted: Boolean) : InsertCurrentUserViewState()
        data class Error(val message: String) : InsertCurrentUserViewState()
        object Loading : InsertCurrentUserViewState()
        object Idle : InsertCurrentUserViewState()
    }

     fun setCustomersDataIntoDB(customersList:ArrayList<CustomersData>) = viewModelScope.launch {
        _viewStateInsertCustomerList.value = InsertCustomersListViewState.Loading
        val isInserted = insertCustomerListIntoDB(customersList)
        delay(2000L)
        _viewStateInsertCustomerList.value = try {
            InsertCustomersListViewState.Success(isInserted = isInserted)
        }catch (ex : Exception){
            InsertCustomersListViewState.Error(ex.message!!)
        }
    }
    private fun insertCustomerListIntoDB(customersList:ArrayList<CustomersData>) : Boolean = runBlocking{
        var statusObserver:Long = -1
        for(customer in customersList){
             statusObserver =  sqLiteHelper.insertCustomersData(customer)
        }
        return@runBlocking statusObserver > -1
    }
     fun setCurrentUserDataIntoDB(currentUserData: CurrentUserData) = viewModelScope.launch {
        _viewStateInsertCurrentUserData.value = InsertCurrentUserViewState.Loading
        val isInserted = insertCurrentUserDataIntoDB(currentUserData)
        delay(2000L)
        _viewStateInsertCurrentUserData.value = try {
            InsertCurrentUserViewState.Success(isInserted = isInserted)
        }catch (ex : Exception){
            InsertCurrentUserViewState.Error(ex.message!!)
        }
    }
    private fun insertCurrentUserDataIntoDB(currentUserData: CurrentUserData) : Boolean = runBlocking{
        val statusObserver =  sqLiteHelper.insertCurrentUser(currentUserData)
        return@runBlocking statusObserver > -1
    }
}