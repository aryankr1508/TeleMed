package com.cscodetech.pharmafast.ui;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cscodetech.pharmafast.R;
import com.cscodetech.pharmafast.utiles.SessionManager;
import com.google.android.material.appbar.AppBarLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.cscodetech.pharmafast.utiles.SessionManager.about;

public class AboutActivity extends AppCompatActivity {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_actiontitle)
    TextView tatActionTiter;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.txt_dscirtion)
    TextView tatDeception;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        sessionManager = new SessionManager(AboutActivity.this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tatDeception.setText(Html.fromHtml(sessionManager.getStringData(about), Html.FROM_HTML_MODE_LEGACY));
        } else {
            tatDeception.setText(Html.fromHtml(sessionManager.getStringData(about)));
        }
    }

    @OnClick(R.id.img_back)
    public void onClick() {
        finish();
    }
}