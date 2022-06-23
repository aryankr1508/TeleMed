package com.cscodetech.pharmafast.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cscodetech.pharmafast.R;
import com.cscodetech.pharmafast.utiles.CustPrograssbar;
import com.cscodetech.pharmafast.utiles.SessionManager;
import com.cscodetech.pharmafast.utiles.Utility;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.Stripe;
import com.stripe.android.model.ConfirmPaymentIntentParams;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.view.CardInputWidget;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cscodetech.pharmafast.utiles.SessionManager.currency;


public class StripPaymentActivity extends AppCompatActivity {

    @BindView(R.id.txt_total)
    TextView txtTotal;
    private String paymentIntentClientSecret;
    private Stripe stripe;
    CardInputWidget cardInputWidget;
    com.cscodetech.pharmafast.model.PaymentItem paymentItem;
    double amount = 0.0;
    SessionManager sessionManager;
    CustPrograssbar custPrograssbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strippayment);
        ButterKnife.bind(this);
        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(StripPaymentActivity.this);
        paymentItem = (com.cscodetech.pharmafast.model.PaymentItem) getIntent().getSerializableExtra("detail");
        amount = getIntent().getDoubleExtra("amount", 0.0);
        txtTotal.setText("Total Amount : " + sessionManager.getStringData(currency) + new DecimalFormat("##.##").format(amount));
        long total = Math.round(amount * 100);
        Log.e("toto", "-->" + total);
        List<String> elephantList = Arrays.asList(paymentItem.getmAttributes().split(","));
        PaymentConfiguration.init(
                getApplicationContext(),
                elephantList.get(0)
        );
        new Thread() {
            @Override
            public void run() {
                try {
                    com.stripe.Stripe.apiKey = elephantList.get(1);
                    PaymentIntentCreateParams params =
                            PaymentIntentCreateParams.builder()
                                    .setAmount(total)
                                    .setCurrency("INR")
                                    .build();
                    PaymentIntent intent = PaymentIntent.create(params);
                    paymentIntentClientSecret = intent.getClientSecret();

                    // Hook up the pay button to the card widget and stripe instance
                    cardInputWidget = findViewById(R.id.cardInputWidget);
                    Button payButton = findViewById(R.id.payButton);
                    payButton.setOnClickListener(view -> {
                        custPrograssbar.prograssCreate(StripPaymentActivity.this);
                        PaymentMethodCreateParams params1 = cardInputWidget.getPaymentMethodCreateParams();
                        if (params1 != null) {
                            ConfirmPaymentIntentParams confirmParams = ConfirmPaymentIntentParams
                                    .createWithPaymentMethodCreateParams(params1, paymentIntentClientSecret);
                            final Context context = StripPaymentActivity.this.getApplicationContext();
                            stripe = new Stripe(
                                    context,
                                    PaymentConfiguration.getInstance(context).getPublishableKey()
                            );
                            stripe.confirmPayment(StripPaymentActivity.this, confirmParams);
                        }
                    });
                } catch (StripeException e) {
                    e.printStackTrace();
                }
            }
        }.start();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Handle the result of stripe.confirmPayment
        stripe.onPaymentResult(requestCode, data, new PaymentResultCallback(this));
    }

    private final class PaymentResultCallback
            implements ApiResultCallback<PaymentIntentResult> {
        @NonNull
        private final WeakReference<StripPaymentActivity> activityRef;

        PaymentResultCallback(@NonNull StripPaymentActivity activity) {
            activityRef = new WeakReference<>(activity);
        }

        @Override
        public void onSuccess(@NonNull PaymentIntentResult result) {
            final StripPaymentActivity activity = activityRef.get();
            custPrograssbar.closePrograssBar();
            if (activity == null) {
                return;
            }

            com.stripe.android.model.PaymentIntent paymentIntent = result.getIntent();
            com.stripe.android.model.PaymentIntent.Status status = paymentIntent.getStatus();
            if (status == com.stripe.android.model.PaymentIntent.Status.Succeeded) {
                // Payment completed successfully
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                try {
                    String resdpons = gson.toJson(paymentIntent);
                    JSONObject object = new JSONObject(resdpons);
                    Utility.tragectionID = object.getString("id");
                    Utility.paymentsucsses = 1;
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else if (status == com.stripe.android.model.PaymentIntent.Status.RequiresPaymentMethod) {
                // Payment failed

                Log.e("Payment failed", "Payment failed");

            }
        }

        @Override
        public void onError(@NonNull Exception e) {
            custPrograssbar.closePrograssBar();
            final StripPaymentActivity activity = activityRef.get();
            if (activity == null) {
                return;
            }

            Log.e("Error", "-->" + e.toString());
        }
    }
}