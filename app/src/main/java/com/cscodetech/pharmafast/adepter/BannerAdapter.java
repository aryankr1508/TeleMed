package com.cscodetech.pharmafast.adepter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cscodetech.pharmafast.R;
import com.cscodetech.pharmafast.model.Banner;
import com.cscodetech.pharmafast.retrofit.APIClient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerHolder> {
    private Context context;
    private List<Banner> mBanner;
    private RecyclerTouchListener listener;

    public interface RecyclerTouchListener {
        public void onClickBannerItem(String titel, int position);
    }

    public BannerAdapter(Context context, List<Banner> mBanner, RecyclerTouchListener listener) {
        this.context = context;
        this.mBanner = mBanner;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BannerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_banner_item, parent, false);
        return new BannerHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull BannerHolder holder, int position) {

        Glide.with(context).load(APIClient.baseUrl + "/" + mBanner.get(position).getImg()).thumbnail(Glide.with(context).load(R.drawable.ezgifresize)).centerCrop().into(holder.imgBanner);
        holder.imgBanner.setOnClickListener(view -> listener.onClickBannerItem("Banner", mBanner.get(position).getPositions()));
    }
    @Override
    public int getItemCount() {
        return mBanner.size();
    }

    public class BannerHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_banner)
        ImageView imgBanner;

        public BannerHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}