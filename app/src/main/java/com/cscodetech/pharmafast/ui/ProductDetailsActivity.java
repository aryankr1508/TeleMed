package com.cscodetech.pharmafast.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cscodetech.pharmafast.R;
import com.cscodetech.pharmafast.model.Medicine;
import com.cscodetech.pharmafast.model.ProductPrice;
import com.cscodetech.pharmafast.retrofit.APIClient;
import com.cscodetech.pharmafast.utiles.DatabaseHelper;
import com.cscodetech.pharmafast.utiles.MyCart;
import com.cscodetech.pharmafast.utiles.SessionManager;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.cscodetech.pharmafast.utiles.SessionManager.currency;


public class ProductDetailsActivity extends RootActivity {
    @BindView(R.id.my_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.txt_price)
    TextView txtPrice;
    @BindView(R.id.txt_item_offer)
    TextView txtItemOffer;
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.txt_countcard)
    TextView txtCountcard;
    @BindView(R.id.txt_offer)
    TextView txtOffer;
    @BindView(R.id.lvl_offer)
    LinearLayout lvlOffer;
    @BindView(R.id.lvl_addcart)
    LinearLayout lvlAddcart;
    @BindView(R.id.txt_desc)
    TextView txtDesc;
    @BindView(R.id.txt_brandname)
    TextView txtBrandname;
    @BindView(R.id.btn_addtocart)
    TextView btnAddtocart;
    @BindView(R.id.txt_priceone)
    TextView txtPriceone;
    @BindView(R.id.lvl_spineer)
    LinearLayout lvlSpineer;
    @BindView(R.id.img_back)
    ImageView imgBack;
    Medicine medicine;
    ArrayList<String> productImage = new ArrayList<>();
    private ArrayList<ProductPrice> productInfo = new ArrayList<>();
    SessionManager sessionManager;
    DatabaseHelper helper;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        ButterKnife.bind(this);
        helper = new DatabaseHelper(ProductDetailsActivity.this);

        sessionManager = new SessionManager(ProductDetailsActivity.this);
        medicine = (Medicine) getIntent().getParcelableExtra("MyClass");
        productInfo = getIntent().getParcelableArrayListExtra("PriceList");
        productImage = getIntent().getStringArrayListExtra("ImageList");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            txtDesc.setText(Html.fromHtml(medicine.getShortDesc(), Html.FROM_HTML_MODE_LEGACY));
        } else {
            txtDesc.setText(Html.fromHtml(medicine.getShortDesc()));
        }
        txtTitle.setText("" + medicine.getProductName());
        txtBrandname.setText("" + medicine.getBrandName());
        LinearLayoutManager mLayoutManager1 = new LinearLayoutManager(ProductDetailsActivity.this);
        mLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(mLayoutManager1);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        BannerAdapter bannerAdapter = new BannerAdapter(ProductDetailsActivity.this, productImage);
        recyclerView.setAdapter(bannerAdapter);
        List<String> arealist = new ArrayList<>();
        if (productInfo.size() == 1) {
            txtPriceone.setText("" + productInfo.get(0).getProductType());
            lvlSpineer.setVisibility(View.GONE);
            setJoinPlayrList(lvlAddcart, productInfo.get(0));

        } else {
            for (int i = 0; i < productInfo.size(); i++) {
                arealist.add(productInfo.get(i).getProductType());
            }
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, R.layout.spinner_layout, arealist);
            spinner.setAdapter(dataAdapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    positontype = position;
                    setJoinPlayrList(lvlAddcart, productInfo.get(position));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }

    }

    @OnClick({R.id.img_back, R.id.rlt_cart, R.id.btn_addtocart})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_addtocart:
                finish();
                break;
            case R.id.rlt_cart:
                startActivity(new Intent(ProductDetailsActivity.this, CartActivity.class));
                break;
            default:
                break;
        }
    }

    private void setJoinPlayrList(LinearLayout lnrView, ProductPrice productPrice) {
        lnrView.removeAllViews();
        final int[] count = {0};
        LayoutInflater inflater = LayoutInflater.from(ProductDetailsActivity.this);
        View view = inflater.inflate(R.layout.custome_prize, null);
        TextView txtcount = view.findViewById(R.id.txtcount);
        LinearLayout txtOutstock = view.findViewById(R.id.txt_outstock);
        LinearLayout lvlAddremove = view.findViewById(R.id.lvl_addremove);
        LinearLayout lvlAddcart = view.findViewById(R.id.lvl_addcart);
        LinearLayout imgMins = view.findViewById(R.id.img_mins);
        LinearLayout imgPlus = view.findViewById(R.id.img_plus);
        MyCart myCart = new MyCart();
        myCart.setPid(medicine.getId());
        myCart.setProductName(medicine.getProductName());
        myCart.setProductPrice(productPrice.getProductPrice());
        myCart.setProductImage(medicine.getProductImage().get(0));
        myCart.setBrandName(medicine.getBrandName());
        myCart.setDiscount(productPrice.getProductDiscount());
        myCart.setShortDesc(medicine.getShortDesc());
        myCart.setAttributeId(productPrice.getAttributeId());
        myCart.setProductType(productPrice.getProductType());
        updateItem();

        if (productPrice.getProductInStock().equalsIgnoreCase("1")) {
            txtOutstock.setVisibility(View.VISIBLE);
            lvlAddremove.setVisibility(View.GONE);
            lvlAddcart.setVisibility(View.GONE);

        } else {
            int qrt = helper.getCard(myCart.getAttributeId());
            if (qrt != -1) {
                count[0] = qrt;
                txtcount.setText("" + count[0]);
                lvlAddremove.setVisibility(View.VISIBLE);
                lvlAddcart.setVisibility(View.GONE);
            } else {
                lvlAddremove.setVisibility(View.GONE);
                lvlAddcart.setVisibility(View.VISIBLE);
            }
        }
        if (productPrice.getProductDiscount() == 0) {
            lvlOffer.setVisibility(View.GONE);
            txtPrice.setText(sessionManager.getStringData(currency) + productPrice.getProductPrice());
            txtItemOffer.setVisibility(View.GONE);
        } else {
            lvlOffer.setVisibility(View.VISIBLE);
            txtItemOffer.setVisibility(View.VISIBLE);
            DecimalFormat format = new DecimalFormat("0.#");
            txtOffer.setText(format.format(productPrice.getProductDiscount()) + "% OFF");
            double res = (Double.parseDouble(productPrice.getProductPrice()) / 100.0f) * productPrice.getProductDiscount();
            res = Double.parseDouble(productPrice.getProductPrice()) - res;
            txtPrice.setText(sessionManager.getStringData(currency) + new DecimalFormat("##.##").format(res));
            txtItemOffer.setText(sessionManager.getStringData(currency) + productPrice.getProductPrice());
            txtItemOffer.setPaintFlags(txtItemOffer.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        imgMins.setOnClickListener(v -> {
            count[0] = Integer.parseInt(txtcount.getText().toString());
            count[0] = count[0] - 1;
            if (count[0] <= 0) {
                txtcount.setText("" + count[0]);
                lvlAddremove.setVisibility(View.GONE);
                lvlAddcart.setVisibility(View.VISIBLE);
                helper.deleteRData(myCart.getAttributeId());
                updateItem();
            } else {
                txtcount.setVisibility(View.VISIBLE);
                txtcount.setText("" + count[0]);
                myCart.setQty(String.valueOf(count[0]));
                updateItem();
            }
        });
        imgPlus.setOnClickListener(v -> {
            count[0] = Integer.parseInt(txtcount.getText().toString());
            count[0] = count[0] + 1;
            txtcount.setText("" + count[0]);
            myCart.setQty(String.valueOf(count[0]));
            Log.e("INsert", "--> " + helper.insertData(myCart));
            updateItem();
        });
        lvlAddcart.setOnClickListener(v -> {
            lvlAddcart.setVisibility(View.GONE);
            lvlAddremove.setVisibility(View.VISIBLE);
            count[0] = Integer.parseInt(txtcount.getText().toString());
            count[0] = count[0] + 1;
            txtcount.setText("" + count[0]);
            myCart.setQty(String.valueOf(count[0]));
            Log.e("INsert", "--> " + helper.insertData(myCart));
            updateItem();
        });
        lnrView.addView(view);
    }

    public void updateItem() {
        Cursor res = helper.getAllData();
        if (res.getCount() == 0) {
            txtCountcard.setText("0");
        } else {
            txtCountcard.setText("" + res.getCount());
        }
    }

    public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerHolder> {
        private Context context;
        private List<String> mBanner;

        public BannerAdapter(Context context, List<String> mBanner) {
            this.context = context;
            this.mBanner = mBanner;
        }

        @NonNull
        @Override
        public BannerAdapter.BannerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.image_item, parent, false);
            return new BannerAdapter.BannerHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull BannerHolder holder, int position) {
            Glide.with(context).load(APIClient.baseUrl + "/" + mBanner.get(position)).thumbnail(Glide.with(context).load(R.drawable.ezgifresize)).centerCrop().into(holder.imgBanner);
            holder.imgBanner.setOnClickListener(view -> {
                Dialog dialog = new Dialog(context,android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
                dialog.setContentView(R.layout.item_imageview);
                TouchImageView imageView = dialog.findViewById(R.id.imageView);
                Picasso.get().load(APIClient.baseUrl + "/" + mBanner.get(position)).into(imageView);
                dialog.show();
            });
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

    int positontype = 0;

    @Override
    protected void onResume() {
        super.onResume();
        if (txtCountcard != null) {
            updateItem();
            setJoinPlayrList(lvlAddcart, productInfo.get(positontype));

        }
    }
}
