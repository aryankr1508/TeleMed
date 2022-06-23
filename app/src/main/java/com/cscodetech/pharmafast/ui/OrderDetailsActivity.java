package com.cscodetech.pharmafast.ui;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Point;
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
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.cscodetech.pharmafast.R;
import com.cscodetech.pharmafast.model.OrderP;
import com.cscodetech.pharmafast.model.OrderProductDatum;
import com.cscodetech.pharmafast.model.User;
import com.cscodetech.pharmafast.retrofit.APIClient;
import com.cscodetech.pharmafast.retrofit.GetResult;
import com.cscodetech.pharmafast.utiles.CustPrograssbar;
import com.cscodetech.pharmafast.utiles.SessionManager;
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class OrderDetailsActivity extends RootActivity implements GetResult.MyListener {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_actiontitle)
    TextView txtActionTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.txt_summary)
    TextView txtSummary;
    @BindView(R.id.txt_item)
    TextView txtItem;
    @BindView(R.id.txt_orderid)
    TextView txtOrderId;
    @BindView(R.id.txt_orderstatus)
    TextView txtOrderStatus;
    @BindView(R.id.txt_orderdate)
    TextView txtOrderDate;
    @BindView(R.id.txt_orderddate)
    TextView txtOrderDDate;
    @BindView(R.id.txt_paymentmethod)
    TextView txtPaymentMethod;
    @BindView(R.id.txt_qut)
    TextView txtQut;
    @BindView(R.id.txt_price)
    TextView txtPrice;
    @BindView(R.id.txt_deliverycharge)
    TextView txtDeliveryCharge;
    @BindView(R.id.txt_total)
    TextView txtTotal;
    @BindView(R.id.scv_summry)
    ScrollView schSummary;
    @BindView(R.id.scv_item)
    ScrollView scvItem;
    @BindView(R.id.my_recycler_view)
    RecyclerView myRecyclerView;
    StaggeredGridLayoutManager gridLayoutManager;
    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;
    User user;
    String oid;
    @BindView(R.id.txt_address)
    TextView txtAddress;
    @BindView(R.id.txt_aditionalinfo)
    TextView txtAdditionInfo;
    @BindView(R.id.lvl_aditional)
    LinearLayout lvlAditional;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        ButterKnife.bind(this);
        oid = getIntent().getStringExtra("oid");
        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(OrderDetailsActivity.this);
        user = sessionManager.getUserDetails("");
        gridLayoutManager = new StaggeredGridLayoutManager(1, 1);
        myRecyclerView.setLayoutManager(gridLayoutManager);
        getOrderHistiry();
    }

    private void getOrderHistiry() {
        custPrograssbar.prograssCreate(OrderDetailsActivity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", user.getId());
            jsonObject.put("order_id", oid);
        } catch (Exception e) {
            e.printStackTrace();

        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().getOrderHistory(bodyRequest);
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
                OrderP orderP = gson.fromJson(result.toString(), OrderP.class);
                if (orderP.getResult().equalsIgnoreCase("true")) {
                    txtOrderId.setText("" + oid);
                    txtOrderStatus.setText("" + orderP.getOrderProductList().get(0).getOrderStatus());
                    txtOrderDate.setText("" + orderP.getOrderProductList().get(0).getOrderDate());
                    txtPaymentMethod.setText("" + orderP.getOrderProductList().get(0).getPMethodName());
                    txtQut.setText(""+orderP.getOrderProductList().get(0).getOrderProductData().size());
                    txtPrice.setText(sessionManager.getStringData(SessionManager.currency) + orderP.getOrderProductList().get(0).getOrderSubTotal());
                    txtDeliveryCharge.setText(sessionManager.getStringData(SessionManager.currency)  + orderP.getOrderProductList().get(0).getDeliveryCharge());
                    txtTotal.setText(sessionManager.getStringData(SessionManager.currency)  + orderP.getOrderProductList().get(0).getOrderTotal());

                    txtAddress.setText("" + orderP.getOrderProductList().get(0).getCustomerAddress());
                    if (orderP.getOrderProductList().get(0).getAdditionalNote().equalsIgnoreCase("")) {
                        lvlAditional.setVisibility(View.GONE);
                    }else {
                        lvlAditional.setVisibility(View.VISIBLE);
                    }
                    txtAdditionInfo.setText("" + orderP.getOrderProductList().get(0).getAdditionalNote());

                    ItemAdp itemAdp = new ItemAdp(OrderDetailsActivity.this, orderP.getOrderProductList().get(0).getOrderProductData());
                    myRecyclerView.setAdapter(itemAdp);
                }
            }
        } catch (Exception e) {
            Log.e("Error", "-->" + e.toString());
        }

    }

    @OnClick({R.id.img_back, R.id.txt_summary, R.id.txt_item})
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
                schSummary.setVisibility(View.VISIBLE);
                scvItem.setVisibility(View.GONE);
                break;
            case R.id.txt_item:
                txtSummary.setTextColor(getResources().getColor(R.color.black));
                txtItem.setTextColor(getResources().getColor(R.color.white));
                txtSummary.setBackground(getResources().getDrawable(R.drawable.orderbox_white));
                txtItem.setBackground(getResources().getDrawable(R.drawable.orderbox));
                schSummary.setVisibility(View.GONE);
                scvItem.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }


    public class ItemAdp extends RecyclerView.Adapter<ItemAdp.ViewHolder> {

        private List<OrderProductDatum> mData;
        private LayoutInflater mInflater;
        Context mContext;


        public ItemAdp(Context context, List<OrderProductDatum> data) {
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
            View view = mInflater.inflate(R.layout.custome_myitem, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int i) {
            OrderProductDatum productDatum = mData.get(i);
            Glide.with(mContext).load(APIClient.baseUrl + "/" + productDatum.getProductImage()).thumbnail(Glide.with(mContext).load(R.drawable.ezgifresize)).into(holder.imgIcon);
            if (productDatum.getProductDiscount().equalsIgnoreCase("0")) {
                holder.lvlOffer.setVisibility(View.GONE);
                holder.txtDscount.setVisibility(View.GONE);
                holder.txtcount.setText("Qty " + productDatum.getProductQuantity() + "  ");
                holder.txtPrice.setText(sessionManager.getStringData(SessionManager.currency) + productDatum.getProductPrice());
                holder.txtPtype.setText("" + productDatum.getProductVariation());
                holder.txtTitle.setText("" + productDatum.getProductName());
            } else {
                holder.lvlOffer.setVisibility(View.VISIBLE);
                holder.txtDscount.setVisibility(View.VISIBLE);
                double res = (Double.parseDouble(productDatum.getProductPrice()) * Double.parseDouble(productDatum.getProductDiscount())) / 100;
                res = Double.parseDouble(productDatum.getProductPrice()) - res;
                holder.txtDscount.setPaintFlags(holder.txtDscount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                holder.txtDscount.setText(sessionManager.getStringData(SessionManager.currency) + productDatum.getProductPrice() + "  ");

                holder.txtOffer.setText(productDatum.getProductDiscount() + "% OFF");
                holder.txtcount.setText("Qty " + productDatum.getProductQuantity() + "  ");
                holder.txtPrice.setText(sessionManager.getStringData(SessionManager.currency) + new DecimalFormat("##.##").format(res));
                holder.txtTitle.setText("" + productDatum.getProductName());
                holder.txtPtype.setText("" + productDatum.getProductVariation());
            }


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
            @BindView(R.id.txt_ptype)
            TextView txtPtype;
            @BindView(R.id.txt_dscount)
            TextView txtDscount;
            @BindView(R.id.txtcount)
            TextView txtcount;
            @BindView(R.id.txt_price)
            TextView txtPrice;
            @BindView(R.id.txt_offer)
            TextView txtOffer;
            @BindView(R.id.lvl_offer)
            LinearLayout lvlOffer;

            ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

        }


    }

}