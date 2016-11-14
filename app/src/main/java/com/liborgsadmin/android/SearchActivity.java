package com.liborgsadmin.android;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.liborgsadmin.Datahelpers.DataHelper;
import com.liborgsadmin.Datahelpers.RequestHelper;
import com.liborgsadmin.Datahelpers.adapters.Tab2Adapter;
import com.liborgsadmin.datamodel.AllBooks;
import com.liborgsadmin.interfaces.ResponseInterface;
import com.liborgsadmin.utils.AppUtils;
import com.liborgsadmin.utils.SharedPreferences;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by sakshiagarwal on 18/01/16.
 */
public class SearchActivity extends AppCompatActivity{

    RecyclerView recyclerView;
    TextView noTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.action_search);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        noTextView = (TextView) findViewById(R.id.noTextView);
        String queryurl = getIntent().getStringExtra("queryurl");

        recyclerView = (RecyclerView) findViewById(R.id.searchView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        findSearchedBook(queryurl);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                AppUtils.getInstance().pageTransition(this, BooksDataActivity.class);
        }
        return true;
    }

    private void findSearchedBook(String queryUrl)
    {
        AppUtils.getInstance().showProgressDialog(this, "", getString(R.string.loading));
        new RequestHelper().getSearchedData(queryUrl, new SharedPreferences().getAuthKey(this), new ResponseInterface() {

            @Override
            public void onSuccess(JSONObject jsonObject) {
                List<AllBooks> allBooks = new DataHelper().getAllBooks(jsonObject);
                Tab2Adapter adapter = new Tab2Adapter(SearchActivity.this);
                recyclerView.setAdapter(adapter);
                if (allBooks.size() <= 0) {
                    noTextView.setVisibility(View.VISIBLE);
                } else {
                    adapter.setDataList(allBooks);
                }
            }

            @Override
            public void onFailure(String message) {
                AppUtils.getInstance().showAlertDialogBox(SearchActivity.this, message);
            }
        });
    }
}
