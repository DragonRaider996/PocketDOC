package com.example.p.pocketdoc;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;

/**
 * Created by P on 09-04-2017.
 */

public class RecyclerSpecificAnatomyAdapter extends RecyclerView.Adapter<RecyclerSpecificAnatomyAdapter.ViewHolder>{

    Context context;
    LayoutInflater inflater;
    ArrayList<String> data = new ArrayList<String>();
    String title;
    public RecyclerSpecificAnatomyAdapter(Context context,String title) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.title = title;
    }

    public void setData(String temp)
    {
        data.add(temp);
        notifyItemRangeChanged(0,data.size());
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.singlerowanatomy,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        String url = data.get(position);
        final ImageLoader imageLoader = SingletonRequest.getInstance(context.getApplicationContext()).getImageLoader();
        imageLoader.get(url, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                holder.imageView.setImageBitmap(response.getBitmap());
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
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.singleRowAnatomyImage);
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            int width = displayMetrics.widthPixels;
            int height = displayMetrics.heightPixels;

            imageView.getLayoutParams().height = (height)/3-40;
            imageView.getLayoutParams().width = (width/2)-10;

            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context,ViewImage.class);
            intent.putExtra("url",data);
            intent.putExtra("title",title);
            context.startActivity(intent);
        }
    }
}
