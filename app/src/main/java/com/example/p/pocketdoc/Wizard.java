package com.example.p.pocketdoc;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.Toast;

import com.eftimoff.viewpagertransformers.DrawFromBackTransformer;

import java.lang.reflect.Field;

import me.relex.circleindicator.CircleIndicator;

public class Wizard extends AppCompatActivity {

    ViewPager pager;
    ViewPagerAdapter pagerAdapter;
    CircleIndicator indicator;
    Button btnNext;
    RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wizard);

        layout = (RelativeLayout) findViewById(R.id.activity_wizard);

        btnNext = (Button) findViewById(R.id.next);
        btnNext.setEnabled(false);
        btnNext.setText("");

        indicator = (CircleIndicator) findViewById(R.id.indicator);

        pager = (ViewPager) findViewById(R.id.viewPager);

        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        pager.setAdapter(pagerAdapter);

        Field mScroller = null;

        try {
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            Scroller scroller = new Scroller(this, new DecelerateInterpolator(1.0f));
            mScroller.set(pager, scroller);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    btnNext.setEnabled(false);
                    btnNext.setText("");
                    layout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
                } else if (position == 1) {
                    btnNext.setEnabled(false);
                    btnNext.setText("");
                    layout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.Primary));
                } else if (position == 2) {
                    btnNext.setEnabled(true);
                    btnNext.setText("Finish");
                    layout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        pager.setPageTransformer(true, new DrawFromBackTransformer());

        indicator.setViewPager(pager);

    }


    public void OnNext(View v) {
        Intent intent = new Intent(this,Login.class);
        startActivity(intent);
        this.finish();
    }
}

