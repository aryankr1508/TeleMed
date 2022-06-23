package com.cscodetech.pharmafast.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.cscodetech.pharmafast.R;
import com.cscodetech.pharmafast.fragment.CategoryFragment;
import com.cscodetech.pharmafast.fragment.HomeFragment;
import com.cscodetech.pharmafast.model.AddressList;
import com.cscodetech.pharmafast.utiles.DatabaseHelper;
import com.cscodetech.pharmafast.utiles.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.cscodetech.pharmafast.ui.AddressActivity.changeAddress;
import static com.cscodetech.pharmafast.utiles.SessionManager.lats;
import static com.cscodetech.pharmafast.utiles.SessionManager.SELCTADDRESS;

public class HomeActivity extends RootActivity {

    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigation;
    @BindView(R.id.container)
    FrameLayout container;
    @BindView(R.id.txt_location)
    TextView txtLocation;
    public static HomeActivity homeActivity = null;
    public static TextView txtCountcard;


    public static HomeActivity getInstance() {
        return homeActivity;
    }

    SessionManager sessionManager;
    DatabaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        sessionManager = new SessionManager(HomeActivity.this);
        helper = new DatabaseHelper(HomeActivity.this);
        homeActivity = HomeActivity.this;
        txtCountcard = findViewById(R.id.txt_countcard);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        if (sessionManager.getAddress() == null) {
            startActivity(new Intent(HomeActivity.this, AddressActivity.class));
        } else {
            setLocation(sessionManager.getAddress());
            openFragment(new HomeFragment());
            updateItem();
        }
    }

    public void updateItem() {
        Cursor res = helper.getAllData();
        if (res.getCount() == 0) {
            txtCountcard.setText("0");
        } else {
            txtCountcard.setText("" + res.getCount());
        }
    }

    public void setLocation(AddressList location) {
        if (sessionManager.getAddress() == null) {
            txtLocation.setText("Select Address");

        } else {
            txtLocation.setText("Deliver to " + location.getAddress());

        }

    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            item -> {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        item.setIcon(R.drawable.ic_home_black);
                        openFragment(new HomeFragment());
                        return true;
                    case R.id.navigation_medicine:
                        item.setIcon(R.drawable.ic_medicine_black);
                        openFragment(new CategoryFragment());
                        return true;
                    case R.id.navigation_prescription:
                        item.setIcon(R.drawable.ic_prescription_black);
                        startActivity(new Intent(HomeActivity.this, UploadPrescriptionActivity.class));

                        return true;
                    case R.id.navigation_notifications:
                        item.setIcon(R.drawable.ic_notification_black);
                        startActivity(new Intent(HomeActivity.this, NotificationActivity.class));


                        return true;
                    case R.id.navigation_setting:
                        item.setIcon(R.drawable.ic_setting_black);
                        startActivity(new Intent(HomeActivity.this, SettingActivity.class));
                        return true;
                    default:
                        break;
                }
                return false;
            };

    public void uploadPref(String isUpload) {
        if (!isUpload.equalsIgnoreCase("1")) {
            bottomNavigation.getMenu().removeItem(R.id.navigation_prescription);
        }
    }

    @OnClick({R.id.rlt_cart, R.id.lvl_actionsearch, R.id.txt_location})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlt_cart:
                startActivity(new Intent(HomeActivity.this, CartActivity.class));
                break;
            case R.id.lvl_actionsearch:
                startActivity(new Intent(HomeActivity.this, SearchActivity.class));
                break;
            case R.id.txt_location:
                startActivity(new Intent(HomeActivity.this, AddressActivity.class));
                break;
            default:

                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (changeAddress) {
            changeAddress = false;
            setLocation(sessionManager.getAddress());
            openFragment(new HomeFragment());
            updateItem();
        }
    }

    @Override
    public void onBackPressed() {

        FragmentManager fragment = getSupportFragmentManager();


        if (fragment.getBackStackEntryCount() > 1) {
            Fragment fragmentaa = getSupportFragmentManager().findFragmentById(R.id.container);
            if (fragmentaa instanceof HomeFragment && fragmentaa.isVisible()) {
                finish();
            } else {
                super.onBackPressed();
            }
        } else {
            //Nothing in the back stack, so exit
            finish();
        }
    }


}