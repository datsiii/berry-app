package com.example.berryapp

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.text.StaticLayout
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.graphics.withTranslation
import com.example.berryapp.databinding.FragmentHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.lang.Exception
import java.net.URL
import java.util.logging.Handler


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bottom = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottom.visibility = View.VISIBLE
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val loopImages = listOf(R.drawable.im1, R.drawable.im2, R.drawable.im3, R.drawable.im4, R.drawable.im5, R.drawable.im6)

        //webViewSetup()
        /*try{
            var url = URL("http://orcl.unicorns-group.ru:9009/atis-charts/9C-9C-1F-CA-22-74-Влажность.png")
            var image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            binding.image.setImageBitmap(image);
        }
        catch (e: Exception) {
            //requireContext().ex = e;
        }*/


        android.os.Handler().apply {
            val runnable = object : Runnable {
                var index = 0
                var imageView = binding.image
                override fun run() {
                    imageView.setImageResource(loopImages[index])
                    index++
                    if (index > loopImages.size - 1) {
                        index = 0
                    }
                    postDelayed(this, 5000)
                }
            }
            postDelayed(runnable, 5000)
        }

    }

    /*@RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetJavaScriptEnabled")
    private fun webViewSetup(){
        binding.webview.webViewClient = WebViewClient()
        binding.webview.apply {
            loadUrl("http://orcl.unicorns-group.ru:9009/atis-charts/9C-9C-1F-CA-22-74-Влажность.png")
            settings.javaScriptEnabled = true
            settings.safeBrowsingEnabled = true
        }
    }*/


}