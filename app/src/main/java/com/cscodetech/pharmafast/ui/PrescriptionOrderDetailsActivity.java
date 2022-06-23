package com.cscodetech.pharmafast.ui;

import static com.cscodetech.pharmafast.utiles.SessionManager.currency;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.cscodetech.pharmafast.R;
import com.cscodetech.pharmafast.model.OrderProductDatum;
import com.cscodetech.pharmafast.model.PrescriptionOrderH;
import com.cscodetech.pharmafast.model.User;
import com.cscodetech.pharmafast.retrofit.APIClient;
import com.cscodetech.pharmafast.retrofit.GetResult;
import com.cscodetech.pharmafast.utiles.CustPrograssbar;
import com.cscodetech.pharmafast.utiles.SessionManager;
import com.cscodetech.pharmafast.utiles.Utility;
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class PrescriptionOrderDetailsActivity extends RootActivity implements GetResult.MyListener {


    StaggeredGridLayoutManager gridLayoutManager;
    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;
    User user;
    String oid;
    @BindView(R.id.txt_address)
    TextView txtAddress;
    @BindView(R.id.txt_aditionalinfo)
    TextView txtAditionalinfo;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_actiontitle)
    TextView txtActiontitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.txt_summary)
    TextView txtSummary;
    @BindView(R.id.txt_item)
    TextView txtItem;
    @BindView(R.id.txt_orderid)
    TextView txtOrderid;
    @BindView(R.id.txt_orderstatus)
    TextView txtOrderstatus;
    @BindView(R.id.txt_orderdate)
    TextView txtOrderdate;
    @BindView(R.id.txt_orderddate)
    TextView txtOrderddate;
    @BindView(R.id.scv_summry)
    ScrollView scvSummry;
    @BindView(R.id.my_recycler_view)
    RecyclerView myRecyclerView;
    @BindView(R.id.my_recycler_item)
    RecyclerView myRecyclerItem;
    @BindView(R.id.scv_item)
    ScrollView scvItem;
    @BindView(R.id.lvl_additional)
    LinearLayout lvlAdditional;
    @BindView(R.id.lvl_viewcart)
    LinearLayout lvlViewcart;
    PrescriptionOrderH orderP;

    @BindView(R.id.lvl_subtotle)
    LinearLayout lvlSubtotle;
    @BindView(R.id.txt_subtotal)
    TextView txtSubtotal;
    @BindView(R.id.lvl_discount)
    LinearLayout lvlDiscount;
    @BindView(R.id.lvl_dcharge)
    LinearLayout lvlDcharge;
    @BindView(R.id.txt_dcharge)
    TextView txtDcharge;
    @BindView(R.id.txt_discount)
    TextView txtDiscount;
    @BindView(R.id.lvl_total)
    LinearLayout lvlTotal;
    @BindView(R.id.txt_total)
    TextView txtTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescriptionorder_details);
        ButterKnife.bind(this);
        oid = getIntent().getStringExtra("oid");
        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(PrescriptionOrderDetailsActivity.this);
        user = sessionManager.getUserDetails("");
        gridLayoutManager = new StaggeredGridLayoutManager(2, 1);
        myRecyclerView.setLayoutManager(gridLayoutManager);
        myRecyclerItem.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));

        getOrderHistiry();
    }

    private void getOrderHistiry() {
        custPrograssbar.prograssCreate(PrescriptionOrderDetailsActivity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", user.getId());
            jsonObject.put("order_id", oid);
        } catch (Exception e) {
            e.printStackTrace();

        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().getPrescriptionOrderHistry(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1");

    }

    @Override
    public void callback(JsonObject result, String callNo) {

        try {
            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                orderP = gson.fromJson(result.toString(), PrescriptionOrderH.class);
                if (orderP.getResult().equalsIgnoreCase("true")) {
                    txtOrderid.setText("" + oid);
                    txtOrderstatus.setText("" + orderP.getPrescriptionOrderProductList().getOrderStatus());
                    txtOrderdate.setText("" + orderP.getPrescriptionOrderProductList().getOrderDate());
                    txtAddress.setText("" + orderP.getPrescriptionOrderProductList().getCustomerAddress());
                    if (orderP.getPrescriptionOrderProductList().getCartIsSet().equalsIgnoreCase("1")) {
                        lvlViewcart.setVisibility(View.VISIBLE);
                    } else if (orderP.getPrescriptionOrderProductList().getCartIsSet().equalsIgnoreCase("2")) {
                        lvlViewcart.setVisibility(View.GONE);
                        myRecyclerItem.setVisibility(View.VISIBLE);
                        lvlSubtotle.setVisibility(View.VISIBLE);
                        lvlTotal.setVisibility(View.VISIBLE);
                        lvlDiscount.setVisibility(View.VISIBLE);
                        lvlDcharge.setVisibility(View.VISIBLE);
                        txtTotal.setText(sessionManager.getStringData(currency) + orderP.getPrescriptionOrderProductList().getmOrderTotal());
                        txtSubtotal.setText(sessionManager.getStringData(currency) + orderP.getPrescriptionOrderProductList().getmOrderSubTotal());
                        txtDiscount.setText(sessionManager.getStringData(currency) + orderP.getPrescriptionOrderProductList().getmCouponAmount());
                        txtDcharge.setText(sessionManager.getStringData(currency) + orderP.getPrescriptionOrderProductList().getmDeliveryCharge());
                        ItemAdp itemAdp = new ItemAdp(PrescriptionOrderDetailsActivity.this, orderP.getPrescriptionOrderProductList().getmOrderProductData());
                        myRecyclerItem.setAdapter(itemAdp);
                    }
                    ItemAdpImage itemAdp1 = new ItemAdpImage(PrescriptionOrderDetailsActivity.this, orderP.getPrescriptionOrderProductList().getPrescriptionImageList());
                    myRecyclerView.setAdapter(itemAdp1);

                }
            }
        } catch (Exception e) {
            Log.e("Error", "-->" + e.toString());
        }

    }

    @OnClick({R.id.img_back, R.id.txt_summary, R.id.txt_item, R.id.lvl_viewcart})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.txt_summary:
                txtSummary.setTextColor(getResources().getColor(R.color.white));
                txtItem.setTextColor(getResources().getColor(R.color.black));
                txtSummary.setBackground(getResources().getDrawable(R.drawable.orderbox));
                txtItem.setBackground(getResources().getDrawable(R.drawable.orderbox_white));
                scvSummry.setVisibility(View.VISIBLE);
                scvItem.setVisibility(View.GONE);
                break;
            case R.id.txt_item:
                txtSummary.setTextColor(getResources().getColor(R.color.black));
                txtItem.setTextColor(getResources().getColor(R.color.white));
                txtSummary.setBackground(getResources().getDrawable(R.drawable.orderbox_white));
                txtItem.setBackground(getResources().getDrawable(R.drawable.orderbox));
                scvSummry.setVisibility(View.GONE);
                scvItem.setVisibility(View.VISIBLE);
                break;
            case R.id.lvl_viewcart:
                startActivity(new Intent(PrescriptionOrderDetailsActivity.this, PresCartActivity.class)
                        .putParcelableArrayListExtra("item_list", orderP.getPrescriptionOrderProductList().getmOrderProductData())
                        .putExtra("oid", oid)
                        .putExtra("item", orderP.getPrescriptionOrderProductList()));
                break;
            default:
                break;
        }
    }


    public class ItemAdpImage extends RecyclerView.Adapter<ItemAdpImage.ViewHolder> {

        private List<String> mData;
        private LayoutInflater mInflater;
        Context mContext;


        public ItemAdpImage(Context context, List<String> data) {
            this.mInflater = LayoutInflater.from(context);
            this.mData = data;
            this.mContext = context;
            WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.custome_image, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int i) {

            Glide.with(mContext).load(APIClient.baseUrl + "/" + mData.get(i)).thumbnail(Glide.with(mContext).load(R.drawable.ezgifresize)).into(holder.imgIcon);

            holder.imgIcon.setOnClickListener(view -> {

                Dialog dialog = new Dialog(PrescriptionOrderDetailsActivity.this, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
                dialog.setContentView(R.layout.item_imageview);
                TouchImageView imageView = dialog.findViewById(R.id.imageView);
                Picasso.get().load(APIClient.baseUrl + "/" + mData.get(i)).into(imageView);
                dialog.show();

            });
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.img_icon)
            ImageView imgIcon;


            ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

        }


    }

    public class ItemAdp extends RecyclerView.Adapter<ItemAdp.ViewHolder> {

        private ArrayList<OrderProductDatum> mData;
        private LayoutInflater mInflater;
        Context mContext;


        public ItemAdp(Context context, ArrayList<OrderProductDatum> data) {
            this.mInflater = LayoutInflater.from(context);
            this.mData = data;
            this.mContext = context;


        }

        @Override
        public ItemAdp.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.custome_mycard_prefs, parent, false);
            return new ItemAdp.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ItemAdp.ViewHolder holder, int i) {

            OrderProductDatum datum = mData.get(i);
            Glide.with(mContext).load(APIClient.baseUrl + "/" + datum.getProductImage()).thumbnail(Glide.with(mContext).load(R.drawable.ezgifresize)).centerCrop().into(holder.imgIcon);
            holder.txtTitle.setText("" + datum.getProductName());
            holder.txtPtype.setText("" + datum.getProductQuantity());
            holder.txtPrice.setText(sessionManager.getStringData(currency) + datum.getProductPrice());
            holder.txtDscount.setPaintFlags(holder.txtDscount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.txtDscount.setText(sessionManager.getStringData(currency) + datum.getProductDiscount());


        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.img_icon)
            ImageView imgIcon;
            @BindView(R.id.txt_title)
            TextView txtTitle;
            @BindView(R.id.txt_dscount)
            TextView txtDscount;
            @BindView(R.id.txt_price)
            TextView txtPrice;
            @BindView(R.id.txt_ptype)
            TextView txtPtype;


            ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

        }


    }


    @Override
    protected void onResume() {
        super.onResume();
        if (Utility.cartUpdate == 1) {
            Utility.cartUpdate=0;
            getOrderHistiry();
        }
    }
}