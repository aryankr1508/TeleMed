package com.cscodetech.pharmafast.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cscodetech.pharmafast.R;
import com.cscodetech.pharmafast.model.ProfileUpdate;
import com.cscodetech.pharmafast.model.User;
import com.cscodetech.pharmafast.retrofit.APIClient;
import com.cscodetech.pharmafast.retrofit.GetResult;
import com.cscodetech.pharmafast.utiles.CustPrograssbar;
import com.cscodetech.pharmafast.utiles.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class EditProfileActivity extends RootActivity implements GetResult.MyListener {

    @BindView(R.id.ed_firstname)
    EditText edFirstName;
    @BindView(R.id.ed_lastname)
    EditText edLastName;
    @BindView(R.id.ed_email)
    TextView edEmail;
    @BindView(R.id.ed_password)
    EditText edPassword;
    @BindView(R.id.btn_countinue)
    TextView btuContinues;

    SessionManager sessionManager;
    User user;
    @BindView(R.id.ed_mobile)
    TextView edMobile;
    @BindView(R.id.img_back)
    ImageView imgBack;
    CustPrograssbar custPrograssbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);
        ButterKnife.bind(this);
        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(EditProfileActivity.this);
        user = sessionManager.getUserDetails("");
        edFirstName.setText(user.getFname());
        edLastName.setText(user.getLname());
        edMobile.setText(user.getMobile());
        edEmail.setText(user.getEmail());
        edPassword.setText(user.getPassword());
    }


    public boolean validation() {
        if (edFirstName.getText().toString().isEmpty()) {
            edFirstName.setError("Enter First Name");
            return false;
        }
        if (edLastName.getText().toString().isEmpty()) {
            edLastName.setError("Enter Last Name");
            return false;
        }
        if (edPassword.getText().toString().isEmpty()) {
            edPassword.setError("Enter Password");
            return false;
        }
        return true;
    }

    private void updateUser() {
        custPrograssbar.prograssCreate(EditProfileActivity.this);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", user.getId());
            jsonObject.put("fname", edFirstName.getText().toString());
            jsonObject.put("lname", edLastName.getText().toString());
            jsonObject.put("password", edPassword.getText().toString());
            RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
            Call<JsonObject> call = APIClient.getInterface().getUpdate(bodyRequest);
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
                ProfileUpdate profileUpdate = gson.fromJson(result.toString(), ProfileUpdate.class);
                Toast.makeText(EditProfileActivity.this, profileUpdate.getResponseMsg(), Toast.LENGTH_SHORT).show();
                if (profileUpdate.getResult().equalsIgnoreCase("true")) {
                    sessionManager.setUserDetails("", profileUpdate.getUser());
                    finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.img_back, R.id.btn_countinue})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_countinue:
                if (validation()) {
                    updateUser();
                }
                break;
            default:
                break;
        }
    }
}