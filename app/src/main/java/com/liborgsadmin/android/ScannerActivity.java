package com.liborgsadmin.android;

        import android.os.Bundle;
        import android.support.v4.view.MenuItemCompat;
        import android.support.v7.app.ActionBarActivity;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.widget.TextView;
        import com.google.zxing.Result;
        import java.util.ArrayList;

        import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerActivity extends ActionBarActivity implements ZXingScannerView.ResultHandler{
    private static final String FLASH_STATE = "FLASH_STATE";
    private static final String AUTO_FOCUS_STATE = "AUTO_FOCUS_STATE";
    private static final String CAMERA_ID = "CAMERA_ID";
    private ZXingScannerView mScannerView;
    private boolean mFlash;
    private boolean mAutoFocus;
    private ArrayList<Integer> mSelectedIndices;
    private int mCameraId = -1;
    private TextView isbn;
    private String isbnString = "";

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        if(state != null) {
            isbn = (TextView) findViewById(com.liborgsadmin.android.R.id.ISBN);
            mFlash = state.getBoolean(FLASH_STATE, false);
            mAutoFocus = state.getBoolean(AUTO_FOCUS_STATE, true);
            mCameraId = state.getInt(CAMERA_ID, -1);
        } else {
            mFlash = false;
            mAutoFocus = true;
            mSelectedIndices = null;
            mCameraId = -1;
        }

        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera(mCameraId);
        mScannerView.setFlash(mFlash);
        mScannerView.setAutoFocus(mAutoFocus);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(FLASH_STATE, mFlash);
        outState.putBoolean(AUTO_FOCUS_STATE, mAutoFocus);
        outState.putInt(CAMERA_ID, mCameraId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem menuItem;

        if(mFlash) {
            menuItem = menu.add(Menu.NONE, com.liborgsadmin.android.R.id.menu_flash, 0, R.string.flash_on);
        } else {
            menuItem = menu.add(Menu.NONE, com.liborgsadmin.android.R.id.menu_flash, 0, com.liborgsadmin.android.R.string.flash_off);
        }
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_ALWAYS);


        if(mAutoFocus) {
            menuItem = menu.add(Menu.NONE, com.liborgsadmin.android.R.id.menu_auto_focus, 0, com.liborgsadmin.android.R.string.auto_focus_on);
        } else {
            menuItem = menu.add(Menu.NONE, com.liborgsadmin.android.R.id.menu_auto_focus, 0, com.liborgsadmin.android.R.string.auto_focus_off);
        }
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_ALWAYS);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case com.liborgsadmin.android.R.id.menu_flash:
                mFlash = !mFlash;
                if(mFlash) {
                    item.setTitle(com.liborgsadmin.android.R.string.flash_on);
                } else {
                    item.setTitle(com.liborgsadmin.android.R.string.flash_off);
                }
                mScannerView.setFlash(mFlash);
                return true;
            case com.liborgsadmin.android.R.id.menu_auto_focus:
                mAutoFocus = !mAutoFocus;
                if(mAutoFocus) {
                    item.setTitle(com.liborgsadmin.android.R.string.auto_focus_on);
                } else {
                    item.setTitle(com.liborgsadmin.android.R.string.auto_focus_off);
                }
                mScannerView.setAutoFocus(mAutoFocus);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void handleResult(Result rawResult) {
        isbnString = rawResult.getText().toString();
        ((LiborsApplication) getApplication()).setIsbn(isbnString);
        finish();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }
}