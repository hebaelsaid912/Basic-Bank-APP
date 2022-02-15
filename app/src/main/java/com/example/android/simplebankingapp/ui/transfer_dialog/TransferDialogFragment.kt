package com.example.android.simplebankingapp.ui.transfer_dialog

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.android.simplebankingapp.R
import com.example.android.simplebankingapp.database.SQLiteHelper
import com.example.android.simplebankingapp.databinding.FragmentTransferDialogBinding
import com.example.android.simplebankingapp.pojo.CurrentUserData
import com.example.android.simplebankingapp.pojo.CustomersData
import com.example.android.simplebankingapp.pojo.TransfersProcessData

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_SENDER_NAME = "sender_name"
private const val ARG_CUSTOMERS_NAME_ARRAY_LIST = "customers_name_arraylist"
private const val ARG_CUSTOMERS_EMAIL_ARRAY_LIST = "customers_email_arraylist"
private const val ARG_CUSTOMERS_BALANCE_ARRAY_LIST = "customers_balance_arraylist"
private const val ARG_CURRENT_USER_BALANCE = "current_user_balance"
private const val ARG_CURRENT_USER_EMAIL = "current_user_email"

class TransferDialogFragment(context: Context) : DialogFragment() {
    // TODO: Rename and change types of parameters

    private var senderName: String? = null
    private var customersNameList: ArrayList<String>? = null
    private var customersEmailList: ArrayList<String>? = null
    private var customersBalanceList: ArrayList<String>? = null
    private var currentUserBalance: String? = null
    private var currentUserEmail: String? = null
    private lateinit var binding: FragmentTransferDialogBinding
    private var sqLiteHelper: SQLiteHelper = SQLiteHelper(context)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        senderName = transferDialogFragmentBundle.getString(ARG_SENDER_NAME)
        customersNameList =
            transferDialogFragmentBundle.getStringArrayList(ARG_CUSTOMERS_NAME_ARRAY_LIST)
        customersEmailList =
            transferDialogFragmentBundle.getStringArrayList(ARG_CUSTOMERS_EMAIL_ARRAY_LIST)
        customersBalanceList =
            transferDialogFragmentBundle.getStringArrayList(ARG_CUSTOMERS_BALANCE_ARRAY_LIST)
        currentUserBalance = transferDialogFragmentBundle.getString(ARG_CURRENT_USER_BALANCE)
        currentUserEmail = transferDialogFragmentBundle.getString(ARG_CURRENT_USER_EMAIL)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTransferDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, customersNameList!!)
        binding.list.setText(customersNameList!![0])
        binding.list.setAdapter(adapter)
        binding.cancelBtn.setOnClickListener {
            val receiverName = binding.list.text.toString()
            val x = customersNameList!!.indexOf(receiverName)
            val receiverEmail = customersEmailList!![x]
            val status = "0"
            val amount = binding.amountTransfer.editText!!.text.toString()
            if(amount.isEmpty()){
                binding.amountTransfer.boxStrokeErrorColor
                binding.amountTransfer.error = "Amount cann't be null"
                Log.d("TransferDialogFragment", "amount is empty ")
            }else{
                if(amount.toInt() >= currentUserBalance?.toInt()!!){
                    binding.amountTransfer.boxStrokeErrorColor
                    binding.amountTransfer.error = "you don't have enough balance"
                    Log.d("TransferDialogFragment", "amount is more than current balance")
                }else{
                    binding.amountTransfer.error = null
                    val transferData = TransfersProcessData(
                        senderName!!,
                        receiverName, receiverEmail, amount.toInt(), status.toInt()
                    )
                    sqLiteHelper.insertNewTransferProcess(transferData)
                    Toast.makeText(
                        context,
                        "transfer money to $receiverName fail",
                        Toast.LENGTH_LONG
                    ).show()
                    dismiss()
                }

            }


        }
        binding.submitTransferBtn.setOnClickListener {
            val receiverName = binding.list.text.toString()
            val x = customersNameList!!.indexOf(receiverName)
            val receiverEmail = customersEmailList!![x]
            val receiverBalance = customersBalanceList!![x].toString()
            val status = "1"
            val amount = binding.amountTransfer.editText!!.text.toString()
            if(amount.isEmpty()){
                binding.amountTransfer.boxStrokeErrorColor
                binding.amountTransfer.error = "Amount cann't be null"
                Log.d("TransferDialogFragment", "amount is empty ")
            }else{
                if(amount.toInt() >= currentUserBalance?.toInt()!!){
                    binding.amountTransfer.boxStrokeErrorColor
                    binding.amountTransfer.error = "you don't have enough balance"
                    Log.d("TransferDialogFragment", "amount is more than current balance")
                }else {
                    binding.amountTransfer.error = null
                    val transferData = TransfersProcessData(
                        senderName!!,
                        receiverName, receiverEmail, amount.toInt(), status.toInt()
                    )
                    sqLiteHelper.insertNewTransferProcess(transferData)
                    sqLiteHelper.updateCurrentUserAmount(CurrentUserData(
                        senderName!!,currentUserEmail!!,(currentUserBalance!!.toInt()-amount.toInt())
                    ))
                    sqLiteHelper.updateCustomersDataAmount(
                        CustomersData(
                        receiverName,receiverEmail,(receiverBalance.toInt()+amount.toInt())
                    )
                    )
                    Toast.makeText(
                        context,
                        "transfer money to $receiverName success",
                        Toast.LENGTH_LONG
                    ).show()
                    dismiss()
                }
            }


        }

    }

    companion object {
        private val transferDialogFragmentBundle = Bundle()

        @JvmStatic
        fun newInstance(
            senderName: String, customersNameList: ArrayList<String>,
            customersEmailList: ArrayList<String>,
            customersBalanceList: ArrayList<String>,
            currentUserBalance: String,
            currentUserEmail: String
        ) {
            transferDialogFragmentBundle.putString(ARG_SENDER_NAME, senderName)
            transferDialogFragmentBundle.putStringArrayList(
                ARG_CUSTOMERS_NAME_ARRAY_LIST,
                customersNameList
            )
            transferDialogFragmentBundle.putStringArrayList(
                ARG_CUSTOMERS_EMAIL_ARRAY_LIST,
                customersEmailList
            )
            transferDialogFragmentBundle.putStringArrayList(
                ARG_CUSTOMERS_BALANCE_ARRAY_LIST,
                customersBalanceList
            )
            transferDialogFragmentBundle.putString(
                ARG_CURRENT_USER_BALANCE,
                currentUserBalance
            )
            transferDialogFragmentBundle.putString(
                ARG_CURRENT_USER_EMAIL,
                currentUserEmail
            )

        }
    }
}