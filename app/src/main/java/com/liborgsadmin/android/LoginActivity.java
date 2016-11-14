package com.liborgsadmin.android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.liborgsadmin.Datahelpers.RequestHelper;
import com.liborgsadmin.interfaces.ResponseInterface;
import com.liborgsadmin.utils.AppUtils;
import com.liborgsadmin.utils.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText userName, passWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //registering for GCM if not registered
        if (new SharedPreferences().getGCMRegId(this).isEmpty()) {
            AppUtils.getInstance().registerGCM(this);
        }

        //showing login activity if no auth_token
        if (new SharedPreferences().getAuthKey(this).isEmpty()) {
            setContentView(R.layout.activity_login);
            userName = (EditText) findViewById(R.id.editText);
            passWord = (EditText) findViewById(R.id.editText2);
            (findViewById(R.id.loginbutton)).setOnClickListener(this);
            ((TextView) findViewById(R.id.header)).setText(getString(R.string.login));
            (findViewById(R.id.back_button)).setVisibility(View.GONE);
        } else {
            AppUtils.getInstance().pageTransition(this, BooksDataActivity.class);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginbutton:
                loginUser();
                break;
        }
    }

    public void loginUser() {
        AppUtils.getInstance().showProgressDialog(this, "", getString(R.string.loading));
        if (AppUtils.getInstance().isNetworkAvailable(this)) {
            new RequestHelper().loginUser(userName.getText().toString(), passWord.getText().toString(), new ResponseInterface() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    AppUtils.getInstance().dismissProgressDialog();
                    try {
                        new SharedPreferences().storeAuthKey(LoginActivity.this, jsonObject.getString(getString(R.string.auth_token)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    AppUtils.getInstance().pageTransition(LoginActivity.this, BooksDataActivity.class);
                }

                @Override
                public void onFailure(String message) {
                    AppUtils.getInstance().dismissProgressDialog();
                    AppUtils.getInstance().showAlertDialogBox(LoginActivity.this, message);
                }
            });
        } else {
            AppUtils.getInstance().showAlertDialogBox(this, getResources().getString(R.string.could_not_connect_to_server));
        }

    }

}