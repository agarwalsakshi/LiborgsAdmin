package com.liborgsadmin.utils;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

/**
 * Created by sakshiagarwal on 11/01/16.
 */
public class AppUtils {

    private static AppUtils instance;
    private ProgressDialog progressDialog;

    public static AppUtils getInstance(){
        if(instance == null){
            instance = new AppUtils();
        }
        return instance;
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }

    public void showProgressDialog(Activity context, String title, String message){
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    public void dismissProgressDialog(){
        if(progressDialog != null){
            progressDialog.dismiss();
        }
    }

    public void pageTransition(Activity activity, Class<?> transitionclass){
        Intent transitionintent = new Intent(activity, transitionclass);
        transitionintent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        activity.startActivity(transitionintent);
        activity.overridePendingTransition(0, 0);
        activity.finish();
    }

        public void showAlertDialogBox(Context context, String message)
        {
            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setMessage(message);
            alertDialog.setTitle("Alert");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }

    public void showAlertDialogBox(final Context context, String message, final Class<?> transitionClass)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setMessage(message);
        alertDialog.setTitle("Alert");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        AppUtils.getInstance().pageTransition((Activity) context, transitionClass);
                    }
                });
        alertDialog.show();
    }

    public boolean checkPermissionForCamera(Activity activity){
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED){
            return true;
        } else {
            return false;
        }
    }

    public void registerGCM(final Activity activity) {

        new Thread() {
            @Override
            public void run() {
                try {
                    GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(activity);
                    String regId = gcm.register(ApplicationConstants.GOOGLE_PROJECT_ID);
                    new SharedPreferences().storeGCMRegId(activity, regId);

                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }.start();
    }

    public void requestPermissionForCamera(Activity activity, int CAMERA_PERMISSION_REQUEST_CODE){
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)){
            Toast.makeText(activity, "Camera permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.CAMERA},CAMERA_PERMISSION_REQUEST_CODE);
        }
    }

    public boolean checkPermissionForExternalStorage(Activity activity){
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED){
            return true;
        } else {
            return false;
        }
    }

    public void requestPermissionForExternalStorage(Activity activity, int EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE){
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            Toast.makeText(activity, "", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
        }
    }

}
