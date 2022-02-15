package com.example.android.simplebankingapp.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.android.simplebankingapp.pojo.CurrentUserData
import com.example.android.simplebankingapp.pojo.CustomersData
import com.example.android.simplebankingapp.pojo.TransfersProcessData

class SQLiteHelper(context: Context) : SQLiteOpenHelper(context,DB_NAME,null,DB_VERSION)  {
    companion object{
        private const val DB_VERSION = 1
        private const val DB_NAME = "current_user.db"
        private const val CURRENT_USER_TABLE_NAME = "current_user_tb"
        private const val CUSTOMERS_TABLE_NAME = "customers_tb"
        private const val TRANSFERS_PROCESSES_TABLE_NAME = "transfers_process_tb"
        private const val USER_ID = "_id"
        private const val USER_NAME = "user_name"
        private const val USER_EMAIL = "user_email"
        private const val USER_BALANCE = "user_balance"
        private const val TRANSFER_ID = "_id"
        private const val SENDER = "sender"
        private const val RECEIVER = "receiver"
        private const val RECEIVER_EMAIL = "receiver_email"
        private const val AMOUNT = "amount"
        private const val STATUS = "status"
    }
    override fun onCreate(db: SQLiteDatabase?) {
        val createTableCurrentUserQuery = ("CREATE TABLE $CURRENT_USER_TABLE_NAME " +
                "($USER_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " $USER_NAME TEXT, $USER_EMAIL TEXT, $USER_BALANCE INTEGER )")

        val createTableCustomersQuery = ("CREATE TABLE $CUSTOMERS_TABLE_NAME " +
                "($USER_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " $USER_NAME TEXT, $USER_EMAIL TEXT, $USER_BALANCE INTEGER )")
        val createTableTransferProcessesQuery = ("CREATE TABLE $TRANSFERS_PROCESSES_TABLE_NAME " +
                "($TRANSFER_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " $SENDER TEXT, $RECEIVER TEXT, $RECEIVER_EMAIL TEXT, $AMOUNT INTEGER, $STATUS INTEGER)")
        db?.execSQL(createTableCurrentUserQuery)
        db?.execSQL(createTableCustomersQuery)
        db?.execSQL(createTableTransferProcessesQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        val dropTableIfExistQuery = "DROP TABLE IF EXISTS $CURRENT_USER_TABLE_NAME"
        db!!.execSQL(dropTableIfExistQuery)
        onCreate(db)
    }

    fun insertCurrentUser(user_data: CurrentUserData): Long{
        val db = this.writableDatabase

        val contentValue = ContentValues()
        contentValue.put(USER_NAME,user_data.name)
        contentValue.put(USER_EMAIL,user_data.email)
        contentValue.put(USER_BALANCE,user_data.balance)
        val success = db.insert(CURRENT_USER_TABLE_NAME,null,contentValue)
        db.close()
        return success
    }
    fun insertCustomersData(customers_data: CustomersData): Long{
        val db = this.writableDatabase

        val contentValue = ContentValues()
        contentValue.put(USER_NAME,customers_data.name)
        contentValue.put(USER_EMAIL,customers_data.email)
        contentValue.put(USER_BALANCE,customers_data.balance)
        val success = db.insert(CUSTOMERS_TABLE_NAME,null,contentValue)
        db.close()
        return success
    }
    fun insertNewTransferProcess(transfer_process_data: TransfersProcessData): Long{
        val db = this.writableDatabase

        val contentValue = ContentValues()
        contentValue.put(SENDER,transfer_process_data.sender)
        contentValue.put(RECEIVER,transfer_process_data.receiver)
        contentValue.put(RECEIVER_EMAIL,transfer_process_data.receiverEmail)
        contentValue.put(AMOUNT,transfer_process_data.amount)
        contentValue.put(STATUS,transfer_process_data.status)
        val success = db.insert(TRANSFERS_PROCESSES_TABLE_NAME,null,contentValue)
        db.close()
        return success
    }

    @SuppressLint("Range")
    fun getCurrentUserData():ArrayList<CurrentUserData>{
        val db = this.readableDatabase
        val userDataList: ArrayList<CurrentUserData> = ArrayList()
        val selectQuery = "SELECT * FROM $CURRENT_USER_TABLE_NAME"
        val cursor:Cursor?
        try {
            cursor = db.rawQuery(selectQuery,null)
        }catch (ex:Exception){
            ex.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var name:String
        var email:String
        var balance:Int
        if(cursor.moveToFirst()){
            do {
                name = cursor.getString(cursor.getColumnIndex(USER_NAME))
                email = cursor.getString(cursor.getColumnIndex(USER_EMAIL))
                balance = cursor.getInt(cursor.getColumnIndex(USER_BALANCE))
                val data  = CurrentUserData( name = name , email = email , balance = balance)
                userDataList.add(data)

            }while (cursor.moveToNext())
        }
        return userDataList
    }
    @SuppressLint("Range")
    fun getCustomersData():ArrayList<CustomersData>{
        val db = this.readableDatabase
        val customersDataList: ArrayList<CustomersData> = ArrayList()
        val selectQuery = "SELECT * FROM $CUSTOMERS_TABLE_NAME"
        val cursor:Cursor?
        try {
            cursor = db.rawQuery(selectQuery,null)
        }catch (ex:Exception){
            ex.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var name:String
        var email:String
        var balance:Int
        if(cursor.moveToFirst()){
            do {
                name = cursor.getString(cursor.getColumnIndex(USER_NAME))
                email = cursor.getString(cursor.getColumnIndex(USER_EMAIL))
                balance = cursor.getInt(cursor.getColumnIndex(USER_BALANCE))
                val data  = CustomersData( name = name , email = email , balance = balance)
                customersDataList.add(data)

            }while (cursor.moveToNext())
        }
        return customersDataList
    }
    @SuppressLint("Range")
    fun getTransfersProcessData():ArrayList<TransfersProcessData>{
        val db = this.readableDatabase
        val transfersProcessDataList: ArrayList<TransfersProcessData> = ArrayList()
        val selectQuery = "SELECT * FROM $TRANSFERS_PROCESSES_TABLE_NAME"
        val cursor:Cursor?
        try {
            cursor = db.rawQuery(selectQuery,null)
        }catch (ex:Exception){
            ex.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var sender:String
        var receiver:String
        var receiverEmail:String
        var amount:Int
        var status:Int
        if(cursor.moveToFirst()){
            do {
                sender = cursor.getString(cursor.getColumnIndex(SENDER))
                receiver = cursor.getString(cursor.getColumnIndex(RECEIVER))
                receiverEmail = cursor.getString(cursor.getColumnIndex(RECEIVER_EMAIL))
                amount = cursor.getInt(cursor.getColumnIndex(AMOUNT))
                status = cursor.getInt(cursor.getColumnIndex(STATUS))
                Log.d("getTransfersProcessData", "receiverEmail is $receiverEmail ")
                val data  = TransfersProcessData( sender = sender ,
                    receiver = receiver , receiverEmail= receiverEmail, amount= amount , status = status)
                transfersProcessDataList.add(data)

            }while (cursor.moveToNext())
        }
        return transfersProcessDataList
    }
    fun updateCurrentUserAmount(currentUserData: CurrentUserData): Int {
        val db = this.writableDatabase

        val contentValue = ContentValues()
        contentValue.put(USER_NAME,currentUserData.name)
        contentValue.put(USER_EMAIL,currentUserData.email)
        contentValue.put(USER_BALANCE,currentUserData.balance)
        val success = db.update(CURRENT_USER_TABLE_NAME,contentValue,
            "$USER_NAME ='${currentUserData.name}'",null)
        db.close()
        return success
    }
    fun updateCustomersDataAmount(customerData: CustomersData): Int {
        val db = this.writableDatabase

        val contentValue = ContentValues()
        contentValue.put(USER_NAME,customerData.name)
        contentValue.put(USER_EMAIL,customerData.email)
        contentValue.put(USER_BALANCE,customerData.balance)
        val success = db.update(
            CUSTOMERS_TABLE_NAME,contentValue,
            "$USER_NAME = '${customerData.name}'",null)
        db.close()
        return success
    }

}