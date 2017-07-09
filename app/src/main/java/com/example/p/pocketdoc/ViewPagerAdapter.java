package com.example.p.pocketdoc;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by P on 28-03-2017.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0)
        {
            return new Frag1();
        }
        else if(position == 1)
        {
            return new Frag2();
        }
        else
        {
            return new Frag3();
        }

    }

    @Override
    public int getCount() {
        return 3;
    }
}
