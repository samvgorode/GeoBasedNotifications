package com.example.who.geobasednotifications.interfaces;

import android.location.Location;

import com.arellomobile.mvp.MvpView;

public interface MapsActivityView  extends MvpView {
    void setLastKnownLocation(Location lastKnownLocation);
    void showDialogSwitchOnGps();
    void locationChanged(Location currentBestLocation);
}
