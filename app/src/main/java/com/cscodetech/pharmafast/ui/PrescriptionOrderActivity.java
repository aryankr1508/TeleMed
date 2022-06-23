package com.cscodetech.pharmafast.ui;

import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.cscodetech.pharmafast.R;
import com.cscodetech.pharmafast.model.PrescriptionHistory;
import com.cscodetech.pharmafast.model.PrescritionOrder;
import com.cscodetech.pharmafast.model.RestResponse;
import com.cscodetech.pharmafast.model.User;
import com.cscodetech.pharmafast.retrofit.APIClient;
import com.cscodetech.pharmafast.retrofit.GetResult;
import com.cscodetech.pharmafast.utiles.CustPrograssbar;
import com.cscodetech.pharmafast.utiles.SessionManager;
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class PrescriptionOrderActivity extends RootActivity implements GetResult.MyListener {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_actiontitle)
    TextView tactile;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.txt_resent)
    TextView txtResent;
    @BindView(R.id.txt_delivered)
    TextView txtDelivered;
    @BindView(R.id.my_recycler_view)
    RecyclerView myRecyclerView;
    StaggeredGridLayoutManager gridLayoutManager;
    SessionManager sessionManager;
    User user;
    CustPrograssbar custPrograssbar;
    @BindView(R.id.txt_notfount)
    TextView txtNotfount;
    @BindView(R.id.lvl_notfound)
    LinearLayout lvlNotfound;
    ItemAdp itemAdp;
    List<PrescriptionHistory> orderHistories = new ArrayList<>();
    List<PrescriptionHistory> resent = new ArrayList<>();
    List<PrescriptionHistory> delivery = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescriptionorder);
        ButterKnife.bind(this);
        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(PrescriptionOrderActivity.this);
        user = sessionManager.getUserDetails("");
        gridLayoutManager = new StaggeredGridLayoutManager(1, 1);
        myRecyclerView.setLayoutManager(gridLayoutManager);

        getOrder();
    }

    @OnClick({R.id.img_back, R.id.txt_resent, R.id.txt_delivered})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.txt_resent:
                dataset(resent);
                txtResent.setTextColor(getResources().getColor(R.color.white));
                txtDelivered.setTextColor(getResources().getColor(R.color.black));
                txtResent.setBackground(getResources().getDrawable(R.drawable.orderbox));
                txtDelivered.setBackground(getResources().getDrawable(R.drawable.orderbox_white));
                break;
            case R.id.txt_delivered:
                dataset(delivery);
                txtResent.setTextColor(getResources().getColor(R.color.black));
                txtDelivered.setTextColor(getResources().getColor(R.color.white));
                txtResent.setBackground(getResources().getDrawable(R.drawable.orderbox_white));
                txtDelivered.setBackground(getResources().getDrawable(R.drawable.orderbox));

                break;
            default:
                break;
        }
    }

    private void getOrder() {
        custPrograssbar.prograssCreate(PrescriptionOrderActivity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", user.getId());
        } catch (Exception e) {
            e.printStackTrace();

        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().getPredationOrder(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1");

    }

    public void getCancel(String oid) {
        custPrograssbar.prograssCreate(PrescriptionOrderActivity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", user.getId());
            jsonObject.put("order_id", oid);
        } catch (Exception e) {
            e.printStackTrace();

        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().getPresOrderCancel(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "2");

    }

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                PrescritionOrder orderH = gson.fromJson(result.toString(), PrescritionOrder.class);
                if (orderH.getResult().equalsIgnoreCase("true")) {
                    orderHistories = orderH.getPrescriptionHistory();
                    resent = new ArrayList<>();
                    delivery = new ArrayList<>();
                    for (int i = 0; i < orderHistories.size(); i++) {
                        if (orderHistories.get(i).getStatus().equalsIgnoreCase("Completed")) {
                            delivery.add(orderHistories.get(i));
                        } else {
                            resent.add(orderHistories.get(i));
                        }
                    }
                    dataset(resent);
                } else {
                    myRecyclerView.setVisibility(View.GONE);
                    lvlNotfound.setVisibility(View.VISIBLE);
                    txtNotfount.setText("Your orders will be displayed hear.");
                }
            } else if (callNo.equalsIgnoreCase("2")) {
                Gson gson = new Gson();
                RestResponse response = gson.fromJson(result.toString(), RestResponse.class);
                if (response.getResult().equalsIgnoreCase("true")) {
                    itemAdp.notifyDataSetChanged();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void dataset(List<PrescriptionHistory> s) {

        if (s.size() != 0) {
            lvlNotfound.setVisibility(View.GONE);
            myRecyclerView.setVisibility(View.VISIBLE);
            gridLayoutManager = new StaggeredGridLayoutManager(1, 1);
            myRecyclerView.setLayoutManager(gridLayoutManager);
            itemAdp = new ItemAdp(PrescriptionOrderActivity.this, s);
            myRecyclerView.setAdapter(itemAdp);
        } else {
            myRecyclerView.setVisibility(View.GONE);
            lvlNotfound.setVisibility(View.VISIBLE);
            txtNotfount.setText("Your orders will be displayed hear.");
        }
    }

    public class ItemAdp extends RecyclerView.Adapter<ItemAdp.ViewHolder> {


        private LayoutInflater mInflater;
        Context mContext;
        List<PrescriptionHistory> lists;

        public ItemAdp(Context context, List<PrescriptionHistory> s) {
            this.mInflater = LayoutInflater.from(context);
            this.lists = s;
            this.mContext = context;
            WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.custome_orderitem, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int i) {

            PrescriptionHistory history = lists.get(i);
            holder.txtOrder.setText("" + history.getId());
            holder.txtOrderdate.setText("" + history.getOrderDate());
            holder.txtOrderstatus.setText("" + history.getStatus());

            if (history.getStatus().equalsIgnoreCase("Pending")) {
                holder.txtCancel.setVisibility(View.VISIBLE);
            } else {
                holder.txtCancel.setVisibility(View.INVISIBLE);

            }
            holder.txtInfo.setOnClickListener(v -> startActivity(new Intent(PrescriptionOrderActivity.this, PrescriptionOrderDetailsActivity.class).putExtra("oid", history.getId())));
            holder.txtCancel.setOnClickListener(v -> {
                AlertDialog myDelete = new AlertDialog.Builder(PrescriptionOrderActivity.this)
                        .setTitle("Order Cancel")
                        .setMessage("Are you sure you want to Order Cancel?")
                        .setPositiveButton("Yes", (dialog, whichButton) -> {
                            Log.d("sdj", "" + whichButton);
                            dialog.dismiss();
                            history.setStatus("Cancelled");
                            lists.set(i, history);
                            getCancel(history.getId());
                        })
                        .setNegativeButton("No", (dialog, which) -> {
                            Log.d("sdj", "" + which);
                            dialog.dismiss();
                        })
                        .create();
                myDelete.show();

            });


        }

        @Override
        public int getItemCount() {
            return lists.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.txt_order)
            TextView txtOrder;
            @BindView(R.id.txt_total)
            TextView txtTotal;
            @BindView(R.id.txt_orderstatus)
            TextView txtOrderstatus;
            @BindView(R.id.txt_orderdate)
            TextView txtOrderdate;
            @BindView(R.id.txt_info)
            TextView txtInfo;
            @BindView(R.id.txt_cancel)
            TextView txtCancel;

            ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

        }


    }
}