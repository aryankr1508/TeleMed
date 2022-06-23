package com.cscodetech.pharmafast.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cscodetech.pharmafast.R;
import com.cscodetech.pharmafast.adepter.CategoryAdapter;
import com.cscodetech.pharmafast.model.AddressList;
import com.cscodetech.pharmafast.model.Category;
import com.cscodetech.pharmafast.model.User;
import com.cscodetech.pharmafast.retrofit.APIClient;
import com.cscodetech.pharmafast.retrofit.GetResult;
import com.cscodetech.pharmafast.ui.HomeActivity;
import com.cscodetech.pharmafast.utiles.CustPrograssbar;
import com.cscodetech.pharmafast.utiles.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;


import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

import static com.cscodetech.pharmafast.utiles.SessionManager.lats;


public class CategoryFragment extends Fragment implements CategoryAdapter.RecyclerTouchListener, GetResult.MyListener {


    @BindView(R.id.recycler_category)
    RecyclerView recyclerCategory;
    CategoryAdapter categoryAdapter;
    User user;
    SessionManager sessionManager;
    CustPrograssbar custPrograssbar;
    AddressList address;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        ButterKnife.bind(this, view);
        sessionManager = new SessionManager(getActivity());
        custPrograssbar = new CustPrograssbar();
        user = sessionManager.getUserDetails("");
        address=sessionManager.getAddress();
        recyclerCategory.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerCategory.setItemAnimator(new DefaultItemAnimator());

        getCategory();
        return view;
    }

    private void getCategory() {
        custPrograssbar.prograssCreate(getActivity());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", user.getId());

            jsonObject.put("lats", address.getLatMap());
            jsonObject.put("longs", address.getLongMap());

        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().getCatList(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "category");

    }

    @Override
    public void callback(JsonObject result, String callNo) {
        custPrograssbar.closePrograssBar();
        try {
            if (callNo.equalsIgnoreCase("category")) {
                Gson gson = new Gson();
                Category category = gson.fromJson(result.toString(), Category.class);
                if (category.getResult().equalsIgnoreCase("true")) {
                    categoryAdapter = new CategoryAdapter(getActivity(), category.getCategoryData(), this, "viewall");
                    recyclerCategory.setAdapter(categoryAdapter);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClickCategoryItem(String titel, int position) {
        Bundle args = new Bundle();
        args.putInt("position", position);
        Fragment fragment = new SubCategoryFragment();
        fragment.setArguments(args);
        HomeActivity.getInstance().openFragment(fragment);
    }


}