package com.example.p.pocketdoc;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by P on 11-04-2017.
 */

public class RecyclerNearbyHospitalAdapter extends RecyclerView.Adapter<RecyclerNearbyHospitalAdapter.ViewHolder> {

    Context context;
    LayoutInflater inflater;
    ArrayList<HospitalData> data = new ArrayList<HospitalData>();
    public RecyclerNearbyHospitalAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setData(HospitalData temp)
    {
        data.add(temp);
        notifyItemRangeChanged(0,data.size());
        notifyDataSetChanged();
    }

    public void clearData()
    {
        data = new ArrayList<HospitalData>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.singlerownearbyhospital,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }



    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        HospitalData temp = data.get(position);
        String hname = temp.hname;
        String url = temp.himage;

        holder.textView.setText(hname);
        final ImageLoader imageLoader = SingletonRequest.getInstance(context.getApplicationContext()).getImageLoader();
        imageLoader.get(url, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                holder.imageView.setImageBitmap(response.getBitmap());
            }
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NoConnectionError || error instanceof NetworkError) {
                    Toast.makeText(context, "Please Check your Internet Connection!!!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Error in Image", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        CardView cardView;
        ImageView imageView;
        TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textNearbyHospital);
            imageView = (ImageView) itemView.findViewById(R.id.imageNearbyHospital);
            cardView = (CardView) itemView.findViewById(R.id.cardNearbyHospital);
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            HospitalData temp = data.get(getAdapterPosition());
            Intent intent = new Intent(context,NearbyHospitalDetail.class);
            intent.putExtra("data",temp);
            context.startActivity(intent);

        }
    }
}
