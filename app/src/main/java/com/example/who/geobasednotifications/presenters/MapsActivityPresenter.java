package com.example.who.geobasednotifications.presenters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.who.geobasednotifications.App;
import com.example.who.geobasednotifications.interfaces.MapsActivityView;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

@InjectViewState
public class MapsActivityPresenter extends MvpPresenter<MapsActivityView> implements LocationListener {

    private LocationManager locationManager;
    private String gpsProvider;
    private String netProvider;
    // check for location updates every thirty seconds
    private static final int THIRTY_SECONDS = 1000 /** 30*/;
    private static final int TWO_MINUTES = 1000 * 60 * 2;

    public MapsActivityPresenter() {
        Context context = App.getAppContext();
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        gpsProvider = LocationManager.GPS_PROVIDER;
        netProvider = LocationManager.NETWORK_PROVIDER;
    }

    @SuppressLint ("MissingPermission")
    public void getLastKnownLocation() {
        if (netProviderEnabled()) {
            getViewState().setLastKnownLocation(locationManager.getLastKnownLocation(netProvider));
        } else if (!netProviderEnabled() && gpsProviderEnabled()) {
            getViewState().setLastKnownLocation(locationManager.getLastKnownLocation(gpsProvider));
        } else {
            getViewState().showDialogSwitchOnGps();
        }

    }

    @SuppressLint ("MissingPermission")
    public void startListenLocationUpdates() {
        if (netProviderEnabled()) {
            locationManager.requestLocationUpdates(netProvider, THIRTY_SECONDS, 0, this);
        } else if (!netProviderEnabled() && gpsProviderEnabled()) {
            locationManager.requestLocationUpdates(gpsProvider, THIRTY_SECONDS, 0, this);
        } else {
            getViewState().showDialogSwitchOnGps();
        }
    }

    public void stopListenLocationUpdates() {
        locationManager.removeUpdates(this);
    }

    private boolean netProviderEnabled() {
        return locationManager.isProviderEnabled(netProvider);
    }

    private boolean gpsProviderEnabled() {
        return locationManager.isProviderEnabled(gpsProvider);
    }

    public LatLng getLatLngFromMarker(Marker marker) {
        if (marker != null) {
            return new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
        } else {return new LatLng(0.0, 0.0);}
    }

    public LatLng getLatLngFromLocation(Location location) {
        if (location != null) {
            return new LatLng(location.getLatitude(), location.getLongitude());
        } else {return new LatLng(0.0, 0.0);}
    }

    public boolean isMarkerInsideCircle(Marker marker, Circle circle) {
        float[] distance = new float[2];
        boolean result;
        Location.distanceBetween(marker.getPosition().latitude, marker.getPosition().longitude,
                circle.getCenter().latitude, circle.getCenter().longitude, distance);
        result = !(distance[0] > circle.getRadius());
        return result;
    }

    /**
     * LocationListener methods
     **/

    @Override
    public void onLocationChanged(Location location) {
        getViewState().locationChanged(location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {}

    @Override
    public void onProviderEnabled(String s) {}

    @Override
    public void onProviderDisabled(String s) {
        getViewState().showDialogSwitchOnGps();
    }
}
