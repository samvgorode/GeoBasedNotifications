package com.example.who.geobasednotifications.utils;

import android.graphics.Color;
import android.location.Location;

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
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
    }

    public static Marker getUserMarker(GoogleMap mMap, Marker userPosition, LatLng latLng) {
        if (mMap != null) {
            if (userPosition == null) {
                return mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("You are here")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            } else {
                userPosition.setPosition(latLng);
                return userPosition;
            }

        } else return userPosition;
    }

    public static void animatePosition(GoogleMap mMap, LatLng latLng){
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(17)
                .tilt(30)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public static Circle getCircle(GoogleMap mMap, Circle circle, LatLng latLng, double radius){
        if (circle != null) {circle.remove();}
        return mMap.addCircle(new CircleOptions()
                .center(latLng)
                .radius(radius)
                .strokeColor(Color.RED)
                .fillColor(Color.GRAY));
    }
}
