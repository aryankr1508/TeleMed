package com.cscodetech.pharmafast.ui;

import static com.cscodetech.pharmafast.utiles.SessionManager.coupon;
import static com.cscodetech.pharmafast.utiles.SessionManager.couponid;
import static com.cscodetech.pharmafast.utiles.SessionManager.currency;
import static com.cscodetech.pharmafast.utiles.Utility.cartUpdate;
import static com.cscodetech.pharmafast.utiles.Utility.paymentID;
import static com.cscodetech.pharmafast.utiles.Utility.paymentsucsses;
import static com.cscodetech.pharmafast.utiles.Utility.tragectionID;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.cscodetech.pharmafast.R;
import com.cscodetech.pharmafast.model.OrderProductDatum;
import com.cscodetech.pharmafast.model.Payment;
import com.cscodetech.pharmafast.model.PaymentItem;
import com.cscodetech.pharmafast.model.PrescriptionOrderProductList;
import com.cscodetech.pharmafast.model.RestResponse;
import com.cscodetech.pharmafast.model.User;
import com.cscodetech.pharmafast.retrofit.APIClient;
import com.cscodetech.pharmafast.retrofit.GetResult;
import com.cscodetech.pharmafast.utiles.CustPrograssbar;
import com.cscodetech.pharmafast.utiles.DatabaseHelper;
import com.cscodetech.pharmafast.utiles.MyCart;
import com.cscodetech.pharmafast.utiles.SessionManager;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class PresCartActivity extends AppCompatActivity implements GetResult.MyListener {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_actiontitle)
    TextView txtActionTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.my_recycler_view)
    RecyclerView myRecyclerView;
    @BindView(R.id.img_coopncode)
    ImageView imgCoupnCode;
    @BindView(R.id.ed_customnot)
    EditText edCustom;
    @BindView(R.id.txt_address)
    TextView txtAddress;

    @BindView(R.id.btn_proceed)
    TextView btnProceed;
    StaggeredGridLayoutManager gridLayoutManager;

    @BindView(R.id.txt_itemtotal)
    TextView txtItemTotal;
    @BindView(R.id.txt_dcharge)
    TextView txtDCharge;
    @BindView(R.id.txt_Discount)
    TextView txtDiscount;
    @BindView(R.id.txt_topay)
    TextView txtToPay;


    SessionManager sessionManager;
    User user;
    double total = 0;

    @BindView(R.id.lvl_main)
    LinearLayout lvlMain;
    @BindView(R.id.lvl_notfound)
    LinearLayout lvlNotFound;
    CustPrograssbar custPrograssbar;
    ArrayList<OrderProductDatum> list;
    PrescriptionOrderProductList orderProduct;
    String oid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pres_cart);
        ButterKnife.bind(this);
        sessionManager = new SessionManager(PresCartActivity.this);
        user = sessionManager.getUserDetails("");
        sessionManager.setIntData(coupon, 0);
        sessionManager.setIntData(couponid, 0);
        custPrograssbar = new CustPrograssbar();
        oid = getIntent().getStringExtra("oid");
        list = getIntent().getParcelableArrayListExtra("item_list");
        orderProduct = getIntent().getParcelableExtra("item");
        gridLayoutManager = new StaggeredGridLayoutManager(1, 1);
        myRecyclerView.setLayoutManager(gridLayoutManager);
        ItemAdp itemAdp = new ItemAdp(PresCartActivity.this, list);
        myRecyclerView.setAdapter(itemAdp);
        update();

    }

    public void update() {
        total = Double.parseDouble(orderProduct.getmOrderTotal());
        if (sessionManager.getIntData(coupon) != 0) {
            imgCoupnCode.setImageResource(R.drawable.ic_cancel_coupon);
        } else {
            imgCoupnCode.setImageResource(R.drawable.ic_apply_coupon);

        }
        total = total - sessionManager.getIntData(coupon);
        txtItemTotal.setText(sessionManager.getStringData(currency) + orderProduct.getmOrderSubTotal());
        txtDiscount.setText(sessionManager.getStringData(currency) + sessionManager.getIntData(coupon));
        txtDCharge.setText(sessionManager.getStringData(currency) + orderProduct.getmDeliveryCharge());
        txtToPay.setText(sessionManager.getStringData(currency) + total);
        txtAddress.setText("" + orderProduct.getCustomerAddress());
    }

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                Payment payment = gson.fromJson(result.toString(), Payment.class);
                setJoinPlayrList(payment.getData());
            } else if (callNo.equalsIgnoreCase("2")) {
                Gson gson = new Gson();
                RestResponse response = gson.fromJson(result.toString(), RestResponse.class);
                if (response.getResult().equalsIgnoreCase("true")) {
                    cartUpdate=1;
                    finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private void getPayment() {
        custPrograssbar.prograssCreate(PresCartActivity.this);

        JSONObject jsonObject = new JSONObject();
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().getPaymentList(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1");

    }

    private void orderProcess(String status) {
        custPrograssbar.prograssCreate(PresCartActivity.this);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", user.getId());
            jsonObject.put("oid", oid);
            jsonObject.put("status", status);
            jsonObject.put("d_charge", orderProduct.getmDeliveryCharge());
            jsonObject.put("cou_id", sessionManager.getIntData(couponid));
            jsonObject.put("cou_amt", sessionManager.getIntData(coupon));
            jsonObject.put("o_total", total);
            jsonObject.put("trans_id", tragectionID);
            jsonObject.put("a_note", edCustom.getText().toString());
            jsonObject.put("p_method_id", paymentID);
        } catch (Exception e) {
            e.printStackTrace();

        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().sendPreData(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "2");

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


    private void setJoinPlayrList(List<PaymentItem> paymentList) {

        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.custome_payment, null);
        LinearLayout listView = sheetView.findViewById(R.id.lvl_list);
        TextView txtTotal = sheetView.findViewById(R.id.txt_total);
        txtTotal.setText("item total " + sessionManager.getStringData(currency) + total);
        mBottomSheetDialog.setContentView(sheetView);

        for (int i = 0; i < paymentList.size(); i++) {
            LayoutInflater inflater = LayoutInflater.from(PresCartActivity.this);
            PaymentItem paymentItem = paymentList.get(i);
            View view = inflater.inflate(R.layout.custome_payment_item, null);
            ImageView imageView = view.findViewById(R.id.img_icon);
            TextView txtTitle = view.findViewById(R.id.txt_title);
            TextView txtSubtitel = view.findViewById(R.id.txt_subtitel);
            txtTitle.setText("" + paymentList.get(i).getmTitle());
            txtSubtitel.setText("" + paymentList.get(i).getSubtitle());
            Glide.with(PresCartActivity.this).load(APIClient.baseUrl + "/" + paymentList.get(i).getmImg()).thumbnail(Glide.with(PresCartActivity.this).load(R.drawable.ezgifresize)).into(imageView);
            int finalI = i;
            view.setOnClickListener(v -> {
                paymentID = paymentList.get(finalI).getmId();
                try {
                    switch (paymentList.get(finalI).getmTitle()) {
                        case "Razorpay":
                            int temtoal = (int) Math.round(total);
                            startActivity(new Intent(PresCartActivity.this, RazerpayActivity.class).putExtra("amount", temtoal).putExtra("detail", paymentItem));
                            break;
                        case "Cash On Delivery":
                            orderProcess("accept");
                            break;
                        case "Paypal":
                            startActivity(new Intent(PresCartActivity.this, PaypalActivity.class).putExtra("amount", total).putExtra("detail", paymentItem));
                            break;
                        case "Stripe":
                            startActivity(new Intent(PresCartActivity.this, StripPaymentActivity.class).putExtra("amount", total).putExtra("detail", paymentItem));
                            break;
                        default:
                            break;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            listView.addView(view);

        }
        mBottomSheetDialog.show();
    }


    @OnClick({R.id.img_coopncode, R.id.btn_proceed, R.id.img_back, R.id.btn_cancle})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_coopncode:
                if (sessionManager.getIntData(coupon) != 0) {
                    imgCoupnCode.setImageResource(R.drawable.ic_cancel_coupon);
                    sessionManager.setIntData(coupon, 0);
                    update();
                } else {

                    int temp = (int) Math.round(total);
                    startActivity(new Intent(PresCartActivity.this, CoupunActivity.class).putExtra("amount", temp));
                }

                break;
            case R.id.btn_proceed:
                getPayment();
                break;
            case R.id.btn_cancle:
                orderProcess("reject");
                break;
            case R.id.img_back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sessionManager != null) {
            update();
        }

        if (paymentsucsses == 1) {
            paymentsucsses = 0;
            orderProcess("accept");
        }
    }
}