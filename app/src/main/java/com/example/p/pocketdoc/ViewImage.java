package com.example.p.pocketdoc;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase;
import me.relex.circleindicator.CircleIndicator;


public class ViewImage extends AppCompatActivity {

    ArrayList<String> url  = new ArrayList<String>();
    Context context = this;
    ViewPager viewPager;
    ViewPagerAdapterImageAnatomy adapter;
    CircleIndicator indicator;
    Toolbar toolbar;
    String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);
        url = getIntent().getStringArrayListExtra("url");
        title = getIntent().getStringExtra("title");
        viewPager = (ViewPager) findViewById(R.id.viewPagerImageAnatomy);
        toolbar = (Toolbar) findViewById(R.id.viewImageToolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        adapter = new ViewPagerAdapterImageAnatomy(getSupportFragmentManager(),url);
        indicator = (CircleIndicator) findViewById(R.id.indicatorImageAnatomy);
        viewPager.setAdapter(adapter);
        indicator.setViewPager(viewPager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home)
        {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
