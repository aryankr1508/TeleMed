package com.cscodetech.pharmafast.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cscodetech.pharmafast.R;
import com.cscodetech.pharmafast.model.User;
import com.cscodetech.pharmafast.utiles.CustPrograssbar;
import com.cscodetech.pharmafast.utiles.SessionManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.cscodetech.pharmafast.utiles.SessionManager.uploadpref;

public class SettingActivity extends RootActivity {
    SessionManager sessionManager;
    User user;
    @BindView(R.id.txtfirstl)
    TextView txtfirstl;
    @BindView(R.id.txt_name)
    TextView txtName;
    @BindView(R.id.txt_mob)
    TextView txtMob;
    @BindView(R.id.txt_copyr)
    TextView txtCopyr;
    @BindView(R.id.lvl_myprescription)
    LinearLayout lvlMyprescription;
    CustPrograssbar custPrograssbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(SettingActivity.this);
        user = sessionManager.getUserDetails("");
        char first = user.getFname().charAt(0);
        txtfirstl.setText("" + first);
        txtName.setText("" + user.getFname());
        txtMob.setText("" + user.getMobile());
        String sourceString = "Developed by <b>" + "20 'D' Boys" + "</b> ";
        txtCopyr.setText(Html.fromHtml(sourceString));
        if (sessionManager.getStringData(uploadpref).equalsIgnoreCase("1")) {
            lvlMyprescription.setVisibility(View.VISIBLE);
        } else {
            lvlMyprescription.setVisibility(View.GONE);

        }

    }

    @OnClick({R.id.lvl_order, R.id.lvl_myprescription, R.id.lvl_address, R.id.lvl_about, R.id.lvl_contect, R.id.lvl_privacy, R.id.lvl_teams, R.id.lvl_logot, R.id.lvl_edit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lvl_order:
                startActivity(new Intent(SettingActivity.this, OrderActivity.class));
                break;
            case R.id.lvl_myprescription:
                startActivity(new Intent(SettingActivity.this, PrescriptionOrderActivity.class));

                break;
            case R.id.lvl_address:
                startActivity(new Intent(SettingActivity.this, AddressActivity.class));
                break;
            case R.id.lvl_about:
                startActivity(new Intent(SettingActivity.this, AboutActivity.class));
                break;
            case R.id.lvl_contect:
                startActivity(new Intent(SettingActivity.this, ContactActivity.class));

                break;
            case R.id.lvl_privacy:
                startActivity(new Intent(SettingActivity.this, PrivacyPolicyActivity.class));

                break;
            case R.id.lvl_teams:
                startActivity(new Intent(SettingActivity.this, TramandconditionActivity.class));
                break;
            case R.id.lvl_logot:
                sessionManager.logoutUser();
                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                break;
            case R.id.lvl_edit:
                startActivity(new Intent(SettingActivity.this, EditProfileActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sessionManager != null) {
            user = sessionManager.getUserDetails("");
            char first = user.getFname().charAt(0);
            txtfirstl.setText("" + first);
            txtName.setText("" + user.getFname());
            txtMob.setText("" + user.getMobile());
        }
    }
}