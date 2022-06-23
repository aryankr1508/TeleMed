package com.cscodetech.pharmafast.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.cscodetech.pharmafast.R;
import com.cscodetech.pharmafast.model.AddressList;
import com.cscodetech.pharmafast.model.CartValidate;
import com.cscodetech.pharmafast.model.User;
import com.cscodetech.pharmafast.retrofit.APIClient;
import com.cscodetech.pharmafast.retrofit.GetResult;
import com.cscodetech.pharmafast.utiles.CustPrograssbar;
import com.cscodetech.pharmafast.utiles.DatabaseHelper;
import com.cscodetech.pharmafast.utiles.MyCart;
import com.cscodetech.pharmafast.utiles.SessionManager;
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
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

import static com.cscodetech.pharmafast.ui.AddressActivity.changeAddress;
import static com.cscodetech.pharmafast.utiles.SessionManager.address;
import static com.cscodetech.pharmafast.utiles.SessionManager.coupon;
import static com.cscodetech.pharmafast.utiles.SessionManager.currency;
import static com.cscodetech.pharmafast.utiles.SessionManager.dcharge;

public class CartActivity extends RootActivity implements GetResult.MyListener {

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
    @BindView(R.id.txt_changeadress)
    TextView tatChangeless;
    @BindView(R.id.btn_proceed)
    TextView btnProceed;
    StaggeredGridLayoutManager gridLayoutManager;
    List<MyCart> myCartList = new ArrayList<>();
    DatabaseHelper databaseHelper;
    @BindView(R.id.txt_itemtotal)
    TextView txtItemTotal;
    @BindView(R.id.txt_dcharge)
    TextView txtDCharge;
    @BindView(R.id.txt_Discount)
    TextView txtDiscount;
    @BindView(R.id.txt_topay)
    TextView txtToPay;
    @BindView(R.id.txt_atype)
    TextView txtAType;

