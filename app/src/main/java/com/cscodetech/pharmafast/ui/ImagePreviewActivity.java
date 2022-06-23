package com.cscodetech.pharmafast.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.cscodetech.pharmafast.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImagePreviewActivity extends RootActivity implements View.OnTouchListener {

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tabview)
    TabLayout tabview;
    int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_prive);
        ButterKnife.bind(this);
        ArrayList<String> myList = (ArrayList<String>) getIntent().getSerializableExtra("imagelist");
        position = getIntent().getIntExtra("position", 0);

        if (myList != null && myList.size() != 0) {
            tabview.setupWithViewPager(viewPager, true);
        }
        MyCustomPagerAdapter myCustomPagerAdapter = new MyCustomPagerAdapter(this, myList);
        viewPager.setAdapter(myCustomPagerAdapter);
        viewPager.setCurrentItem(position);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        return false;
    }

    public class MyCustomPagerAdapter extends PagerAdapter {
        Context context;
        ArrayList<String> imageList;
        LayoutInflater layoutInflater;

        public MyCustomPagerAdapter(Context context, ArrayList<String> bannerDatumList) {
            this.context = context;
            this.imageList = bannerDatumList;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View itemView = layoutInflater.inflate(R.layout.item_imageview, container, false);
            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }


}