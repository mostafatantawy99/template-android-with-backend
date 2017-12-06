package com.example.template.ui.maps;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by omar on 24/05/2017.
 */

public class BaseMapDisplayFragment<M> extends BaseMapFragment {

    @Override
    public void prepareMapAddress() {

        mGoogleMap.setOnMarkerClickListener(this);
        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {
                latitude = Float.valueOf(String.valueOf(latLng.latitude));
                longitude = Float.valueOf(String.valueOf(latLng.longitude));
                mGoogleMap.clear();
                setAddressesAndMarker(latLng);
            }
        });

        doWork();
    }


}
