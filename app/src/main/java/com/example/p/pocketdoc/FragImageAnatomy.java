package com.example.p.pocketdoc;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

/**
 * Created by P on 14-04-2017.
 */

public class FragImageAnatomy extends Fragment {

    TouchImageView imageView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragimageanatomy,container,false);
        imageView = (TouchImageView) view.findViewById(R.id.fragImage);

        if(getArguments() != null)
        {
            String url = getArguments().getString("url");


            final ImageLoader imageLoader = SingletonRequest.getInstance(getActivity().getApplicationContext()).getImageLoader();
            imageLoader.get(url, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    imageView.setImageBitmap(response.getBitmap());
                }
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error instanceof NoConnectionError || error instanceof NetworkError) {
                        Toast.makeText(getActivity(), "Please Check your Internet Connection!!!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Error in Image", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
        else
        {
            Toast.makeText(getActivity(), "Sorry seems like something went wrong!!", Toast.LENGTH_SHORT).show();
        }
        return view;
    }
}
