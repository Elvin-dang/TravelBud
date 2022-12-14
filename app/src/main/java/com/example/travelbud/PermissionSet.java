package com.example.travelbud;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionSet {
    public static final int RECORD_PERMISSION_REQUEST_CODE = 1;
    public static final int EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 2;
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 3;
    public static final int READFILES_PERMISSION_REQUEST_CODE = 4;
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 5;
    public static final int FINE_LOCATION_PERMISSION_REQUEST_CODE = 6;

    Activity activity;

    public PermissionSet(Activity activity) {
        this.activity = activity;
    }


    public boolean checkPermissionForExternalStorage() {
        int result = ContextCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public boolean checkPermissionForCamera() {
        int result = ContextCompat.checkSelfPermission(activity,
                Manifest.permission.CAMERA);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermissionForCamera() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                Manifest.permission.CAMERA)) {
            Toast.makeText(activity, "Camera permission needed. Please allow in App " +
                    "Settings for additional functionality.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(activity, new
                            String[]{Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    CAMERA_PERMISSION_REQUEST_CODE);
            System.out.print("get camera permission");
        }
    }
}