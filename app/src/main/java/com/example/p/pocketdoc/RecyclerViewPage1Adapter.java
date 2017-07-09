package com.example.p.pocketdoc;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by P on 05-04-2017.
 */

public class RecyclerViewPage1Adapter extends RecyclerView.Adapter<RecyclerViewPage1Adapter.ViewHolder> {

    Context context;
    LayoutInflater inflater;
    ArrayList<Symptoms>data = new ArrayList<>();
    public RecyclerViewPage1Adapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setData(Symptoms temp)
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
        Symptoms temp = data.get(position);
        holder.textView.setText(temp.Sname);
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
            Symptoms temp = data.get(getAdapterPosition());
            int sid = temp.Sid;
            Intent intent = new Intent(context,SymptomCheckerPage2.class);
            intent.putExtra("sid",sid);
            context.startActivity(intent);
        }
    }
}
