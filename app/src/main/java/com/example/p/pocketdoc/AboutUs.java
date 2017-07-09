package com.example.p.pocketdoc;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class AboutUs extends AppCompatActivity implements View.OnClickListener{

    TextView textView;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        textView = (TextView) findViewById(R.id.aboutUsText2);

        toolbar = (Toolbar) findViewById(R.id.aboutUsToolbar);
        toolbar.setTitle("Contact Us");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        textView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto","anant.130410116086@gmail.com", null));
        startActivity(emailIntent);
    }
}
