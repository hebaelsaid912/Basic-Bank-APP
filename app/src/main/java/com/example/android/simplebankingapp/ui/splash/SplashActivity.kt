package com.example.android.simplebankingapp.ui.splash

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.Pair
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.android.simplebankingapp.R
import com.example.android.simplebankingapp.databinding.ActivitySplashBinding
import com.example.android.simplebankingapp.pojo.CurrentUserData
import com.example.android.simplebankingapp.pojo.CustomersData
import com.example.android.simplebankingapp.ui.home.MainActivity
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions


class SplashActivity : AppCompatActivity(), EasyPermissions.RationaleCallbacks,
    EasyPermissions.PermissionCallbacks {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var viewModel: SplashViewModel
    private val customersList:ArrayList<CustomersData> = ArrayList()
    private lateinit var currentUserData:CurrentUserData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        viewModel = SplashViewModel(this)
       setData()
        readStorageTask()
    }

    private fun setData() {
        // send current user data
        currentUserData = CurrentUserData("Heba Elsaid","heba123@gmail.com",999220)
        // send customers list
        customersList.add(CustomersData("Mohammed Ahmed","mohammed123@gmail.com",123000))
        customersList.add(CustomersData("Naira Mohammed","nono23@gmail.com",13400))
        customersList.add(CustomersData("Elsaid Mohammed","elsaid123@gmail.com",18800))
        customersList.add(CustomersData("Mahmmoud Elsaid","mohmmoud123@gmail.com",22300))
        customersList.add(CustomersData("Noha Ibrahim","noha123@gmail.com",7654))
        customersList.add(CustomersData("Mostafa Ibrahim","mostafa123@gmail.com",7765))
        customersList.add(CustomersData("Khalid Ragab","khalid123@gmail.com",45000))
        customersList.add(CustomersData("Walid Ahmed","walid123@gmail.com",5670))
        customersList.add(CustomersData("Norhan Mohammed","norhan123@gmail.com",10000))
        customersList.add(CustomersData("Ahmed Khaled","ahmed123@gmail.com",6987))
        customersList.add(CustomersData("Abdulrahman Tarek","abdulrahman123@gmail.com",3330))
        customersList.add(CustomersData("Nermeen Maged","nermeen123@gmail.com",1245))
        customersList.add(CustomersData("Ibrahim Ahmed","ibrahim123@gmail.com",333))
        customersList.add(CustomersData("Ammar Ibrahim","ammar123@gmail.com",22222))
        customersList.add(CustomersData("Aymen Ahmed","aymen123@gmail.com",11111))
    }

    private fun processRenderCustomersList() {
        lifecycleScope.launchWhenStarted {
            viewModel.stateCustomerList.collect{
                when (it) {
                    is SplashViewModel.InsertCustomersListViewState.Idle ->{
                        viewModel.setCustomersDataIntoDB(customersList)
                    }
                    is SplashViewModel.InsertCustomersListViewState.Success ->{
                        Log.d("SplashActivity","InsertCustomersListViewState isSuccess ${it.isInserted}")
                    }
                    is SplashViewModel.InsertCustomersListViewState.Loading ->{
                        Log.d("SplashActivity","InsertCustomersListViewState in loading")

                    }
                    is SplashViewModel.InsertCustomersListViewState.Error ->{
                        Log.d("SplashActivity","Error InsertCustomersListViewState ${it.message}")
                    }
                }
            }
        }
    }
    private fun processRenderCurrentUserData() {
        lifecycleScope.launchWhenStarted {
            viewModel.stateCurrentUserData.collect{
                when (it) {
                    is SplashViewModel.InsertCurrentUserViewState.Idle ->{
                        viewModel.setCurrentUserDataIntoDB(currentUserData)
                    }
                    is SplashViewModel.InsertCurrentUserViewState.Success ->{
                        Log.d("SplashActivity","InsertCurrentUserViewState isSuccess ${it.isInserted}")
                    }
                    is SplashViewModel.InsertCurrentUserViewState.Loading ->{
                        Log.d("SplashActivity","InsertCurrentUserViewState in loading")

                    }
                    is SplashViewModel.InsertCurrentUserViewState.Error ->{
                        Log.d("SplashActivity","Error InsertCurrentUserViewState ${it.message}")
                    }
                }
            }
        }
    }

    private fun readStorageTask() {
        if (hasReadStoragePermission()) {
            processRenderCurrentUserData()
            processRenderCustomersList()
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, MainActivity::class.java)
                val options = ActivityOptions.makeSceneTransitionAnimation(
                    this@SplashActivity,
                    Pair.create(binding.bankLogo, "splash")
                )
                startActivity(intent, options.toBundle())
            }, 4500L)

        } else {
            EasyPermissions.requestPermissions(
                this,
                "This app needs access to your storage,",
                123,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }



    private fun hasReadStoragePermission(): Boolean {
        return EasyPermissions.hasPermissions(
            this,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onRationaleAccepted(requestCode: Int) {
        readStorageTask()
    }

    override fun onRationaleDenied(requestCode: Int) {
        AppSettingsDialog.Builder(this).build().show()
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        processRenderCurrentUserData()
        processRenderCustomersList()
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            val options = ActivityOptions.makeSceneTransitionAnimation(
                this@SplashActivity,
                Pair.create(binding.bankLogo, "splash")
            )
            startActivity(intent, options.toBundle())
        }, 4500L)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        AppSettingsDialog.Builder(this).build().show()
        Log.d("SplashActivity", "onPermissionsDenied 1 + $requestCode")
    }
}