package com.example.berryapp.WiFi

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.berryapp.CustomRecyclerAdapter
import com.example.berryapp.R

class WiFiReceiver(var wifiManager: WifiManager, wifiDeviceList: ListView) : BroadcastReceiver(){
    var sb: StringBuilder? = null
    private lateinit var wifiDeviceList: ListView
    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent!!.action
        if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION == action){
            sb = java.lang.StringBuilder()
            var wifiList: List<ScanResult> = wifiManager.scanResults
            var deviceList: ArrayList<String> = ArrayList()

            for (scanResults in wifiList){
                sb!!.append("\n").append(scanResults.SSID).append(" _ ")
                    .append(scanResults.capabilities)
                deviceList.add(
                    scanResults.SSID.toString()// + " - " + scanResults.capabilities
                )
            }

            Toast.makeText(context, sb, Toast.LENGTH_SHORT).show()

            val arrayAdapter: ArrayAdapter<*> = ArrayAdapter(context!!.applicationContext,
                android.R.layout.simple_list_item_1,
                deviceList.toArray()
                )
                wifiDeviceList.adapter = arrayAdapter
        }
    }
    init {
        this.wifiDeviceList = wifiDeviceList
    }


}