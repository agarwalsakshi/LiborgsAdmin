package com.liborgsadmin.android;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liborgsadmin.Datahelpers.DataHelper;
import com.liborgsadmin.Datahelpers.RequestHelper;
import com.liborgsadmin.datamodel.AllUsers;
import com.liborgsadmin.interfaces.ResponseInterface;
import com.liborgsadmin.utils.AppUtils;
import com.liborgsadmin.utils.SharedPreferences;

import org.json.JSONObject;

import java.util.List;

public class AllUsersActivity extends AppCompatActivity{

    RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.all_users);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setHomeAsUpIndicator(R.mipmap.left_arrow);
        }

        recyclerView = (RecyclerView) findViewById(R.id.all_users);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        getUserDetails();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                AppUtils.getInstance().pageTransition(this, BooksDataActivity.class);
        }
        return true;
    }

    private void getUserDetails()
    {
        AppUtils.getInstance().showProgressDialog(this, "", getString(R.string.loading));
        new RequestHelper().getAllUsersDetails(new SharedPreferences().getAuthKey(this), new ResponseInterface() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                AppUtils.getInstance().dismissProgressDialog();
                List<AllUsers> allUsers = new DataHelper().getAllUsers(AllUsersActivity.this, jsonObject);
                if(allUsers.size() >= 1) {
                    AllUsersAdapter adapter = new AllUsersAdapter(allUsers);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(String message) {
                AppUtils.getInstance().dismissProgressDialog();
                AppUtils.getInstance().showAlertDialogBox(AllUsersActivity.this, message);
            }
        });
    }

    private class AllUsersAdapter extends RecyclerView.Adapter<AllUsersAdapter.ViewHolder> {
        private List<AllUsers> mList;

        AllUsersAdapter(List<AllUsers> allUsersList)
        {
            mList = allUsersList;
        }

        @Override
        public void onBindViewHolder(AllUsersAdapter.ViewHolder holder, int position) {
            AllUsers allUsers = mList.get(position);
            holder.userName.setText(allUsers.getUserName());
            holder.userEmail.setText(allUsers.getUserEmail());
        }

        @Override
        public AllUsersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_all_users, parent, false);
            return new AllUsersAdapter.ViewHolder(view);
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView userName, userEmail;

            ViewHolder(View itemView) {
                super(itemView);

                userName = (TextView) itemView.findViewById(R.id.user_name);
                userEmail = (TextView) itemView.findViewById(R.id.user_email);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // do nothing
                    }
                });
            }
        }
    }
}
