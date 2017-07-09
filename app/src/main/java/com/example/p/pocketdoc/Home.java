package com.example.p.pocketdoc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    NavigationView navigationView;
    RecyclerView recyclerView;
    RecyclerViewHomeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayFirstAid);

        toolbar = (Toolbar) findViewById(R.id.toolbarHome);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapseHome);
        collapsingToolbarLayout.setTitle("Pocket DOC");

        navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);
        setSupportActionBar(toolbar);

        drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.Open,R.string.Close);

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerHome);

        adapter = new RecyclerViewHomeAdapter(this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.home) {
            Toast.makeText(Home.this, "Home Selected", Toast.LENGTH_SHORT).show();
            drawerLayout.closeDrawers();
        } else if (item.getItemId() == R.id.contactUs) {
            Intent intent = new Intent(this,AboutUs.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.logout) {
            Toast.makeText(Home.this, "Logout", Toast.LENGTH_SHORT).show();
            SharedPreferences sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("uid");
            editor.remove("token");
            editor.remove("role");
            editor.commit();
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();

        } else if (item.getItemId() == R.id.exit) {
            drawerLayout.closeDrawers();
            this.finish();
        }
        return false;
    }
}
