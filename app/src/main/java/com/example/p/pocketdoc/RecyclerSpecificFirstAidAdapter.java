package com.example.p.pocketdoc;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by P on 08-04-2017.
 */

public class RecyclerSpecificFirstAidAdapter extends RecyclerView.Adapter<RecyclerSpecificFirstAidAdapter.ViewHolder> {

    Context context;
    LayoutInflater inflater;
    ArrayList<FirstAidData> data = new ArrayList<FirstAidData>();
    public RecyclerSpecificFirstAidAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setData(FirstAidData temp)
    {
        data.add(temp);
        notifyItemRangeChanged(0,data.size());
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.singlerowfirstaid,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FirstAidData temp = data.get(position);
        int pos = position+1;
        String post = String.valueOf(pos);
        holder.number.setText(post);
        holder.answer.setText(temp.detail);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView number,answer;
        public ViewHolder(View itemView) {
            super(itemView);
            number = (TextView) itemView.findViewById(R.id.specifcNumberText);
            answer = (TextView) itemView.findViewById(R.id.specificStepText);
        }
    }
}
