package hr.algebra.ivanabilic.nba

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class GoogleMapsActivity : FragmentActivity(),OnMapReadyCallback {

    private lateinit var map:GoogleMap
    private lateinit var mapFrgament:SupportMapFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_maps)

        //supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mapFrgament=supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFrgament.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        map=googleMap;

        val korcula=LatLng(42.95352427746524, 16.89296737500771);
        map.addMarker(MarkerOptions().position(korcula).title("Korkyra"))
        map.moveCamera(CameraUpdateFactory.newLatLng(korcula))
    }



}