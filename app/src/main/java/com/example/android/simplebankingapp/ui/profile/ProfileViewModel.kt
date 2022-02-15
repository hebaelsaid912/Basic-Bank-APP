package com.example.android.simplebankingapp.ui.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.simplebankingapp.database.SQLiteHelper
import com.example.android.simplebankingapp.pojo.CurrentUserData
import com.example.android.simplebankingapp.pojo.TransfersProcessData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ProfileViewModel(context: Context): ViewModel() {
    private val _viewStateCurrentUserData = MutableStateFlow<GetUserDataViewState>(
        GetUserDataViewState.Idle)
    val stateCurrentUserData: StateFlow<GetUserDataViewState> get() = _viewStateCurrentUserData
    private val _viewStateTransferProcessData = MutableStateFlow<GetTransferProcessDataViewState>(
        GetTransferProcessDataViewState.Idle)
    val stateTransferProcessData: StateFlow<GetTransferProcessDataViewState> get() = _viewStateTransferProcessData
    private var sqLiteHelper: SQLiteHelper = SQLiteHelper(context)
    sealed class GetUserDataViewState {
        data class Success(val currentUserDataList: ArrayList<CurrentUserData>) : GetUserDataViewState()
        data class Error(val message: String) : GetUserDataViewState()
        object Loading : GetUserDataViewState()
        object Idle : GetUserDataViewState()
    }
    sealed class GetTransferProcessDataViewState {
        data class Success(val transferProcessesDataList: ArrayList<TransfersProcessData>) : GetTransferProcessDataViewState()
        data class Error(val message: String) : GetTransferProcessDataViewState()
        object Loading : GetTransferProcessDataViewState()
        object Idle : GetTransferProcessDataViewState()
    }
    fun getCurrentUserDataList() = viewModelScope.launch {
        _viewStateCurrentUserData.value = GetUserDataViewState.Loading
        delay(2000L)
        _viewStateCurrentUserData.value = try {
            GetUserDataViewState.Success(getCurrentUserListData())
        }catch (ex : Exception){
            GetUserDataViewState.Error(ex.message!!)
        }
    }
    private fun getCurrentUserListData() : ArrayList<CurrentUserData> = runBlocking{
        return@runBlocking sqLiteHelper.getCurrentUserData()
    }
    fun getTransferProcessDataList() = viewModelScope.launch {
        _viewStateTransferProcessData.value = GetTransferProcessDataViewState.Loading
        delay(2000L)
        _viewStateTransferProcessData.value = try {
            GetTransferProcessDataViewState.Success(getTransferProcessDataData())
        }catch (ex : Exception){
            GetTransferProcessDataViewState.Error(ex.message!!)
        }
    }
    private fun getTransferProcessDataData() : ArrayList<TransfersProcessData> = runBlocking{
        return@runBlocking sqLiteHelper.getTransfersProcessData()
    }


}