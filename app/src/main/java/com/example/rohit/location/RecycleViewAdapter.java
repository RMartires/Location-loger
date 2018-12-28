package com.example.rohit.location;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ROHIT on 12/25/2018.
 */

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder>{

    private ArrayList<String> latitude = new ArrayList<>();
    private ArrayList<String> longitude = new ArrayList<>();
    private Context mcontext;

    public RecycleViewAdapter(ArrayList<String> latitude, ArrayList<String> longitude ,Context context) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.mcontext=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.text1.setText(longitude.get(position));
        holder.text2.setText(latitude.get(position));

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("pos",String.valueOf(position));

                Uri LocationUri= Uri.parse(getlocation(longitude.get(position),latitude.get(position)));
                Intent locationIntent = new Intent(Intent.ACTION_VIEW,LocationUri);
                locationIntent.setPackage("com.google.android.apps.maps");
                view.getContext().startActivity(locationIntent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return latitude.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView text1,text2;
        RelativeLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);

            text1 = itemView.findViewById(R.id.longitude);
            text2 = itemView.findViewById(R.id.latitude);
            relativeLayout = itemView.findViewById(R.id.relativelayout);
        }
    }

    public String getlocation(String longitude,String latitude){
        String location=null;
        location="geo:"+latitude+","+longitude;

        return  location;
    }

}
