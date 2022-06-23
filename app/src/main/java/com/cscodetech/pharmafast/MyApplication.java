package com.cscodetech.pharmafast;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.cscodetech.pharmafast.ui.OrderDetailsActivity;
import com.cscodetech.pharmafast.ui.PrescriptionOrderDetailsActivity;
import com.google.firebase.FirebaseApp;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;

public class MyApplication extends Application {
    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        FirebaseApp.initializeApp(this);
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .setNotificationOpenedHandler(new NotificationOpenedHandler())
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
    }

    public class NotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
        @Override
        public void notificationOpened(OSNotificationOpenResult result) {

            JSONObject data = result.notification.payload.additionalData;
            String activityToBeOpened;
            if (data != null) {
                activityToBeOpened = data.optString("type", null);
                if (activityToBeOpened != null && activityToBeOpened.equals("normal")) {
                    Log.i("OneSignal", "customkey set with value: " + activityToBeOpened);
                    Intent intent = new Intent(mContext, OrderDetailsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent.putExtra("oid", data.optString("order_id", null));
                    startActivity(intent);
                } else if (activityToBeOpened != null && activityToBeOpened.equals("prescription")) {
                    Intent intent = new Intent(mContext, PrescriptionOrderDetailsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent.putExtra("oid", data.optString("order_id", null));
                    startActivity(intent);
                }
            }
        }
    }
}