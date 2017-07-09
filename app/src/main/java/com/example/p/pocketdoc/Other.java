package com.example.p.pocketdoc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

public class Other extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    RecyclerOtherAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);

        toolbar = (Toolbar) findViewById(R.id.otherToolbar);
        recyclerView = (RecyclerView) findViewById(R.id.otherRecycler);
        adapter = new RecyclerOtherAdapter(this);

        toolbar.setTitle("Others");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
    }
}

