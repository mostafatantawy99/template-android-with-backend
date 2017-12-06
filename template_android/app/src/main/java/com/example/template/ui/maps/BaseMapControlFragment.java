package com.example.template.ui.maps;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.example.template.R;
import com.example.template.utils.KeyBoardUtil;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import static android.content.Context.LOCATION_SERVICE;

public class BaseMapControlFragment<M> extends BaseMapFragment
        implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener
        , LocationListener {


    ///// map control and location
    private FusedLocationProviderClient mFusedLocationClient;
    // public GoogleApiClient mGoogleApiClient;
    public LocationRequest locationRequest;
    private LocationCallback mLocationCallback;
    public LatLng userLocation;
    private boolean gotLocation;
    boolean checkedGpsBefore = true;


    @Override
    public void onResumeBase() {
        prepareMapFragFunc();
        prepareLocationFunc();
    }


    public void prepareLocationFunc() {

        if (checkedGpsBefore) {
            if (checkGpsState()) {
                accessLocation();
            }
        }

    }


    public boolean checkGpsState() {
        boolean availableGps = true;

        ConnectivityManager cm = (ConnectivityManager) getContainerActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        // test for connection
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            LocationManager locationManager = (LocationManager) getContainerActivity().getSystemService(LOCATION_SERVICE);

            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                availableGps = false;

                Snackbar.make(getContainerActivity().findViewById(android.R.id.content), getString(R.string.gpsIsDisabled), Snackbar.LENGTH_INDEFINITE)
                        .setAction(getString(R.string.enable), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        })
                        .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                        .show();
            }
        } else {
            availableGps = false;

            Snackbar.make(getContainerActivity().findViewById(android.R.id.content), getString(R.string.noInternetConnection), Snackbar.LENGTH_LONG).show();
        }

        checkedGpsBefore = false;

        return availableGps;
    }


    public void accessLocation() {
        final int version = Build.VERSION.SDK_INT;

        if (version >= 23) {

            if (ContextCompat.checkSelfPermission(getContainerActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                //  return;


                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        47);
            }                        // TODO: Consider calling
            else {
                mapFragment.getMapAsync(this);
            }
        } else {
            mapFragment.getMapAsync(this);
        }

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        KeyBoardUtil.hideSoftKeyboard(getContainerActivity());
        initMapLocationFunction();

    }

    public void initMapLocationFunction() {

        initializeMap();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContainerActivity());

        getContainerActivity().getWindow().addFlags(128);
        startLocationClient();

    }

    public void initializeMap() {
        try {

            prepareMap();
            mGoogleMap.setMyLocationEnabled(true);

        } catch (Exception e) {

            Log.d("Exception ", e.toString());

        }

    }

    @Override
    public void prepareMapAddress() {

        mGoogleMap.setOnMarkerClickListener(this);
        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {
                latitude = Float.valueOf(String.valueOf(latLng.latitude));
                longitude = Float.valueOf(String.valueOf(latLng.longitude));
                mGoogleMap.clear();
                userLocation = latLng;
                setAddressesAndMarker(latLng);
            }
        });
    }


    public void startLocationClient() {

        try {
            if (locationRequest == null) {
                locationRequest = LocationRequest.create();
                prepareLocationRequest();
                prepareLocationCallBack();
                requestLocationUpdates();
            } else {
                requestLocationUpdates();
            }

        } catch (Exception e) {
            Log.d("exception startLoc ", e.toString());
        }

    }

    private void requestLocationUpdates() {
        mFusedLocationClient.requestLocationUpdates(locationRequest, mLocationCallback, null);
    }

    public void prepareLocationRequest() {

        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setFastestInterval(1 * 1000);
        locationRequest.setInterval(10 * 1000);
        // locationRequest.setSmallestDisplacement(0);

    }

    public void prepareLocationCallBack() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult result) {
                super.onLocationResult(result);
                //mCurrentLocation = locationResult.getLastLocation();
                for (Location location : result.getLocations()) {
                    // Update UI with location data
                    onLocationChanged(location);
                }

                /**
                 * 지속적으로 위치정보를 받으려면
                 * mLocationRequest.setNumUpdates(1) 주석처리하고
                 * 밑에 코드를 주석을 푼다
                 */
                //mFusedLocationClient.removeLocationUpdates(mLocationCallback);
            }

            //Location관련정보를 모두 사용할 수 있음을 의미
            @Override
            public void onLocationAvailability(LocationAvailability availability) {
                //boolean isLocation = availability.isLocationAvailable();
            }
        };

    }


    @Override
    public boolean onMarkerClick(Marker marker)

    {
        return false;
    }


    public void onLocationChanged(Location newLocation) {

        if (newLocation != null) {
            //you can remove this comment to move your finger as you want in map without continuous updating map to current location
            // if (!gotLocation) {
            gotLocation = true;
            latitude = Float.valueOf(String.valueOf(newLocation.getLatitude()));
            longitude = Float.valueOf(String.valueOf(newLocation.getLongitude()));

            LatLng latLng = new LatLng(latitude, longitude);
            if (mGoogleMap != null)

            {
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
                mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(17));
            }

            Log.e("onLocationChanged  getLongitude: ", "" + newLocation.getLongitude());
            Log.e("onLocationChanged  getLatitude: ", "" + newLocation.getLatitude());

            gettingUserLocation(newLocation);
            // }
            gotLocationChanged(newLocation);
        }
    }


    //place marker at current position
    private void gettingUserLocation(Location location) {
        mGoogleMap.clear();
        LatLng latLng=new LatLng(location.getLatitude(), location.getLongitude());
        userLocation = latLng;
        setAddressesAndMarker(latLng);
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 47) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    ) {
                mapFragment.getMapAsync(this);
            }
        }

    }


}
