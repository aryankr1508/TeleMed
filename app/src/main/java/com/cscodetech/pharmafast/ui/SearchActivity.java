package com.cscodetech.pharmafast.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cscodetech.pharmafast.R;
import com.cscodetech.pharmafast.adepter.ProductAdapter;
import com.cscodetech.pharmafast.model.AddressList;
import com.cscodetech.pharmafast.model.Medicine;
import com.cscodetech.pharmafast.model.Search;
import com.cscodetech.pharmafast.retrofit.APIClient;
import com.cscodetech.pharmafast.retrofit.GetResult;
import com.cscodetech.pharmafast.utiles.CustPrograssbar;
import com.cscodetech.pharmafast.utiles.SessionManager;
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

import static com.cscodetech.pharmafast.utiles.SessionManager.lats;

public class SearchActivity extends RootActivity implements GetResult.MyListener, ProductAdapter.RecyclerTouchListener {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.img_notfound)
    ImageView imgNotfound;
    @BindView(R.id.ed_search)
    EditText edSearch;
    @BindView(R.id.img_search)
    ImageView imgSearch;
    @BindView(R.id.lvl_actionsearch)
    LinearLayout lvlActionsearch;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.recycler_product)
    RecyclerView recyclerProduct;

    @BindView(R.id.lvl_notfound)
    LinearLayout lvlNotfound;
    SessionManager sessionManager;
    CustPrograssbar custPrograssbar;
    AddressList address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(SearchActivity.this);
        address=sessionManager.getAddress();
        recyclerProduct.setLayoutManager(new GridLayoutManager(SearchActivity.this, 2));
        recyclerProduct.setItemAnimator(new DefaultItemAnimator());

    }

    private void getSearch(String keyword) {
        custPrograssbar.prograssCreate(SearchActivity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("keyword", keyword);
            jsonObject.put("lats", address.getLatMap());
            jsonObject.put("longs", address.getLongMap());
        } catch (Exception e) {
            e.printStackTrace();

        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().getSearch(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1");

    }

    @OnClick({R.id.img_back, R.id.img_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_search:
                if (!edSearch.getText().toString().isEmpty()) {
                    getSearch(edSearch.getText().toString());
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
                Search search = gson.fromJson(result.toString(), Search.class);
                if (search.getResult().equalsIgnoreCase("true")) {
                    if (search.getSearchData().size() != 0) {
                        lvlNotfound.setVisibility(View.GONE);
                        recyclerProduct.setVisibility(View.VISIBLE);
                        ProductAdapter productAdapter = new ProductAdapter(SearchActivity.this, search.getSearchData(), this);
                        recyclerProduct.setAdapter(productAdapter);
                    } else {
                        lvlNotfound.setVisibility(View.VISIBLE);
                        Toast.makeText(SearchActivity.this, search.getResponseMsg(), Toast.LENGTH_LONG).show();
                        imgNotfound.setImageDrawable(getResources().getDrawable(R.drawable.ic_not_found));
                    }

                } else {
                    recyclerProduct.setVisibility(View.GONE);
                    lvlNotfound.setVisibility(View.VISIBLE);
                    imgNotfound.setImageDrawable(getResources().getDrawable(R.drawable.ic_not_found));
                    Toast.makeText(SearchActivity.this, search.getResponseMsg(), Toast.LENGTH_LONG).show();

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClickProductItem(String titel, Medicine medicine) {
        startActivity(new Intent(SearchActivity.this, ProductDetailsActivity.class).putExtra("MyClass", medicine).putParcelableArrayListExtra("PriceList", medicine.getProductInfo()).putStringArrayListExtra("ImageList", medicine.getProductImage()));

    }
}