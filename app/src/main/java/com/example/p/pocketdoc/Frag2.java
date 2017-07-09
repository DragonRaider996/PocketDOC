package com.example.p.pocketdoc;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by P on 28-03-2017.
 */

public class Frag2 extends Fragment {

    ImageView imageView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag2,container,false);
        imageView = (ImageView) view.findViewById(R.id.frag2image);
        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        imageView.getLayoutParams().height = ((height/2) + (height)/6) ;
        imageView.getLayoutParams().width = (width);

        return view;
    }
}
