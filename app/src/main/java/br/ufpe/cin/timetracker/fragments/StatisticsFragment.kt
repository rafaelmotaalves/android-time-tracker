package br.ufpe.cin.timetracker.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.ufpe.cin.timetracker.R
import br.ufpe.cin.timetracker.databinding.FragmentStatisticsBinding
import br.ufpe.cin.timetracker.util.PermissionsHelper
import br.ufpe.cin.timetracker.viewmodels.StatisticsViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.ClusterManager

class StatisticsFragment(private val permissionsHelper: PermissionsHelper, private val viewModel: StatisticsViewModel) : Fragment() {
    private lateinit var binding: FragmentStatisticsBinding
    private lateinit var clusterManager: ClusterManager<TaskLocation>

    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStatisticsBinding.inflate(layoutInflater, container, false)

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment

        if (permissionsHelper.hasPermissions()) {
            mapFragment.getMapAsync { googleMap ->
                clusterManager = ClusterManager(activity?.applicationContext, googleMap)

                googleMap.setOnCameraIdleListener (clusterManager)
                googleMap.setOnMarkerClickListener (clusterManager)
                googleMap.setOnInfoWindowClickListener(clusterManager)

                LocationServices.getFusedLocationProviderClient(context).lastLocation
                    .addOnSuccessListener {
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                            LatLng(it.latitude, it.longitude), MAP_ZOOM))
                    }

                viewModel.tasks.observe(viewLifecycleOwner) { tasks ->
                    clusterManager.clearItems()
                    tasks.forEach {
                        it.doneLocation?.apply {
                            val loc = TaskLocation(lat, long, it.name)
                            clusterManager.addItem(loc)
                        }
                    }
                    clusterManager.cluster()
                }
            }
        }

        return binding.root
    }

    class TaskLocation(
        private val lat: Double,
        private val lng: Double,
        private val title: String
    ) : ClusterItem {

        override fun getPosition(): LatLng = LatLng(lat, lng)

        override fun getTitle(): String? = title

        override fun getSnippet(): String? = ""
    }

    companion object {
        const val MAP_ZOOM = 10f
        @JvmStatic
        fun newInstance(permissionsHelper: PermissionsHelper, viewModel: StatisticsViewModel) = StatisticsFragment(permissionsHelper, viewModel)
    }
}