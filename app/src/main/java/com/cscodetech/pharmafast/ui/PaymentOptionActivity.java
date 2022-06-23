package com.cscodetech.pharmafast.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cscodetech.pharmafast.R;
import com.cscodetech.pharmafast.model.AddressList;
import com.cscodetech.pharmafast.model.Payment;
import com.cscodetech.pharmafast.model.PaymentItem;
import com.cscodetech.pharmafast.model.RestResponse;
import com.cscodetech.pharmafast.model.User;
import com.cscodetech.pharmafast.retrofit.APIClient;
import com.cscodetech.pharmafast.retrofit.GetResult;
import com.cscodetech.pharmafast.utiles.CustPrograssbar;
import com.cscodetech.pharmafast.utiles.DatabaseHelper;
import com.cscodetech.pharmafast.utiles.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

import static com.cscodetech.pharmafast.utiles.SessionManager.address;
import static com.cscodetech.pharmafast.utiles.SessionManager.coupon;
import static com.cscodetech.pharmafast.utiles.SessionManager.couponid;
import static com.cscodetech.pharmafast.utiles.SessionManager.dcharge;
import static com.cscodetech.pharmafast.utiles.Utility.paymentID;
import static com.cscodetech.pharmafast.utiles.Utility.paymentsucsses;
import static com.cscodetech.pharmafast.utiles.Utility.tragectionID;

public class PaymentOptionActivity extends RootActivity implements GetResult.MyListener {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.lvl_paymentlist)
    LinearLayout lvlPaymentList;
    DatabaseHelper databaseHelper;
    String subtotal;
    String note;
    String total;
    User user;
    AddressList address1;

    SessionManager sessionManager;
    CustPrograssbar custPrograssbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_option);
        ButterKnife.bind(this);
        sessionManager = new SessionManager(PaymentOptionActivity.this);
        custPrograssbar = new CustPrograssbar();
        address1 = sessionManager.getAddress();
        user = sessionManager.getUserDetails("");
        subtotal = getIntent().getStringExtra("subtotal");
        total = getIntent().getStringExtra("total");
        note = getIntent().getStringExtra("note");
        databaseHelper = new DatabaseHelper(PaymentOptionActivity.this);
        getPayment();
    }

    private void getPayment() {
        custPrograssbar.prograssCreate(PaymentOptionActivity.this);

        JSONObject jsonObject = new JSONObject();
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().getPaymentList(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1");

    }

    public void orderPlace(JSONArray jsonArray) {
        custPrograssbar.prograssCreate(PaymentOptionActivity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", user.getId());
            jsonObject.put("p_method_id", paymentID);
            jsonObject.put("full_address", address1.getHno() + "," + address1.getLandmark() + "," + address1.getAddress());
            jsonObject.put("d_charge", sessionManager.getFloatData(dcharge));
            jsonObject.put("cou_id", sessionManager.getIntData(couponid));
            jsonObject.put("cou_amt", sessionManager.getIntData(coupon));
            jsonObject.put("transaction_id", tragectionID);
            jsonObject.put("product_total", total);
            jsonObject.put("product_subtotal", subtotal);
            jsonObject.put("a_note", note);

            jsonObject.put("ProductData", jsonArray);
            RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
            Call<JsonObject> call = APIClient.getInterface().getOrderNow(bodyRequest);
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "2");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                Payment payment = gson.fromJson(result.toString(), Payment.class);
                setJoinPlayrList(lvlPaymentList, payment.getData());
            } else if (callNo.equalsIgnoreCase("2")) {
                Gson gson = new Gson();
                RestResponse restResponse = gson.fromJson(result.toString(), RestResponse.class);
                if (restResponse.getResult().equalsIgnoreCase("true")) {
                    Intent intent = new Intent(this, CompleOrderActivity.class);
                    startActivity(intent);
                    finish();
                    databaseHelper.deleteCard();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    private class AsyncTaskRunner extends AsyncTask<String, String, JSONArray> {


        @Override
        protected JSONArray doInBackground(String... params) {
            JSONArray jsonArray = new JSONArray();
            Cursor res = databaseHelper.getAllData();
            while (res.moveToNext()) {
                JSONObject jsonObject = new JSONObject();
                try {

                    jsonObject.put("title", res.getString(2));
                    jsonObject.put("image", res.getString(3));
                    jsonObject.put("type", res.getString(10));
                    jsonObject.put("cost", res.getString(6));
                    jsonObject.put("qty", res.getString(7));
                    jsonObject.put("discount", res.getString(8));
                    jsonObject.put("attribute_id", res.getString(9));
                    jsonArray.put(jsonObject);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return jsonArray;
        }


        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            // execution of result of Long time consuming operation
            orderPlace(jsonArray);
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onProgressUpdate(String... text) {

        }
    }

    private void setJoinPlayrList(LinearLayout lnrView, List<PaymentItem> paymentList) {
        lnrView.removeAllViews();
        for (int i = 0; i < paymentList.size(); i++) {
            LayoutInflater inflater = LayoutInflater.from(PaymentOptionActivity.this);
            PaymentItem paymentItem = paymentList.get(i);
            View view = inflater.inflate(R.layout.custome_payment_item, null);
            ImageView imageView = view.findViewById(R.id.img_icon);
            TextView txtTitle = view.findViewById(R.id.txt_title);
            TextView txtSubtitel = view.findViewById(R.id.txt_subtitel);
            txtTitle.setText("" + paymentList.get(i).getmTitle());
            txtSubtitel.setText("" + paymentList.get(i).getSubtitle());
            Glide.with(PaymentOptionActivity.this).load(APIClient.baseUrl + "/" + paymentList.get(i).getmImg()).thumbnail(Glide.with(PaymentOptionActivity.this).load(R.drawable.ezgifresize)).into(imageView);
            int finalI = i;
            view.setOnClickListener(v -> {
                paymentID = paymentList.get(finalI).getmId();
                try {
                    switch (paymentList.get(finalI).getmTitle()) {
                        case "Razorpay":
                            int temtoal = (int) Math.round(Double.parseDouble(total));
                            startActivity(new Intent(PaymentOptionActivity.this, RazerpayActivity.class).putExtra("amount", temtoal).putExtra("detail", paymentItem));
                            break;
                        case "Cash On Delivery":
                            new AsyncTaskRunner().execute("");
                            break;
                        case "Paypal":
                            startActivity(new Intent(PaymentOptionActivity.this, PaypalActivity.class).putExtra("amount", Double.parseDouble(total)).putExtra("detail", paymentItem));
                            break;
                        case "Stripe":
                            startActivity(new Intent(PaymentOptionActivity.this, StripPaymentActivity.class).putExtra("amount", Double.parseDouble(total)).putExtra("detail", paymentItem));
                            break;
                        default:
                            break;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            lnrView.addView(view);
        }
    }


    @OnClick(R.id.img_back)
    public void onClick() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (paymentsucsses == 1) {
            paymentsucsses = 0;
            new AsyncTaskRunner().execute("0");

        }
    }
}