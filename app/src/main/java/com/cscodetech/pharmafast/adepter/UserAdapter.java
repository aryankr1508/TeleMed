package com.cscodetech.pharmafast.adepter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cscodetech.pharmafast.R;
import com.cscodetech.pharmafast.model.Testimonial;
import com.cscodetech.pharmafast.retrofit.APIClient;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {
    private Context mContext;
    private List<Testimonial> mTestimonial;
    private RecyclerTouchListener listener;

    public interface RecyclerTouchListener {
        public void onClickUserItem(String titel, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtName;
        public TextView txtDesignetion;
        public CircleImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            txtName = (TextView) view.findViewById(R.id.txt_name);
            txtDesignetion = (TextView) view.findViewById(R.id.txt_designetion);
            thumbnail = view.findViewById(R.id.profile_image);
        }
    }

    public UserAdapter(Context mContext, List<Testimonial> mTestimonial, final RecyclerTouchListener listener) {
        this.mContext = mContext;
        this.mTestimonial = mTestimonial;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        Testimonial testimonial = mTestimonial.get(position);
        holder.txtName.setText(testimonial.getTitle());
        holder.txtDesignetion.setText(testimonial.getComment());
        Glide.with(mContext).load(APIClient.baseUrl + "/" + testimonial.getImg()).into(holder.thumbnail);
        holder.thumbnail.setOnClickListener(v -> listener.onClickUserItem(testimonial.getTitle(), Integer.parseInt(testimonial.getId())));
    }

    @Override
    public int getItemCount() {
        return mTestimonial.size();
    }
}