package com.example.appandroiddans;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class PositionAdapter extends RecyclerView.Adapter<PositionAdapter.PositionHolder>
{
    private Context context;
    private List<Position> positionList;

    public PositionAdapter(Context context, List<Position> positions){
        this.context = context;
        positionList = positions;
    }

    public void filteredlistjob(List<Position> filterposistion) {
        this.positionList = filterposistion;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PositionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_job, parent, false);
        return new PositionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PositionHolder holder, int position) {
        Position position1 = positionList.get(position);
        holder.Title_tv.setText(position1.getTitle().toString());
        holder.Company_tv.setText(position1.getCompany().toString());
        holder.Location_tv.setText(position1.getLocation().toString());
        Glide.with(context)
                .load(position1.getCompany_logo())
                .into(holder.img_profile_company);

        holder.detail_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, detail_act.class);
                Bundle bundle = new Bundle();
                bundle.putString("title", position1.getTitle());
                bundle.putString("company", position1.getCompany());
                bundle.putString("location", position1.getLocation());
                bundle.putString("url", position1.getUrl());
                bundle.putString("description", position1.getDescription());
                bundle.putString("type", position1.getType());
                bundle.putString("company_logo", position1.getCompany_logo());

                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return positionList.size();
    }
    public void setPositions(List<Position> positions) {
        this.positionList = positions;
        notifyDataSetChanged();
    }

    public class PositionHolder extends RecyclerView.ViewHolder{

        ImageView img_profile_company;
        TextView Title_tv, Company_tv, Location_tv;
        CardView detail_item;
        public PositionHolder(@NonNull View itemView) {
            super(itemView);

            img_profile_company = itemView.findViewById(R.id.img_profile_company);
            Title_tv = itemView.findViewById(R.id.Title_tv);
            Company_tv = itemView.findViewById(R.id.Company_tv);
            Location_tv = itemView.findViewById(R.id.Location_tv);
            detail_item = itemView.findViewById(R.id.detail_item);
        }
    }


}
