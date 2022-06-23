package com.cscodetech.pharmafast.utiles;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

public class CustPrograssbar {
    ProgressDialog progressDialog;

    public void prograssCreate(Context context) {
        try {

            if (progressDialog != null && progressDialog.isShowing()) {
                Log.e("Error","Show!");
            } else {
                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("Progress...");
                progressDialog.show();
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void closePrograssBar() {
        if (progressDialog != null) {
            try {
                progressDialog.cancel();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
