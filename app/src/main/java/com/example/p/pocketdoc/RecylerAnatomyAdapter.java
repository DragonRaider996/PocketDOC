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
 * Created by P on 09-04-2017.
 */

public class RecylerAnatomyAdapter extends RecyclerView.Adapter<RecylerAnatomyAdapter.ViewHolder> {

    Context context;
    LayoutInflater inflater;
    ArrayList<AnatomyData> data = new ArrayList<AnatomyData>();
    public RecylerAnatomyAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setData(AnatomyData temp)
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
        AnatomyData temp = data.get(position);
        holder.textView.setText(temp.hatopic);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.specifcDiseaseText);
            textView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            AnatomyData temp = data.get(getAdapterPosition());
            String hatopic = temp.hatopic;
            int haid = temp.haid;

            Intent intent = new Intent(context,SpecificAnatomy.class);
            intent.putExtra("hatopic",hatopic);
            intent.putExtra("haid",haid);
            context.startActivity(intent);
        }
    }
}
