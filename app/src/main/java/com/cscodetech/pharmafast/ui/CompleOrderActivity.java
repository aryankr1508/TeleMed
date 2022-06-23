package com.cscodetech.pharmafast.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cscodetech.pharmafast.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class CompleOrderActivity extends RootActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comple_order);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.img_back, R.id.btn_myorder})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                Intent intent = new Intent(this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_myorder:
                Intent intent1 = new Intent(this, OrderActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
                finish();
                break;
            default:
                break;
        }
    }
}