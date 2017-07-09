package com.example.p.pocketdoc;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by P on 04-04-2017.
 */

public class RecyclerViewHomeAdapter extends RecyclerView.Adapter<RecyclerViewHomeAdapter.ViewHolder> {

    Context context;
    LayoutInflater inflater;
    int[] images = {R.drawable.symptomchecker1,R.drawable.diseases1,R.drawable.hospital,R.drawable.humananatomy,R.drawable.firstaid,R.drawable.forum};
    String[] first = {"Symptom Checker","View Diseases","Nearby Hospital","Human Anatomy","First Aid","Other Stuffs"};

    public RecyclerViewHomeAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.singlerowhome,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.imageView.setImageResource(images[position]);
        holder.textViewFirst.setText(first[position]);
    }

    @Override
    public int getItemCount() {
        return 6;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imageView;
        TextView textViewFirst;
        public ViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.singlerowhomeImage);
            textViewFirst = (TextView) itemView.findViewById(R.id.imageTextHomeFirst);
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            int width = displayMetrics.widthPixels;
            int height = displayMetrics.heightPixels;

            imageView.getLayoutParams().height = (height)/3-40;
            imageView.getLayoutParams().width = (width/2)-20;

            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            imageView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            String name = first[getAdapterPosition()];

            if(name.equals(first[0]))
            {
                Intent intent = new Intent(context,SymptomCheckerPage1.class);
                context.startActivity(intent);
            }
            if(name.equals(first[1]))
            {
                Intent intent = new Intent(context,ViewDisease.class);
                context.startActivity(intent);
            }

            if (name.equals(first[2]))
            {
                Intent intent = new Intent(context,NearbyHospital.class);
                context.startActivity(intent);
            }

            if(name.equals(first[3]))
            {
                Intent intent = new Intent(context,Anatomy.class);
                context.startActivity(intent);
            }

            if(name.equals(first[4]))
            {
                Intent intent = new Intent(context,FirstAid.class);
                context.startActivity(intent);
            }

            if(name.equals(first[5]))
            {
                Intent intent = new Intent(context,Other.class);
                context.startActivity(intent);
            }
        }
    }
}
