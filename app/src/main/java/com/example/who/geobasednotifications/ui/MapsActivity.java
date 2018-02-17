package com.example.who.geobasednotifications.ui;

import android.Manifest;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.PresenterType;
import com.example.who.geobasednotifications.R;
import com.example.who.geobasednotifications.interfaces.MapsActivityView;
import com.example.who.geobasednotifications.presenters.MapsActivityPresenter;
import com.example.who.geobasednotifications.utils.DialogUtils;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MapsActivity extends BaseActivity implements OnMapReadyCallback, MapsActivityView, GoogleMap.OnMarkerClickListener {

    @BindView (R.id.root_layout)
    FrameLayout rootLayout;

    private GoogleMap mMap;
    private Location lastKnownLocation;
    private Snackbar snackbar;
    private Marker centerMarker;

    @InjectPresenter
    MapsActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        startListenForLocationUpdates();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.stopListenLocationUpdates();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(false);
        checkForLocationCoords();
        snackbar = Snackbar.make(rootLayout, getString(R.string.add_your_marker), Snackbar.LENGTH_INDEFINITE);
        snackbar.show();
        addOnMapLongPress();
    }

    private void addOnMapLongPress() {
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                mMap.clear();
                centerMarker = mMap.addMarker(new MarkerOptions()
                        .position(latLng));
                Circle circle = mMap.addCircle(new CircleOptions()
                        .center(latLng)
                        .radius(1000)
                        .strokeColor(Color.RED)
                        .fillColor(Color.GRAY));
            }
        });
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker == centerMarker) {
            snackbar.setText(getString(R.string.add_your_circle));
        }
        return false;
    }

    @AfterPermissionGranted (RC_LOCATION_PERM)
    private void checkForLocationCoords() {
        if (hasLocationPermissions()) {
            presenter.getLastKnownLocation();
        } else {
            super.askForPermissions();
        }
    }

    @AfterPermissionGranted (RC_LOCATION_PERM)
    private void startListenForLocationUpdates() {
        if (hasLocationPermissions()) {
            presenter.startListenLocationUpdates();
        } else {
            super.askForPermissions();
        }
    }

    @Override
    public void permissionsGranted() { checkForLocationCoords();}

    @Override
    public void showDialogSwitchOnGps() {
        DialogUtils.buildAlertMessageNoGps(this);
    }

    @Override
    public void setLastKnownLocation(Location lastKnownLocation) {
        this.lastKnownLocation = lastKnownLocation;
        /*if(mMap!=null) {
        LatLng yours = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
        mMap.addMarker(new MarkerOptions().position(yours).title("You are here"));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(yours));
        }*/
    }

    @Override
    public void locationChanged(Location currentBestLocation) {
        if (presenter.isBetterLocation(lastKnownLocation, currentBestLocation)) {
            lastKnownLocation = currentBestLocation;
        }
    }
}
