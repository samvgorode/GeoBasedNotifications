package com.example.who.geobasednotifications.ui;

import android.location.Location;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.who.geobasednotifications.R;
import com.example.who.geobasednotifications.interfaces.MapsActivityView;
import com.example.who.geobasednotifications.presenters.MapsActivityPresenter;
import com.example.who.geobasednotifications.ui.fragments.ChooseRadiusDialog;
import com.example.who.geobasednotifications.utils.DialogHelper;
import com.example.who.geobasednotifications.utils.MapUiHelper;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.AfterPermissionGranted;

public class MapsActivity extends BaseActivity implements OnMapReadyCallback, MapsActivityView, GoogleMap.OnMarkerClickListener {

    @BindView (R.id.root_layout)
    FrameLayout root;

    private GoogleMap mMap;
    private Location lastKnownLocation;
    private Snackbar snackbar;
    private Circle circle;
    private Marker centerMarker;
    private Marker userMarker;

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
        mMap.setOnMarkerClickListener(this);
        checkForLocationCoords();
        snackbar = DialogHelper.getSnack(getString(R.string.add_your_marker), snackbar, root);
        addOnMapLongPress();
    }

    private void addOnMapLongPress() {
        mMap.setOnMapLongClickListener(latLng -> {
            if(latLng.equals(userMarker.getPosition())){
                Toast.makeText(this, R.string.you_are_already_here, Toast.LENGTH_LONG).show();
            } else {
            centerMarker = MapUiHelper.getCenterMarker(mMap, centerMarker, latLng);
            snackbar = DialogHelper.getSnack(getString(R.string.add_your_circle), snackbar, root);
            }
        });
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.equals(centerMarker)) {
            ChooseRadiusDialog dialog = ChooseRadiusDialog.newInstance(this::drawCircle);
            if (!dialog.isAdded()) {
                dialog.show(getSupportFragmentManager(), "");
            }
            snackbar = DialogHelper.getSnack(getString(R.string.add_your_circle_again), snackbar, root);
            return true;
        } else return false;
    }

    private void drawCircle(String string) {
        double radius = Double.valueOf(string);
        LatLng latLng = presenter.getLatLngFromMarker(centerMarker);
        circle = MapUiHelper.getCircle(mMap, circle, latLng, radius);
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
        DialogHelper.buildAlertMessageNoGps(this);
    }

    @Override
    public void setLastKnownLocation(Location lastLoc) {
        this.lastKnownLocation = lastLoc;
        if (mMap != null) {
            LatLng latLng = presenter.getLatLngFromLocation(lastLoc);
            userMarker = MapUiHelper.getUserMarker(mMap, userMarker, latLng);
            MapUiHelper.animatePosition(mMap, latLng);
        }
    }

    @Override
    public void locationChanged(Location currentBestLocation) {
        if (presenter.isBetterLocation(lastKnownLocation, currentBestLocation)) {
            lastKnownLocation = currentBestLocation;
            LatLng latLng = presenter.getLatLngFromLocation(currentBestLocation);
            userMarker = MapUiHelper.getUserMarker(mMap, userMarker, latLng);
            MapUiHelper.animatePosition(mMap, latLng);
        }
    }
}
