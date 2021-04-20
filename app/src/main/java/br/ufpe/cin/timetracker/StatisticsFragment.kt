package br.ufpe.cin.timetracker

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.ufpe.cin.timetracker.databinding.FragmentStatisticsBinding
import br.ufpe.cin.timetracker.util.PermissionsHelper

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class StatisticsFragment(private val permissionsHelper: PermissionsHelper, private val viewModel: StatisticsViewModel) : Fragment() {
    private lateinit var binding: FragmentStatisticsBinding

    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStatisticsBinding.inflate(layoutInflater, container, false)

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment

        if (permissionsHelper.hasPermissions()) {
            val locationManager = activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

            mapFragment.getMapAsync { googleMap ->
                if (location != null) {
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude, location.longitude), MAP_ZOOM))
                }

                viewModel.tasks.observe(viewLifecycleOwner) { tasks ->
                    tasks.forEach {
                        it.doneLocation?.apply {
                            googleMap.addMarker(
                                MarkerOptions()
                                    .position(LatLng(lat, long))
                                    .title(it.name)
                            )
                        }
                    }
                }
            }
        }

        return binding.root
    }

    companion object {
        const val MAP_ZOOM = 10f
        @JvmStatic
        fun newInstance(permissionsHelper: PermissionsHelper, viewModel: StatisticsViewModel) = StatisticsFragment(permissionsHelper, viewModel)
    }
}