package com.example.p.pocketdoc;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

public class NearbyHospitalDetail extends AppCompatActivity {


    Toolbar toolbar;
    CardView cardViewLocation,cardViewContact;
    TextView textViewLocation,textViewContact;
    ImageView imageView;
    HospitalData data;
    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_hospital_detail);

        byte[] b = getIntent().getByteArrayExtra("images");
        data = (HospitalData) getIntent().getSerializableExtra("data");
        toolbar = (Toolbar) findViewById(R.id.toolbarNearbyHospitalDetail);
        imageView = (ImageView) findViewById(R.id.imageNearbyHospitalDetail);

        toolbar.setTitle(data.hname);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String url = data.himage;
        final ImageLoader imageLoader = SingletonRequest.getInstance(this.getApplicationContext()).getImageLoader();
        imageLoader.get(url, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                imageView.setImageBitmap(response.getBitmap());
            }
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NoConnectionError || error instanceof NetworkError) {
                    Toast.makeText(context, "Please Check your Internet Connection!!!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Error in Image", Toast.LENGTH_SHORT).show();
                }
            }
        });

        textViewLocation = (TextView) findViewById(R.id.nearbyHospitalDetailInformation1);
        textViewContact  = (TextView) findViewById(R.id.nearbyHospitalDetailInformation2);

        cardViewContact = (CardView) findViewById(R.id.nearbyHospitalDetailCard2);
        cardViewLocation = (CardView) findViewById(R.id.nearbyHospitalDetailCard1);

        textViewContact.setText(data.hcontact);
        textViewLocation.setText(data.address);

        textViewContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String contact = data.hcontact;
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + contact));
                startActivity(intent);
            }
        });
        final String lat = data.hlat;
        final String lng = data.hlong;
        final String label = data.hname;
        textViewLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String format = "geo:0,0?q=" + lat + "," + lng + "(" + label + ")";
                Uri uri = Uri.parse(format);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

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




