package com.cscodetech.pharmafast.adepter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cscodetech.pharmafast.R;
import com.cscodetech.pharmafast.model.Brand;
import com.cscodetech.pharmafast.retrofit.APIClient;

import java.util.List;

public class FeatureBrandAdapter extends RecyclerView.Adapter<FeatureBrandAdapter.MyViewHolder> {
    private Context mContext;
    private List<Brand> mBrand;
    private RecyclerTouchListener listener;
    private String typeView;

    public interface RecyclerTouchListener {
        public void onClickFeaturItem(String titel, String position);

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.txt_title);
            thumbnail = view.findViewById(R.id.imageView);
        }
    }

    public FeatureBrandAdapter(Context mContext, List<Brand> mBrand, final RecyclerTouchListener listener, String typeview) {
        this.mContext = mContext;
        this.mBrand = mBrand;
        this.listener = listener;
        this.typeView = typeview;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (typeView.equalsIgnoreCase("viewall")) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_allfeatur, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_featur, parent, false);
        }
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        Brand brand = mBrand.get(position);
        holder.title.setText(brand.getBname());
        Glide.with(mContext).load(APIClient.baseUrl + "/" + brand.getImg()).thumbnail(Glide.with(mContext).load(R.drawable.ezgifresize)).into(holder.thumbnail);
        holder.thumbnail.setOnClickListener(v -> listener.onClickFeaturItem(brand.getBname(), brand.getId()));
    }

    @Override
    public int getItemCount() {
        return mBrand.size();
    }
}