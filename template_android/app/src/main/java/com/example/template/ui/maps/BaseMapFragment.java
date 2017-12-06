package com.example.template.ui.maps;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import com.example.template.R;
import com.example.template.ui.utils.general_listeners.MapListeners;
import com.example.template.utils.KeyBoardUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import modules.basemvp.Base;
import modules.basemvp.BaseSupportFragment;

/**
 * Created by omar on 24/05/2017.
 */

public class BaseMapFragment<P extends Base.IPresenter> extends BaseSupportFragment
        implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,
        MapListeners {

    ///// map display
    public GoogleMap mGoogleMap;
    public SupportMapFragment mapFragment;
    public float latitude = 0.0f;
    public float longitude = 0.0f;
    public String cityName;
    public String stateName;
    public String countryName;
    public String address;
    public String city;
    public String state;
    public String country;
    public String postalCode;
    public String knownName;

    @Override
    public void onResume() {
        super.onResume();
        onResumeBase();
    }

    public void onResumeBase() {
        prepareMapFragFunc();
        mapFragment.getMapAsync(this);
    }


    public void prepareMapFragFunc() {
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;
        KeyBoardUtil.hideSoftKeyboard(getContainerActivity());
        prepareMap();
    }

    public void prepareMap() {
        mGoogleMap.clear();
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        mGoogleMap.setBuildingsEnabled(true);
        mGoogleMap.setIndoorEnabled(true);
        mGoogleMap.setTrafficEnabled(true);
        mGoogleMap.getUiSettings().setRotateGesturesEnabled(false);
        prepareMapAddress();
    }

    @Override
    public void prepareMapAddress() {

    }

    @Override
    public void gotLocationChanged(Location newLocation) {

    }

    @Override
    public void doWork() {

    }


    @Override
    public void mapLocAddressAtPoint() {

    }

    @Override
    public int getLayoutResource() {
        return 0;
    }

    @Override
    public void configureUI() {

    }

    @Override
    public Base.IPresenter injectDependencies() {
        return null;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }


    public void clearLocatonAddress() {
        cityName = "";
        stateName = "";
        countryName = "";
        address = "";
        city = "";
        state = "";
        country = "";
        postalCode = "";
        knownName = "";
    }

    public void setAddressesAndMarker(LatLng latLng) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mGoogleMap.addMarker(markerOptions);

        clearLocatonAddress();
        Geocoder gcd = new Geocoder(getContainerActivity(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(latLng.latitude,
                    latLng.longitude, 1);
            cityName = addresses.get(0).getAddressLine(0);
            stateName = addresses.get(0).getAddressLine(1);
            countryName = addresses.get(0).getAddressLine(2);

            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            city = addresses.get(0).getLocality();
            state = addresses.get(0).getAdminArea();
            country = addresses.get(0).getCountryName();
            postalCode = addresses.get(0).getPostalCode();
            knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

            Log.e(" cityName: ", " " + cityName);
            Log.e(" stateName: ", " " + stateName);
            Log.e(" countryName: ", " " + countryName);
            Log.e(" address: ", " " + address);
            Log.e(" city: ", " " + city);
            Log.e(" state: ", " " + state);
            Log.e(" country: ", " " + country);
            Log.e(" postalCode: ", " " + postalCode);
            Log.e(" knownName: ", " " + knownName);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException ofp) {
            ofp.printStackTrace();
        }
        if (addresses != null) {
            if (addresses.size() > 0) {
                mapLocAddressAtPoint();
            }
        }
    }

}
