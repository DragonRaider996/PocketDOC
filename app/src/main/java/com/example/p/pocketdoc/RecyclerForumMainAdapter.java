package com.example.p.pocketdoc;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by P on 12-04-2017.
 */

public class RecyclerForumMainAdapter  extends RecyclerView.Adapter<RecyclerForumMainAdapter.ViewHolder>{

    Context context;
    LayoutInflater inflater;
    ArrayList<ForumData> data = new ArrayList<ForumData>();

    public RecyclerForumMainAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setData(ForumData temp)
    {
        data.add(temp);
        notifyItemRangeChanged(0,data.size());
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.singlerowforummain,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ForumData temp = data.get(position);
        holder.description.setText(temp.description);
        holder.question.setText(temp.question);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView question,description;
        RelativeLayout layout;
        public ViewHolder(View itemView) {
            super(itemView);
            question = (TextView) itemView.findViewById(R.id.question);
            description = (TextView) itemView.findViewById(R.id.description);
            question.setOnClickListener(this);
            description.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            ForumData temp = data.get(getAdapterPosition());
            int fid = temp.fid;
            Intent intent = new Intent(context,ForumDetail.class);
            intent.putExtra("fid",fid);
            context.startActivity(intent);
        }
    }
}
