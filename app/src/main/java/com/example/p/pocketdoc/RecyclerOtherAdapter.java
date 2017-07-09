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

/**
 * Created by P on 10-04-2017.
 */

public class RecyclerOtherAdapter extends RecyclerView.Adapter<RecyclerOtherAdapter.ViewHolder>{

    Context context;
    LayoutInflater inflater;
    int [] values ={R.drawable.bmi,R.drawable.forum};
    String [] data = {"Body Mass Index","Forum"};

    public RecyclerOtherAdapter(Context context) {
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
        holder.imageView.setImageResource(values[position]);
        holder.textView.setText(data[position]);
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imageView;
        TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.singlerowhomeImage);
            textView = (TextView) itemView.findViewById(R.id.imageTextHomeFirst);
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
            String temp = data[getAdapterPosition()];
            if(temp.equals("Body Mass Index"))
            {
                Intent intent = new Intent(context,BMI.class);
                context.startActivity(intent);
            }
            else
            {
                Intent intent = new Intent(context,ForumMain.class);
                context.startActivity(intent);
            }
        }
    }
}
