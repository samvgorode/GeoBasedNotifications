package com.example.who.geobasednotifications.utils;

import android.support.v4.content.ContextCompat;
import com.example.who.geobasednotifications.App;
import com.example.who.geobasednotifications.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapUiHelper {

    public static Marker getCenterMarker(GoogleMap mMap, Marker centerMarker, LatLng latLng){
        if (centerMarker != null) centerMarker.remove();
        return mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
    }

    public static Marker getUserMarker(GoogleMap mMap, Marker marker, LatLng latLng) {
            if (marker == null)
                return mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(App.getAppContext().getString(R.string.you_are_here))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            else {marker.setPosition(latLng); return marker;}
    }

    public static void moveCamers(GoogleMap mMap, LatLng latLng){
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    public static void animateCamera(GoogleMap mMap, LatLng latLng){
        CameraPosition cameraPosition = CameraPosition.builder().target(latLng).zoom(17).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public static Circle getCircle(GoogleMap mMap, Circle circle, LatLng latLng, double radius){
        if (circle != null) {circle.remove();}
        return mMap.addCircle(new CircleOptions()
                .center(latLng)
                .radius(radius)
                .strokeColor(ContextCompat.getColor(App.getAppContext(), R.color.colorGreen))
                .fillColor(ContextCompat.getColor(App.getAppContext(), R.color.colorGreenLite)));
    }
}
