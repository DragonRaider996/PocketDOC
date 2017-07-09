package com.example.p.pocketdoc;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by P on 12-04-2017.
 */

public class RecyclerDetailForumAdapter extends RecyclerView.Adapter<RecyclerDetailForumAdapter.ViewHolder> {

    Context context;
    LayoutInflater inflater;
    ArrayList<Comments> data = new ArrayList<Comments>();

    public RecyclerDetailForumAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setData(Comments temp)
    {
        data.add(temp);
        notifyItemRangeChanged(0,data.size());
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.singlerowforumdetail,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Comments temp = data.get(position);
        holder.name.setText(temp.name);
        holder.comments.setText(temp.comment);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView name,comments;
        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.nameForumDetail);
            comments = (TextView) itemView.findViewById(R.id.textAnswerForumDetail);
        }
    }
}
