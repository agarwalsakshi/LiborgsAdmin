package com.liborgsadmin.android.fragment;

/**
 * Created by sakshiagarwal on 16/11/15.
 */

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.liborgsadmin.Datahelpers.DataHelper;
import com.liborgsadmin.Datahelpers.RequestHelper;
import com.liborgsadmin.Datahelpers.adapters.Tab2Adapter;
import com.liborgsadmin.android.R;
import com.liborgsadmin.android.SearchActivity;
import com.liborgsadmin.interfaces.ResponseInterface;
import com.liborgsadmin.utils.AppUtils;
import com.liborgsadmin.utils.ApplicationConstants;
import com.liborgsadmin.utils.SharedPreferences;

import org.json.JSONObject;


public class AllBooksFragment extends Fragment{

    RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_all_books, container, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        return recyclerView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        getAllBooksDetail();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_navigation, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = null;
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
            searchView.setQueryHint(this.getString(R.string.search_text));
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextChange(String newText) {
                    return true;
                }

                @Override
                public boolean onQueryTextSubmit(String query) {
                    String url = Uri.parse(ApplicationConstants.URL_SEARCH_QUERY)
                            .buildUpon()
                            .appendQueryParameter("query", query)
                            .build().toString();
                    Intent searchIntent = new Intent(getActivity(), SearchActivity.class);
                    searchIntent.putExtra("queryurl", url);
                    startActivity(searchIntent);
                    return true;
                }
            };

            searchView.setOnQueryTextListener(queryTextListener);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:

                // Not implemented here
                return false;
        }

        return false;
    }

    private void getAllBooksDetail()
    {
        AppUtils.getInstance().showProgressDialog(getActivity(), "", getString(R.string.loading));
        new RequestHelper().getAllBooksFromServer(new SharedPreferences().getAuthKey(getActivity()), new ResponseInterface() {

            @Override
            public void onSuccess(JSONObject jsonObject) {
                AppUtils.getInstance().dismissProgressDialog();
                Tab2Adapter adapter = new Tab2Adapter(getContext());
                adapter.setDataList(new DataHelper().getAllBooks(jsonObject));
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(String message) {
                AppUtils.getInstance().dismissProgressDialog();
                AppUtils.getInstance().showAlertDialogBox(getActivity(), message);
            }
        });
    }
}

