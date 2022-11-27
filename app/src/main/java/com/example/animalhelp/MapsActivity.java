package com.example.animalhelp;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.animalhelp.databinding.ActivityMapsBinding;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    public static double d_latitude=-1;
    public static double d_longitude=-1;
    public String cityName;
    public static String longitude;
    public static String latitude;
    public StringBuilder sb = new StringBuilder();

    class MyLocationListener implements LocationListener{
        @Override
        public void onLocationChanged(Location location){
            MapsActivity.this.longitude = ""+location.getLongitude();
            MapsActivity.this.latitude = ""+location.getLatitude();

            if(d_latitude != -1 || d_longitude != -1){
                MapsActivity.this.latitude = d_latitude+"";
                MapsActivity.this.longitude = d_longitude+"";

            }
            LatLng here = new LatLng(Double.parseDouble(MapsActivity.this.latitude),
                    Double.parseDouble(MapsActivity.this.longitude));

            Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());

            List<Address> addresses;
            try{
                addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(),1);
                if(addresses.size() > 0){
                    MapsActivity.this.cityName = addresses.get(0).getLocality();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            mMap.addMarker(new MarkerOptions().position(here).title("Marker in "+ MapsActivity.this.cityName));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(here));
        }

        @Override
        public void onProviderDisabled(String provider){}

        @Override
        public void onProviderEnabled(String provider){}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras){}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION )!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},23);
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Add a marker in Sydney and move the camera
       try{
           LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
           MyLocationListener locationListener = new MyLocationListener();
           locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

       }catch (SecurityException se){
           Toast.makeText(
                   getBaseContext(),
                   se.getMessage(),Toast.LENGTH_SHORT
           ).show();
       }
    }

    public void ChangeLocation (double longitude, double latitude){
        Geocoder gc = new Geocoder(getBaseContext(),Locale.getDefault());
        LatLng here = new LatLng(latitude,longitude);

        List<Address> addresses;
        sb.setLength(0);
        try{
            addresses = gc.getFromLocation(latitude,longitude,1);

            if(addresses.size() > 0){
                MapsActivity.this.cityName = addresses.get(0).getLocality();
            }
            for (int i = 0; i < addresses.size(); i++) {
                sb.append(addresses.get(i).getAddressLine(0)+"\t");
                sb.append(addresses.get(i).getLocality()+"\t\r\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }catch(Exception ex){
            Toast.makeText(getBaseContext(), ex.getMessage(),Toast.LENGTH_SHORT).show();
        }
        mMap.addMarker(new MarkerOptions().position(here).title("Marker in: "+ MapsActivity.this.cityName));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(here));
    }
}