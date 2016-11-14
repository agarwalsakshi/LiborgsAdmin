package com.liborgsadmin.android;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.liborgsadmin.Datahelpers.DataHelper;
import com.liborgsadmin.Datahelpers.RequestHelper;
import com.liborgsadmin.Datahelpers.adapters.BookRequestAdapter;
import com.liborgsadmin.datamodel.ReturnBook;
import com.liborgsadmin.interfaces.ResponseInterface;
import com.liborgsadmin.utils.AppUtils;
import com.liborgsadmin.utils.SharedPreferences;

import org.json.JSONObject;

import java.util.List;

public class RequestedBooksActivity extends AppCompatActivity {

    private RecyclerView recyclerView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_request);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.request_toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setHomeAsUpIndicator(R.mipmap.left_arrow);
        }
        recyclerView = (RecyclerView) findViewById(R.id.requested_book);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getRequestedBookData();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                AppUtils.getInstance().pageTransition(this, BooksDataActivity.class);
        }
        return true;
    }

    private void getRequestedBookData() {
        AppUtils.getInstance().showProgressDialog(this, "", getString(R.string.loading));
        new RequestHelper().getRequestedBooks(new SharedPreferences().getAuthKey(this), new ResponseInterface() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                AppUtils.getInstance().dismissProgressDialog();
                List<ReturnBook> returnBooks = new DataHelper().getRequestData(RequestedBooksActivity.this, jsonObject);
                if (returnBooks.size() >= 1) {
                    BookRequestAdapter adapter = new BookRequestAdapter(RequestedBooksActivity.this);
                    recyclerView.setAdapter(adapter);
                    adapter.setDataList(returnBooks);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(String message) {
                AppUtils.getInstance().dismissProgressDialog();
                AppUtils.getInstance().showAlertDialogBox(RequestedBooksActivity.this, message);
            }
        });
    }

}
