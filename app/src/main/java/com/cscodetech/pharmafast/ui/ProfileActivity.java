package com.cscodetech.pharmafast.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.cscodetech.pharmafast.R;
import com.cscodetech.pharmafast.locationpick.LocationGetActivity;
import com.cscodetech.pharmafast.locationpick.MapUtility;
import com.cscodetech.pharmafast.model.Register;
import com.cscodetech.pharmafast.retrofit.APIClient;
import com.cscodetech.pharmafast.retrofit.GetResult;
import com.cscodetech.pharmafast.utiles.CustPrograssbar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class ProfileActivity extends RootActivity implements GetResult.MyListener {

    @BindView(R.id.ed_firstname)
    EditText edFirstname;
    @BindView(R.id.ed_lastname)
    EditText edLastname;
    @BindView(R.id.ed_email)
    EditText edEmail;
    @BindView(R.id.ed_password)
    EditText edPassword;
    @BindView(R.id.btn_countinue)
    TextView btnCountinue;
    String phonenumber;
    String phonecode;
    CustPrograssbar custPrograssbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        custPrograssbar=new CustPrograssbar();
        phonenumber = getIntent().getStringExtra("phone");
        phonecode = getIntent().getStringExtra("code");
    }

    @OnClick(R.id.btn_countinue)
    public void onClick() {
        if (validation()) {
            createUser();
        }


    }

    public boolean validation() {
        if (edFirstname.getText().toString().isEmpty()) {
            edFirstname.setError("Enter First Name");
            return false;
        }
        if (edEmail.getText().toString().isEmpty()) {
            edEmail.setError("Enter Valid Email");
            return false;
        }
        if (edLastname.getText().toString().isEmpty()) {
            edLastname.setError("Enter Last Name");
            return false;
        }
        if (edPassword.getText().toString().isEmpty()) {
            edPassword.setError("Enter Password");
            return false;
        }
        return true;
    }

    private void createUser() {
        custPrograssbar=new CustPrograssbar();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("fname", edFirstname.getText().toString());
            jsonObject.put("lname", edLastname.getText().toString());
            jsonObject.put("email", edEmail.getText().toString());
            jsonObject.put("mobile", phonenumber);
            jsonObject.put("ccode", phonecode);
            jsonObject.put("password", edPassword.getText().toString());
            RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
            Call<JsonObject> call = APIClient.getInterface().getRegister(bodyRequest);
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "1");
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
                Register register = gson.fromJson(result.toString(), Register.class);
                OneSignal.sendTag("userid", register.getUserid());
                startActivity(new Intent(ProfileActivity.this, LocationGetActivity.class)
                        .putExtra(MapUtility.latitude, 0.0)
                        .putExtra(MapUtility.longitude, 0.0)
//                        .putExtra("landmark","")
//                        .putExtra("hno", "")
                        .putExtra("atype", "Home")
                        .putExtra("newuser", "Newuser")
                        .putExtra("userid", register.getUserid())
                        .putExtra("aid", "0")
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));


            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}