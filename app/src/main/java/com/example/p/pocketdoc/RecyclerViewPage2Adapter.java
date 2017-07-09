package com.example.p.pocketdoc;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.ArrayList;

/**
 * Created by P on 05-04-2017.
 */

public class RecyclerViewPage2Adapter extends RecyclerView.Adapter<RecyclerViewPage2Adapter.ViewHold> {

    Context context;
    LayoutInflater inflater;
    ArrayList<Symptoms> data = new ArrayList<>();
    ArrayList<Integer> ans = new ArrayList<>();

    public RecyclerViewPage2Adapter(Context context) {
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
    public ViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.singlerowpage2,parent,false);
        ViewHold holder = new ViewHold(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHold holder, int position) {
        Symptoms temp = data.get(position);
        holder.checkBox.setText(temp.Sname);
        if(ans.contains(temp.Sid))
        {
            holder.checkBox.setChecked(true);
        }
        else
        {
            holder.checkBox.setChecked(false);
        }
    }

    public ArrayList<Integer> getAnswer()
    {
        return ans;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHold extends RecyclerView.ViewHolder{

        CheckBox checkBox;
        public ViewHold(View itemView) {
            super(itemView);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);


            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b)
                    {
                        Symptoms temp = data.get(getAdapterPosition());
                        int sid = temp.Sid;
                        if(!ans.contains(sid))
                        {
                            ans.add(sid);
                        }

                    }
                    else
                    {
                        Symptoms temp = data.get(getAdapterPosition());
                        int sid = temp.Sid;
                        int index = ans.indexOf(sid);
                        if(index>=0)
                        {
                            ans.remove(index);
                        }
                    }
                }
            });

        }

    }
}
