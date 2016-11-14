package com.liborgsadmin.android;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CompleteActivity extends AppCompatActivity {

    private static final String TAG = "CompleteActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.expandedappbar);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            //bookname
            collapsingToolbar.setTitle(extras.getString("bookName"));

            //authors
            ArrayList<String> authorNameList = extras.getStringArrayList("authorName");
            if (authorNameList != null) {
                String authorName = "By: " + authorNameList.get(0);
                for (int i = 1; i < authorNameList.size(); i++) {
                    authorName += (", " + authorNameList.get(i));
                }
                ((TextView) findViewById(R.id.authorname)).setText(authorName);
            }

            //description
            ((TextView) findViewById(R.id.description)).setText(extras.getString("description"));

            //categories
            ArrayList<String> categoriesList = extras.getStringArrayList("categories");
            if (categoriesList != null) {
                String categories = categoriesList.get(0);
                for (int i = 1; i < categoriesList.size(); i++) {
                    categories +=  (", " + categoriesList.get(i));
                }
                ((TextView) findViewById(R.id.categories)).setText(categories);
            }

            //publisher
            if (extras.getString("publisherName") != null)
                ((TextView) findViewById(R.id.publisher)).setText(extras.getString("publisherName"));

            //pagecount
            if (extras.getString("pageCount") != null)
                ((TextView) findViewById(R.id.pageCount)).setText(extras.getString("pageCount") + " pages");

            //subtitles
            if (extras.getString("subtitle") != null)
                ((TextView) findViewById(R.id.subtitle)).setText(extras.getString("subtitle"));

            //image loading
            Glide.with(this).load(extras.getString("imageUrl")).asBitmap().into((ImageView) findViewById(R.id.bookimage));

            //book availability
            if (extras.getInt("available") > 0)
                ((TextView) findViewById(R.id.available)).setText("Available");
            else
                ((TextView) findViewById(R.id.available)).setText("Not Available");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return true;
    }
}
