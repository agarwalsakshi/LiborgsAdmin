package com.liborgsadmin.android;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.liborgsadmin.Datahelpers.RequestHelper;
import com.liborgsadmin.android.fragment.AllBooksFragment;
import com.liborgsadmin.android.fragment.ReturnedBooksFragment;
import com.liborgsadmin.interfaces.ResponseInterface;
import com.liborgsadmin.utils.AppUtils;
import com.liborgsadmin.utils.SharedPreferences;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class BooksDataActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        setupViewPager(viewPager);

        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setHomeAsUpIndicator(R.mipmap.menu);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        shareRegistrationId();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        menuItem.setChecked(true);
                        switch (menuItem.getItemId()) {

                            case R.id.requested_book:
                                AppUtils.getInstance().pageTransition(BooksDataActivity.this, RequestedBooksActivity.class);
                                break;

                            case R.id.upload:
                                AppUtils.getInstance().pageTransition(BooksDataActivity.this, MainActivity.class);
                                break;

                            case R.id.logout:
                                new SharedPreferences().deleteAuthKey(BooksDataActivity.this);
                                AppUtils.getInstance().pageTransition(BooksDataActivity.this, LoginActivity.class);
                                break;

                            case R.id.action_settings:
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.come_soon_text), Toast.LENGTH_SHORT).show();
                                break;

                            case R.id.all_users:
                                AppUtils.getInstance().pageTransition(BooksDataActivity.this, AllUsersActivity.class);
                                break;

                            default:
                                return true;
                        }
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }


    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new AllBooksFragment(), getResources().getString(R.string.all_books_text));
        adapter.addFragment(new ReturnedBooksFragment(), getResources().getString(R.string.pending_text));
        viewPager.setAdapter(adapter);
    }


    private void shareRegistrationId()
    {
        new RequestHelper().shareRegIdWithAppServer(new SharedPreferences().getGCMRegId(this),
                new SharedPreferences().getAuthKey(this), new ResponseInterface() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                    }

                    @Override
                    public void onFailure(String message) {
                    }
                });
    }

    class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;

            case R.id.action_setting:
                Toast.makeText(BooksDataActivity.this, getResources().getString(R.string.come_soon_text), Toast.LENGTH_SHORT).show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

}