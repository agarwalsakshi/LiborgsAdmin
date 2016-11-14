package com.liborgsadmin.android;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.liborgsadmin.utils.AppUtils;
import com.liborgsadmin.utils.ApplicationConstants;
import com.liborgsadmin.utils.SharedPreferences;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private EditText bookn, authorn, isbn;
    private ImageView imageView;
    private String bname;
    private Bitmap photo;
    final int CAMERA_CAPTURE = 1, PIC_CROP = 2;
    private Uri picUri;
    private int x1, x2, y1, y2;
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 1;
    public static final int EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 2;
    private static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.upload_toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setHomeAsUpIndicator(R.mipmap.left_arrow);
        }

        bookn = (EditText) findViewById(R.id.bookName);
        authorn = (EditText) findViewById(R.id.authorName);
        imageView = (ImageView) findViewById(R.id.background);
        imageView.setOnClickListener(this);
        isbn = (EditText) findViewById(R.id.ISBN);
        ((ImageView) findViewById(R.id.ISBNButoon)).setOnClickListener(this);
        ((Button) findViewById(R.id.sendButoon)).setOnClickListener(this);
    }

    public void methodCapture() {
        if (AppUtils.getInstance().checkPermissionForCamera(this)) {
            openCamera();
        } else {
            AppUtils.getInstance().requestPermissionForCamera(this, CAMERA_PERMISSION_REQUEST_CODE);
        }

    }

    private void openCamera() {
        try {
            Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, "");
            startActivityForResult(captureIntent, CAMERA_CAPTURE);
        } catch (ActivityNotFoundException anfe) {
            AppUtils.getInstance().showAlertDialogBox(this, getResources().getString(R.string.camera_error_text));
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_CAPTURE) {
                picUri = data.getData();
                performCrop();
            } else if (requestCode == PIC_CROP) {
                Bundle extras = data.getExtras();
                photo = extras.getParcelable("data");
                imageView.setImageBitmap(photo);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        String isbnString = ((LiborsApplication) getApplication()).getIsbn();
        if (isbnString != null) {
            ((LiborsApplication) getApplication()).setIsbn(null);
            isbn.setText(isbnString);
        }
    }

    public void saveData() {

        if(AppUtils.getInstance().checkPermissionForExternalStorage(this)) {
            AppUtils.getInstance().showProgressDialog(this, "", getResources().getString(R.string.saving_text));
            bname = bookn.getText().toString();

            String externalStorageDirectory = Environment.getExternalStorageDirectory().toString();
            OutputStream stream = null;
            final File bookpic = new File(externalStorageDirectory, "pic.jpg");
            try {
                stream = new FileOutputStream(bookpic);
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
            if (photo != null) {
                Log.d("TAG", "saveData: " + photo);
                resizeMethod(photo).compress(Bitmap.CompressFormat.JPEG, 100, stream);
            }
            try {
                stream.flush();
                stream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (NullPointerException e2) {
                e2.printStackTrace();
            }

            if (bname.isEmpty()|| authorn.getText().toString().isEmpty() || photo == null) {
                AppUtils.getInstance().showAlertDialogBox(this, getResources().getString(R.string.fill_all_fields_text));
                AppUtils.getInstance().dismissProgressDialog();
            } else {
                AsyncHttpClient client = new AsyncHttpClient();
                client.addHeader("auth-token", new SharedPreferences().getAuthKey(MainActivity.this));
                RequestParams params = new RequestParams();
                params.put("title", bname);
                params.put("author", authorn.getText().toString());
                params.put("isbn", isbn.getText().toString());
                try {
                    params.put("pic", bookpic);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                client.post(ApplicationConstants.URL_UPLOAD_BOOKS, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject responseBody) {

                        if (responseBody.has("token_expired")) {
                            Log.d(TAG, "onSuccess: tokenexpired, response: " +responseBody.toString());
                            AppUtils.getInstance().dismissProgressDialog();
                            new SharedPreferences().deleteAuthKey(MainActivity.this);
                            AppUtils.getInstance().showAlertDialogBox(MainActivity.this, getResources().getString(R.string.token_expire_text));
                            AppUtils.getInstance().pageTransition(MainActivity.this, LoginActivity.class);

                        } else {
                            Log.d(TAG, "onSuccess: response:" +responseBody.toString());
                            AppUtils.getInstance().dismissProgressDialog();
                            AppUtils.getInstance().pageTransition(MainActivity.this, MainActivity.class);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String message, Throwable error) {
                        Log.d(TAG, "onFailure: message: " +message);
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        AppUtils.getInstance().dismissProgressDialog();
//                        AppUtils.getInstance().pageTransition(MainActivity.this, MainActivity.class);
                    }
                });
            }
        }
        else
        {
            AppUtils.getInstance().requestPermissionForExternalStorage(this, EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
        }
    }

    protected void sendEmail() {
        Log.e("Send email", "");
        String[] TO = {"agg.sakshi1994@gmail.com"};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, "sakshi.agarwal@cybrilla.com");
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "A new book added to Liborgs");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "The book " + bname + " has been added to the Liborgs.\nVisit Liborgs to get it.\nHappy reading. :)");
        if (picUri != null) {
            emailIntent.putExtra(Intent.EXTRA_STREAM, picUri);
        }
        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.e("EmailIntent", "Finished sending Email");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }


    public Bitmap resizeMethod(Bitmap myPhoto) {
        if (myPhoto != null) {
            int width, height;
            int newwidth, newheight;
            width = myPhoto.getWidth();
            height = myPhoto.getHeight();
            if (height >= width) {
                newheight = 480;
                newwidth = (newheight * width) / height;
            } else {
                newwidth = 480;
                newheight = (newwidth * height) / width;
            }
            return Bitmap.createScaledBitmap(photo, newwidth, newheight, true);
        }
        return null;

    }

    private void performCrop() {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(picUri, "image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", x1);
            cropIntent.putExtra("aspectY", y1);
            cropIntent.putExtra("outputX", x2);
            cropIntent.putExtra("outputY", y2);
            cropIntent.putExtra("return-data", true);
            startActivityForResult(cropIntent, PIC_CROP);
        } catch (ActivityNotFoundException anfe) {
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, BooksDataActivity.class);
                startActivity(intent);
                finish();
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ISBNButoon:
                Intent intent = new Intent(this, ScannerActivity.class);
                startActivity(intent);
                break;
            case R.id.background:
                methodCapture();
                break;
            case R.id.sendButoon:
                saveData();
                break;
        }
    }
}
