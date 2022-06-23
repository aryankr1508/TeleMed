package com.cscodetech.pharmafast.adepter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cscodetech.pharmafast.R;
import com.cscodetech.pharmafast.model.Catlist;
import com.cscodetech.pharmafast.retrofit.APIClient;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    private Context mContext;
    private List<Catlist> mCatlist;
    private RecyclerTouchListener listener;
    private String typeview;

    public interface RecyclerTouchListener {
        public void onClickCategoryItem(String titel, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView thumbnail;
        public LinearLayout lvlclick;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.txt_title);
            thumbnail = view.findViewById(R.id.imageView);
            lvlclick = view.findViewById(R.id.lvl_itemclick);

        }
    }

    public CategoryAdapter(Context mContext, List<Catlist> mCatlist, final RecyclerTouchListener listener, String viewtype) {
        this.mContext = mContext;
        this.mCatlist = mCatlist;
        this.listener = listener;
        this.typeview = viewtype;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (typeview.equalsIgnoreCase("viewall")) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_categoryviewall, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_categoryviewall, parent, false);
        }

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        Catlist category = mCatlist.get(position);
        holder.title.setText(category.getCatname() + "(" + category.getCount() + ")");
        Glide.with(mContext).load(APIClient.baseUrl + "/" + category.getCatimg()).into(holder.thumbnail);
        holder.lvlclick.setOnClickListener(v -> {
            if (category.getCount() == 0) {
                Toast.makeText(mContext, "Product Not Found !!!", Toast.LENGTH_SHORT).show();
            } else {
                listener.onClickCategoryItem(category.getCatname(), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCatlist.size();
    }
}