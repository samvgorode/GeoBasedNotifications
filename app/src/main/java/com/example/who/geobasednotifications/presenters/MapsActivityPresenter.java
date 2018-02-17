package com.example.who.geobasednotifications.presenters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.who.geobasednotifications.App;
import com.example.who.geobasednotifications.interfaces.MapsActivityView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

@InjectViewState
public class MapsActivityPresenter extends MvpPresenter<MapsActivityView> implements LocationListener {

    private LocationManager locationManager;
    private String gpsProvider;
    private String netProvider;
    // check for location updates every thirty seconds
    private static final int THIRTY_SECONDS = 1000 * 30;
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

    public boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            return true;
        }
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;
        if (isSignificantlyNewer) {
            return true;
        } else if (isSignificantlyOlder) {
            return false;
        }
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
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
