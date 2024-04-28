package com.example.berryapp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.berryapp.WiFi.WiFiReceiver
import com.example.berryapp.databinding.FragmentWiFiBinding


class WiFiFragment : Fragment() {
    private lateinit var binding: FragmentWiFiBinding
    private lateinit var wifiList: ListView
    private lateinit var wifiManager: WifiManager
    private val MY_PERMISSION_ACCESS_COARSE_LOCATION = 1

    lateinit var receiverWifi: WiFiReceiver

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWiFiBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    private var activityResultLauncher: ActivityResultLauncher<Array<String>>
    init{
        this.activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()) {result ->
            var allAreGranted = true
            for(b in result.values) {
                allAreGranted = allAreGranted && b
            }

            if(allAreGranted) {
                wifiManager.startScan()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        wifiList = binding.recyclerViewWifi
        val btnScan = binding.scanBtn

        wifiManager = requireContext().applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        if(!wifiManager.isWifiEnabled){
            Toast.makeText(requireContext(),"Включаем WiFi..", Toast.LENGTH_SHORT).show()
            wifiManager.setWifiEnabled(true)
        }
        btnScan.setOnClickListener{
            val appPerms = arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            activityResultLauncher.launch(appPerms)
        }
        wifiList.setOnItemClickListener { list, view, position, id ->
            Log.i("HelloListView", "You clicked Item: $id at position:$position")
            val dialog = DevDialog.newInstance(text = "Description", hint = "Description", isMultiline = true)
            dialog.onOk = {
                val text = dialog.editText.text
                // do something
            }
            dialog.onCancel = {
            }
            var fragmentManager = (activity as FragmentActivity).supportFragmentManager
            dialog.show(fragmentManager, "editDescription")
        }
    }


    override fun onResume() {
        super.onResume()
        receiverWifi = WiFiReceiver(wifiManager, wifiList)
        val intentFilter = IntentFilter()
        intentFilter.addAction(
            WifiManager.SCAN_RESULTS_AVAILABLE_ACTION
        )
        requireActivity().registerReceiver(receiverWifi, intentFilter)
    }

    override fun onPause() {
        super.onPause()
        requireActivity().unregisterReceiver(receiverWifi)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == MY_PERMISSION_ACCESS_COARSE_LOCATION && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(requireContext(), "Permisssion granted", Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(requireContext(), "Permisssion not granted", Toast.LENGTH_SHORT).show()
        }
    }
}