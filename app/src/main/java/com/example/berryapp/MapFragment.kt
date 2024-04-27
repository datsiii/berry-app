package com.example.sportochka

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.PointF
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.berryapp.databinding.FragmentMapBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.GeoObjectTapEvent
import com.yandex.mapkit.layers.GeoObjectTapListener
import com.yandex.mapkit.layers.ObjectEvent
import com.yandex.mapkit.location.Location
import com.yandex.mapkit.location.LocationListener
import com.yandex.mapkit.location.LocationManager
import com.yandex.mapkit.location.LocationStatus
import com.yandex.mapkit.map.CameraListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.CameraUpdateReason
import com.yandex.mapkit.map.GeoObjectSelectionMetadata
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObject
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.MapWindow
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import com.yandex.runtime.image.ImageProvider
import java.util.Random
import androidx.fragment.app.FragmentManager
import com.example.berryapp.R


class MapFragment : Fragment(), CameraListener, UserLocationObjectListener {
    private lateinit var binding: FragmentMapBinding
    private lateinit var map: Map
    private lateinit var mapWindow: MapWindow
    private lateinit var locationMapKit: UserLocationLayer
    private var zoomValue: Float = 16.5f
    private var startLocation = Point(0.0, 0.0)
    private lateinit var placemarkMapObject: PlacemarkMapObject
    //    private val TAG = "MAP";
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setApiKey(savedInstanceState)
        MapKitFactory.initialize(requireContext())

        binding = FragmentMapBinding.inflate(inflater, container, false)
        val bot = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bot.visibility = View.VISIBLE


        binding = FragmentMapBinding.inflate(layoutInflater, container, false)
        mapWindow =binding.mapview.mapWindow
        map = mapWindow.map
        binding.mapview.map.addTapListener(geoObjectTapListener) // Добавляем слушатель тапов по объектам
        binding.mapview.map.addInputListener(inputListener)
        requestLocationPermission()
        moveToStartLocation()
        createPlacemark(Point(55.663024, 37.481001))
        createPlacemark(Point(55.669534, 37.481954))
        return binding.root

    }

    private val geoObjectTapListener = object : GeoObjectTapListener {
        override fun onObjectTap(geoObjectTapEvent: GeoObjectTapEvent): Boolean {
            val selectionMetadata: GeoObjectSelectionMetadata = geoObjectTapEvent
                .geoObject
                .metadataContainer
                .getItem(GeoObjectSelectionMetadata::class.java)
            binding.mapview.map.selectGeoObject(selectionMetadata)
            return false
        }
    }

    private val inputListener = object : InputListener {
        override fun onMapLongTap(map: com.yandex.mapkit.map.Map, point: Point) {
            // передвижение метки при долгом нажатии
            //placemarkMapObject.geometry = point


        }
        override fun onMapTap(map: com.yandex.mapkit.map.Map, point: Point) {


        }
    }

    private fun createPlacemark(point: Point) {
        placemarkMapObject = map.mapObjects.addPlacemark(
            point,
            ImageProvider.fromResource(requireContext(), R.drawable.ic_ya_pin),
            IconStyle().apply { anchor = PointF(0.5f, 1.0f) }
        ).apply {
            isDraggable = true
        }
        placemarkMapObject.addTapListener(placemarkTapListener)
    }

    private var placemarkTapListener: MapObjectTapListener =
        object: MapObjectTapListener {
            override fun onMapObjectTap(mapObject: MapObject, point: Point): Boolean {
                println("kkk")
                if (mapObject is PlacemarkMapObject) {
                    val placemark = mapObject

                    val toast = Toast.makeText(
                        requireContext(),
                        ("Placemark tapped"),
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                    val intent = Intent(requireContext(), MapFragment::class.java)
                    startActivity(intent)
                }
                return true
            }
        }


    private fun setApiKey(savedInstanceState: Bundle?) {
        val haveApiKey = savedInstanceState?.getBoolean("haveApiKey")
            ?: false // При первом запуске приложения всегда false
        if (!haveApiKey) {
            MapKitFactory.setApiKey(MAPKIT_API_KEY) // API-ключ должен быть задан единожды перед инициализацией MapKitFactory
        }
    }

    // Если Activity уничтожается (например, при нехватке памяти или при повороте экрана) - сохраняем информацию, что API-ключ уже был получен ранее
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("haveApiKey", true)
    }


    // Отображаем карты перед моментом, когда активити с картой станет видимой пользователю:
    override fun onStart() {

        super.onStart()
        var mapKit = MapKitFactory.getInstance()

        /*locationMapKit = mapKit.createUserLocationLayer(binding.mapview.mapWindow)
        locationMapKit.isVisible = true
        locationMapKit.setObjectListener(this)*/




        mapKit.onStart()

    }

    private fun requestLocationPermission(){
        if(ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),100)
            return
        }
    }

    private fun moveToStartLocation() {
        binding.mapview.map.move(
            CameraPosition(Point(55.663024, 37.481001), zoomValue, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 5f),
            null
        )
    }

    // Останавливаем обработку карты, когда активити с картой становится невидимым для пользователя:
    override fun onStop() {
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    companion object {
        const val MAPKIT_API_KEY = "6ef8d0c5-2284-480e-aac8-1282e21b2c6b"
    }

    override fun onObjectAdded(p0: UserLocationView) {
        locationMapKit.setAnchor(
            PointF((binding.mapview.width *0.5).toFloat(),(binding.mapview.height*0.5).toFloat()),
            PointF((binding.mapview.width *0.5).toFloat(),(binding.mapview.height*0.83).toFloat())
        )

    }

    override fun onObjectRemoved(p0: UserLocationView) {
        TODO("Not yet implemented")
    }

    override fun onObjectUpdated(p0: UserLocationView, p1: ObjectEvent) {
        TODO("Not yet implemented")
    }

    override fun onCameraPositionChanged(
        p0: Map,
        p1: CameraPosition,
        p2: CameraUpdateReason,
        p3: Boolean
    ) {
        TODO("Not yet implemented")
    }
}