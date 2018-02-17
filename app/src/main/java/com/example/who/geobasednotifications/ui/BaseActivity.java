package com.example.who.geobasednotifications.ui;

import android.Manifest;
import android.support.annotation.NonNull;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.example.who.geobasednotifications.R;

import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public abstract class BaseActivity extends MvpAppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private static final String[] COARSE_FINE_LOCATION =
            {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    public static final int RC_LOCATION_PERM = 124;

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        permissionsGranted();
    }

    public abstract void permissionsGranted();

    public boolean hasLocationPermissions() {
        return EasyPermissions.hasPermissions(this, COARSE_FINE_LOCATION);
    }

    public void askForPermissions() {
        EasyPermissions.requestPermissions(
                this,
                getString(R.string.rationale_location),
                RC_LOCATION_PERM,
                COARSE_FINE_LOCATION);
    }

}
