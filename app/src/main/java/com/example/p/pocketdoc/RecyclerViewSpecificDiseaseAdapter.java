package com.example.p.pocketdoc;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by P on 07-04-2017.
 */

public class RecyclerViewSpecificDiseaseAdapter extends RecyclerView.Adapter<RecyclerViewSpecificDiseaseAdapter.ViewHolder>  {

    Context context;
    LayoutInflater inflater;
    ArrayList<String> data = new ArrayList<String>();

    public RecyclerViewSpecificDiseaseAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setData(String temp)
    {
        data.add(temp);
        notifyItemRangeChanged(0,data.size());
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.singlerowspecificdisease,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String temp = data.get(position);
        holder.textView.setText(temp);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.specifcDiseaseText);
        }
    }
}
