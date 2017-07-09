package com.example.p.pocketdoc;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by P on 06-04-2017.
 */

public class RecyclerViewPage3Adapter extends RecyclerView.Adapter<RecyclerViewPage3Adapter.ViewHolder>  {

    Context context;
    LayoutInflater inflater;
    ArrayList<Diseases> data = new ArrayList<Diseases>();
    public RecyclerViewPage3Adapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setData(Diseases temp)
    {
        data.add(temp);
        notifyItemRangeChanged(0,data.size());
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.singlerowpage1,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Diseases temp = data.get(position);
        holder.textView.setText(temp.Dname);
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.specifcDiseaseText);
            textView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Diseases temp = data.get(getAdapterPosition());
            int did = temp.Did;
            String dname = temp.Dname;
            Intent intent = new Intent(context,ViewSpecificDisease.class);
            intent.putExtra("did",did);
            intent.putExtra("dname",dname);
            context.startActivity(intent);
        }
    }
}