    SessionManager sessionManager;
    User user;
    double total = 0;
    double subtotal = 0;
    @BindView(R.id.lvl_main)
    LinearLayout lvlMain;
    @BindView(R.id.lvl_notfound)
    LinearLayout lvlNotFound;
    CustPrograssbar custPrograssbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        ButterKnife.bind(this);
        sessionManager = new SessionManager(CartActivity.this);
        sessionManager.setIntData(coupon, 0);
        custPrograssbar=new CustPrograssbar();
        databaseHelper = new DatabaseHelper(CartActivity.this);
        gridLayoutManager = new StaggeredGridLayoutManager(1, 1);
        myRecyclerView.setLayoutManager(gridLayoutManager);
        user = sessionManager.getUserDetails("");
        jsoncreate();

    }

    private void jsoncreate() {
        Cursor res = databaseHelper.getAllData();
        if (res.getCount() == 0) {
            lvlNotFound.setVisibility(View.VISIBLE);
            lvlMain.setVisibility(View.GONE);
            return;
        }
        JSONArray jsonArray = new JSONArray();
        while (res.moveToNext()) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("aid", res.getString(0));
                jsonObject.put("price", res.getString(6));
                jsonObject.put("discount", res.getString(8));
                jsonArray.put(jsonObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        getAddressAndCartDatra(jsonArray);
    }

    private void cartdataset() {
        Cursor res = databaseHelper.getAllData();
        myCartList=new ArrayList<>();
        while (res.moveToNext()) {
            MyCart rModel = new MyCart();
            rModel.setId(res.getString(0));
            rModel.setPid(res.getString(1));
            rModel.setProductName(res.getString(2));
            rModel.setProductImage(res.getString(3));
            rModel.setBrandName(res.getString(4));
            rModel.setShortDesc(res.getString(5));
            rModel.setProductPrice(res.getString(6));
            rModel.setQty(res.getString(7));
            rModel.setDiscount(res.getInt(8));
            rModel.setAttributeId(res.getString(9));
            rModel.setProductType(res.getString(10));
            myCartList.add(rModel);
        }
        ItemAdp itemAdp = new ItemAdp(CartActivity.this, myCartList);
        myRecyclerView.setAdapter(itemAdp);
        updateItem();
    }

    private void getAddressAndCartDatra(JSONArray jsonArray) {
        custPrograssbar.prograssCreate(CartActivity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", user.getId());
            jsonObject.put("cartdata", jsonArray);
        } catch (Exception e) {
            e.printStackTrace();

        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().getProductCartAddress(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1");

    }

    public class ItemAdp extends RecyclerView.Adapter<ItemAdp.ViewHolder> {
        final int[] count = {0};
        double[] totalAmount = {0};
        private List<MyCart> mData;
        private LayoutInflater mInflater;
        Context mContext;
        SessionManager sessionManager;
        DatabaseHelper helper;

        public ItemAdp(Context context, List<MyCart> data) {
            this.mInflater = LayoutInflater.from(context);
            this.mData = data;
            this.mContext = context;
            sessionManager = new SessionManager(CartActivity.this);
            helper = new DatabaseHelper(CartActivity.this);
            WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.custome_mycard, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int i) {
            MyCart myCart = mData.get(i);
            if (myCart.getDiscount() != 0) {

                double res = (Double.parseDouble(myCart.getProductPrice()) * myCart.getDiscount()) / 100;
                res = Double.parseDouble(myCart.getProductPrice()) - res;
                holder.txtPrice.setText(sessionManager.getStringData(currency) + new DecimalFormat("##.##").format(res));
                holder.txtDscount.setPaintFlags(holder.txtDscount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.txtDscount.setText(sessionManager.getStringData(currency) + myCart.getProductPrice());


                holder.lvlOffer.setVisibility(View.VISIBLE);
                holder.txtOffer.setText(myCart.getDiscount() + "% OFF ");
            } else {
                holder.txtPrice.setText(sessionManager.getStringData(currency) + myCart.getProductPrice());

                holder.lvlOffer.setVisibility(View.GONE);
                holder.txtDscount.setVisibility(View.GONE);

            }
            Glide.with(mContext).load(APIClient.baseUrl + "/" + myCart.getProductImage()).thumbnail(Glide.with(mContext).load(R.drawable.ezgifresize)).centerCrop().into(holder.imgIcon);

            holder.txtTitle.setText("" + myCart.getProductName());
            holder.txtPtype.setText("" + myCart.getProductType());
//
//
            int qrt = helper.getCard(myCart.getAttributeId());
            if (qrt != -1) {
                count[0] = qrt;
                holder.txtcount.setText("" + count[0]);
                holder.txtcount.setVisibility(View.VISIBLE);
                holder.imgMins.setVisibility(View.VISIBLE);

            } else {
                holder.txtcount.setVisibility(View.INVISIBLE);
                holder.imgMins.setVisibility(View.INVISIBLE);
            }
            double ress = (Double.parseDouble(myCart.getProductPrice()) / 100.0f) * myCart.getDiscount();
            ress = Double.parseDouble(myCart.getProductPrice()) - ress;
            double temp = ress * qrt;
            totalAmount[0] = totalAmount[0] + temp;
            holder.imgMins.setOnClickListener(v -> {
                sessionManager.setIntData(coupon, 0);

                count[0] = Integer.parseInt(holder.txtcount.getText().toString());
                count[0] = count[0] - 1;
                if (count[0] <= 0) {
                    holder.txtcount.setVisibility(View.INVISIBLE);
                    holder.imgMins.setVisibility(View.INVISIBLE);
                    holder.txtcount.setText("" + count[0]);
                    helper.deleteRData(myCart.getAttributeId());
                    mData.remove(myCart);

                    totalAmount[0] = totalAmount[0] - Double.parseDouble(myCart.getProductPrice());
                    Toast.makeText(CartActivity.this, "" + myCart.getProductName() + "  is Remove", Toast.LENGTH_LONG).show();

                    notifyDataSetChanged();
                    updateItem();
                } else {
                    holder.txtcount.setVisibility(View.VISIBLE);
                    holder.txtcount.setText("" + count[0]);
                    myCart.setQty(String.valueOf(count[0]));
                    totalAmount[0] = totalAmount[0] - Double.parseDouble(myCart.getProductPrice());
                    helper.insertData(myCart);
                    notifyDataSetChanged();
                    updateItem();

                }
            });
            holder.imgPlus.setOnClickListener(v -> {
                sessionManager.setIntData(coupon, 0);

                holder.txtcount.setVisibility(View.VISIBLE);
                holder.imgMins.setVisibility(View.VISIBLE);
                count[0] = Integer.parseInt(holder.txtcount.getText().toString());
                totalAmount[0] = totalAmount[0] + Double.parseDouble(myCart.getProductPrice());
                count[0] = count[0] + 1;
                holder.txtcount.setText("" + count[0]);
                myCart.setQty(String.valueOf(count[0]));
                helper.insertData(myCart);
                updateItem();
            });
            holder.imgDelete.setOnClickListener(v -> {
                sessionManager.setIntData(coupon, 0);

                AlertDialog myDelete = new AlertDialog.Builder(CartActivity.this)
                        .setTitle("Delete")
                        .setMessage("Are you sure you want to delete?")
                        .setIcon(R.drawable.ic_delete)
                        .setPositiveButton("Delete", (dialog, whichButton) -> {
                            Log.d("sdj", "" + whichButton);
                            dialog.dismiss();
                            totalAmount[0] = totalAmount[0] - Double.parseDouble(myCart.getProductPrice());
                            helper.deleteRData(myCart.getAttributeId());
                            mData.remove(myCart);
                            updateItem();
                            notifyDataSetChanged();
                        })
                        .setNegativeButton("cancel", (dialog, which) -> {
                            Log.d("sdj", "" + which);
                            dialog.dismiss();
                        })
                        .create();
                myDelete.show();
            });

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
            @BindView(R.id.img_delete)
            ImageView imgDelete;
            @BindView(R.id.img_mins)
            LinearLayout imgMins;
            @BindView(R.id.txtcount)
            TextView txtcount;
            @BindView(R.id.img_plus)
            LinearLayout imgPlus;
            @BindView(R.id.lvl_addremove)
            LinearLayout lvlAddremove;
            @BindView(R.id.txt_offer)
            TextView txtOffer;
            @BindView(R.id.txt_ptype)
            TextView txtPtype;
            @BindView(R.id.lvl_offer)
            LinearLayout lvlOffer;

            ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

        }


    }

    public void updateItem() {
        Cursor res = databaseHelper.getAllData();
        subtotal = 0;
        total = 0;
        double ress = 0;
        int totalItem = 0;
        if (res.getCount() == 0) {

            lvlNotFound.setVisibility(View.VISIBLE);
            lvlMain.setVisibility(View.GONE);

        }
        while (res.moveToNext()) {
            MyCart rModel = new MyCart();
            rModel.setProductPrice(res.getString(6));
            rModel.setQty(res.getString(7));
            rModel.setDiscount(res.getInt(8));
            if (rModel.getDiscount() != 0) {
                ress = (Double.parseDouble(rModel.getProductPrice()) * rModel.getDiscount()) / 100;
                ress = Double.parseDouble(rModel.getProductPrice()) - ress;
            } else {
                ress = Double.parseDouble(rModel.getProductPrice());
            }

            double temp = Integer.parseInt(rModel.getQty()) * ress;
            subtotal = subtotal + temp;
            totalItem = totalItem + Integer.parseInt(res.getString(6));

        }
        txtDCharge.setText(sessionManager.getStringData(currency) + sessionManager.getFloatData(dcharge));

        txtItemTotal.setText(sessionManager.getStringData(currency) + new DecimalFormat("##.##").format(subtotal));
        total = subtotal + sessionManager.getFloatData(dcharge);
        if (sessionManager.getIntData(coupon) != 0) {
            imgCoupnCode.setImageResource(R.drawable.ic_cancel_coupon);
        } else {
            imgCoupnCode.setImageResource(R.drawable.ic_apply_coupon);

        }
        txtDiscount.setText(sessionManager.getStringData(currency) + sessionManager.getIntData(coupon));
        total = total - sessionManager.getIntData(coupon);
        txtToPay.setText(sessionManager.getStringData(currency) + new DecimalFormat("##.##").format(total));

    }


    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                CartValidate cartdataset = gson.fromJson(result.toString(), CartValidate.class);
                if (cartdataset.getResult().equalsIgnoreCase("true")) {
                    if (cartdataset.getAddressList().size() != 0) {
                        if (cartdataset.getCartValidateData().size() != 0) {
                            for (int i = 0; i < cartdataset.getCartValidateData().size(); i++) {
                                databaseHelper.deleteRData(cartdataset.getCartValidateData().get(i).getAid());
                            }
                            sessionManager.setAddress(cartdataset.getAddressList().get(sessionManager.getIntData("position")));
                          AddressList address1= sessionManager.getAddress();
                            txtAType.setText("" + address1.getType());
                            txtAddress.setText(address1.getHno() + "," + address1.getLandmark() + "," + address1.getAddress());
                            sessionManager.setFloatData(dcharge, Float.parseFloat(cartdataset.getAddressList().get(sessionManager.getIntData("position")).getDeliveryCharge()));

                            cartdataset();
                        } else {
                            cartdataset();
                        }
                    } else {
                        startActivity(new Intent(CartActivity.this, AddressActivity.class));
                        finish();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @OnClick({R.id.img_coopncode, R.id.txt_changeadress, R.id.btn_proceed, R.id.img_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_coopncode:
                if (sessionManager.getIntData(coupon) != 0) {
                    imgCoupnCode.setImageResource(R.drawable.ic_cancel_coupon);
                    sessionManager.setIntData(coupon, 0);
                    updateItem();
                } else {

                    int temp = (int) Math.round(total);
                    startActivity(new Intent(CartActivity.this, CoupunActivity.class).putExtra("amount", temp));
                }

                break;
            case R.id.txt_changeadress:
                startActivity(new Intent(CartActivity.this, AddressActivity.class));
                break;
            case R.id.btn_proceed:
                Intent intent = new Intent(CartActivity.this, PaymentOptionActivity.class);
                intent.putExtra("subtotal", String.valueOf(subtotal));
                intent.putExtra("total", String.valueOf(total));
                intent.putExtra("note", edCustom.getText().toString());
                startActivity(intent);
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
            if (changeAddress) {
                changeAddress = false;
                jsoncreate();
            } else {
                updateItem();
            }
        }
    }
}