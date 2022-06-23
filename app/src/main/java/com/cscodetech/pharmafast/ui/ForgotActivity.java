package com.cscodetech.pharmafast.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.cscodetech.pharmafast.R;
import com.cscodetech.pharmafast.model.Codes;
import com.cscodetech.pharmafast.model.CountryCode;
import com.cscodetech.pharmafast.model.Register;
import com.cscodetech.pharmafast.retrofit.APIClient;
import com.cscodetech.pharmafast.retrofit.GetResult;
import com.cscodetech.pharmafast.utiles.CustPrograssbar;
import com.cscodetech.pharmafast.utiles.Utility;
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

public class ForgotActivity extends RootActivity implements GetResult.MyListener {

    @BindView(R.id.ed_mobile)
    EditText edMobile;
    @BindView(R.id.spinner)
    Spinner spinner;
    String codeSelect;
    CustPrograssbar custPrograssbar;
    List<CountryCode> cCodes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        ButterKnife.bind(this);
        custPrograssbar = new CustPrograssbar();
        getContryCode();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                codeSelect = cCodes.get(position).getCcode();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getContryCode() {
        custPrograssbar.prograssCreate(ForgotActivity.this);
        JSONObject jsonObject = new JSONObject();

        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().getCountry(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1");

    }

    private void checkUser() {
        custPrograssbar.prograssCreate(ForgotActivity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile", edMobile.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();

        }

        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().getCheckMobile(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "2");

    }

    @OnClick({R.id.img_back, R.id.btn_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_submit:
                if (!edMobile.getText().toString().isEmpty()) {
                    checkUser();
                } else {
                    edMobile.setError("Enter Mobile");
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                Codes contry = gson.fromJson(result.toString(), Codes.class);
                cCodes = contry.getCountryCode();
                List<String> arrayList = new ArrayList<>();
                for (int i = 0; i < contry.getCountryCode().size(); i++) {
                    CountryCode countryCode = contry.getCountryCode().get(i);
                    if (countryCode.getStatus().equalsIgnoreCase("1")) {
                        arrayList.add(countryCode.getCcode());
                    }
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, R.layout.spinnercode_layout, arrayList);
                dataAdapter.setDropDownViewResource(R.layout.spinnercode_layout);
                spinner.setAdapter(dataAdapter);
            } else if (callNo.equalsIgnoreCase("2")) {
                Gson gson = new Gson();
                Register register = gson.fromJson(result.toString(), Register.class);
                if (register.getResult().equalsIgnoreCase("true")) {
                    Toast.makeText(ForgotActivity.this, register.getResponseMsg(), Toast.LENGTH_SHORT).show();
                } else {
                    Utility.isvarification = 0;
                    startActivity(new Intent(ForgotActivity.this, VerifyPhoneActivity.class).putExtra("code", codeSelect).putExtra("phone", edMobile.getText().toString()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}