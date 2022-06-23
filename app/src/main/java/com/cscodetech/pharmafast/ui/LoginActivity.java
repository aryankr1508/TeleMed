package com.cscodetech.pharmafast.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cscodetech.pharmafast.R;
import com.cscodetech.pharmafast.model.Codes;
import com.cscodetech.pharmafast.model.CountryCode;
import com.cscodetech.pharmafast.model.Login;
import com.cscodetech.pharmafast.model.Register;
import com.cscodetech.pharmafast.retrofit.APIClient;
import com.cscodetech.pharmafast.retrofit.GetResult;
import com.cscodetech.pharmafast.utiles.CustPrograssbar;
import com.cscodetech.pharmafast.utiles.SessionManager;
import com.cscodetech.pharmafast.utiles.Utility;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.FirebaseApp;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class LoginActivity extends RootActivity implements GetResult.MyListener {

    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.ed_mobile)
    EditText edMobile;
    @BindView(R.id.ed_email)
    EditText edEmail;
    @BindView(R.id.ed_password)
    EditText edPassword;
    @BindView(R.id.txt_olduser)
    TextView txtSingUp;
    @BindView(R.id.btn_proceed)
    TextView btnProceed;
    @BindView(R.id.lvl_singup)
    LinearLayout lvlSingup;
    @BindView(R.id.txt_newuser)
    TextView txtLogin;
    @BindView(R.id.lvl_login)
    LinearLayout lvlLogin;
    List<CountryCode> cCodes = new ArrayList<>();
    String codeSelect;
    boolean isNewuser = true;
    SessionManager sessionManager;
    CustPrograssbar custPrograssbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        FirebaseApp.initializeApp(this);
        custPrograssbar=new CustPrograssbar();
        sessionManager = new SessionManager(LoginActivity.this);
        String sourceString = "Have a <b>" + "Email/Password" + "</b> " + "Account?";
        String sourceCreate = "<b>Sign Up?</b>";
        txtSingUp.setText(Html.fromHtml(sourceString));
        txtLogin.setText(Html.fromHtml(sourceCreate));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                codeSelect = cCodes.get(position).getCcode();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        getContryCode();
    }

    private void getContryCode() {
        custPrograssbar.prograssCreate(LoginActivity.this);
        JSONObject jsonObject = new JSONObject();
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().getCountry(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1");

    }

    private void chackUser() {
        custPrograssbar.prograssCreate(LoginActivity.this);
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

    private void loginUser() {
        custPrograssbar.prograssCreate(LoginActivity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile", edEmail.getText().toString());
            jsonObject.put("password", edPassword.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();

        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().login(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "3");

    }


    @OnClick({R.id.txt_olduser, R.id.btn_proceed, R.id.txt_newuser, R.id.txt_forgotclick})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_olduser:
                txtSingUp.setVisibility(View.GONE);
                lvlSingup.setVisibility(View.GONE);
                lvlLogin.setVisibility(View.VISIBLE);
                isNewuser = false;
                break;
            case R.id.btn_proceed:
                if (isNewuser) {
                    if (!TextUtils.isEmpty(edMobile.getText().toString())) {
                        chackUser();
                    } else {
                        edMobile.setError("Enter Mobile Number");
                    }
                } else {
                    if (TextUtils.isEmpty(edEmail.getText().toString())) {
                        edEmail.setError("Enter Mobile / Email");
                    } else if (TextUtils.isEmpty(edPassword.getText().toString())) {
                        edPassword.setError("Enter Password");
                    } else {
                        loginUser();
                    }
                }
                break;
            case R.id.txt_newuser:
                isNewuser = true;
                txtSingUp.setVisibility(View.VISIBLE);
                lvlSingup.setVisibility(View.VISIBLE);
                lvlLogin.setVisibility(View.GONE);
                break;
            case R.id.txt_forgotclick:
                startActivity(new Intent(LoginActivity.this, ForgotActivity.class));
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
                List<String> Arealist = new ArrayList<>();
                for (int i = 0; i < contry.getCountryCode().size(); i++) {
                    CountryCode countryCode = contry.getCountryCode().get(i);
                    if (countryCode.getStatus().equalsIgnoreCase("1")) {
                        Arealist.add(countryCode.getCcode());
                    }
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, R.layout.spinnercode_layout, Arealist);
                dataAdapter.setDropDownViewResource(R.layout.spinnercode_layout);
                spinner.setAdapter(dataAdapter);
            } else if (callNo.equalsIgnoreCase("2")) {
                Gson gson = new Gson();
                Register register = gson.fromJson(result.toString(), Register.class);
                if (register.getResult().equalsIgnoreCase("true")) {
                    Utility.isvarification = 1;
                    startActivity(new Intent(LoginActivity.this, VerifyPhoneActivity.class).putExtra("code", codeSelect).putExtra("phone", edMobile.getText().toString()));


                } else {
                    Toast.makeText(LoginActivity.this, register.getResponseMsg(), Toast.LENGTH_SHORT).show();
                    edEmail.setText("" + edMobile.getText().toString());
                    isNewuser = false;
                    txtSingUp.setVisibility(View.GONE);
                    lvlSingup.setVisibility(View.GONE);
                    lvlLogin.setVisibility(View.VISIBLE);
                }


            } else if (callNo.equalsIgnoreCase("3")) {
                Gson gson = new Gson();
                Login login = gson.fromJson(result.toString(), Login.class);
                Toast.makeText(LoginActivity.this, login.getResponseMsg(), Toast.LENGTH_SHORT).show();
                if (login.getResult().equalsIgnoreCase("true")) {
                    OneSignal.sendTag("userid", login.getUserLogin().getId());
                    sessionManager.setStringData(SessionManager.lats, "0");
                    sessionManager.setStringData(SessionManager.SELCTADDRESS, "0");
                    sessionManager.setUserDetails("", login.getUserLogin());
                    sessionManager.setBooleanData(SessionManager.login, true);
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void bottonVelidation() {
        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.custome_demo_layout, null);
        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.show();


    }
}