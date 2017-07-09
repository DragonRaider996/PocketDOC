package com.example.p.pocketdoc;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by P on 14-04-2017.
 */

public class ViewPagerAdapterImageAnatomy extends FragmentStatePagerAdapter {

    ArrayList<String> data = new ArrayList<String>();

    public ViewPagerAdapterImageAnatomy(FragmentManager fm,ArrayList<String> temp) {
        super(fm);
        data = temp;
    }

    @Override
    public Fragment getItem(int position) {
        String url = data.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("url",url);
        Fragment frag = new FragImageAnatomy();
        frag.setArguments(bundle);
        return frag;
    }

    @Override
    public int getCount() {
        return data.size();
    }
}
