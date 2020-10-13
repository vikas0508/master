package com.e.ryedup.notification;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import android.util.Log;


import androidx.core.app.NotificationCompat;

import com.e.ryedup.ChatActivity;
import com.e.ryedup.HomeActivity;
import com.e.ryedup.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import Utils.Constants;

//import android.app.Notification;


/**
 * Created by eweb-a1-pc-14 on 9/1/2016.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    Context context;
    private static final String TAG = "FCM Service";
    private SharedPreferences preference;
    private String loginUserId="",UserType=" ",type="";
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    public static final String PRIMARY_CHANNEL = "default";
    Intent intent;
    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        // Check if message contains a data payload.

        Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e("FCM", "Notification Message Body: " + remoteMessage.getNotification().getIcon());
           // printdata(remoteMessage.getNotification().getBody());
            if(remoteMessage.getNotification().getIcon()!=null){
                Notification("Ryde Up",remoteMessage.getNotification().getBody(),remoteMessage.getNotification().getIcon());
            }else {
                Notification("Ryde Up",remoteMessage.getNotification().getBody(),"");
            }


        }
        if (remoteMessage.getData()!=null){
            Log.e("message", remoteMessage.getData().toString());
            printdata(remoteMessage.getData().toString());
            //Notification("",remoteMessage.getData().get("message"),"","","","");
            // eg. Server Send Structure data:{"post_id":"12345","post_title":"A Blog Post"}
           // printdata(remoteMessage.getData().get("message"));
           // {"message":"Batt  *commented* on your post","item_id":"1773","user_id":4,"type":3,"post_type":2}
        }


    }
    private void printdata( String message) {
        Log.e("aaa",message);
        preference = getSharedPreferences(Constants.Shared_Pref, Context.MODE_PRIVATE);
        UserType = preference.getString("user_type", "");
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(message);
            //String sms = jsonObject.getString("message");
           // String sms= String.valueOf(Html.fromHtml(noti_text));
            String title="RydeUp";
            type=jsonObject.getString("requestStatus");
            String reqId=jsonObject.getString("requestID");
            String badge=jsonObject.getString("badge");
           // Notification(title,sms,type,reqId);

             ///// send broadcast request
                    if (UserType.equalsIgnoreCase("3")) {

                    }else {
                        Intent intent = new Intent();
                        intent.setAction("com.e.ryedup.CUSTOM_INTENT");
                        intent.putExtra("status",type);
                        intent.putExtra("reqId",reqId);
                        intent.putExtra("count",badge);
                        sendBroadcast(intent);
                    }


        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("error",e.toString());
        }
      //  Notification Message Body: {"rstatus":2,"message":"Your request is accepted!","reqId":"41"}
   }
   @SuppressLint("NewApi")
   public void Notification(String title, String sms,String icon) {

        if (icon.equalsIgnoreCase("chat")){
            intent = new Intent(this,ChatActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("driverid",HomeActivity.homeActivity.driver_id);
            intent.putExtra("driver_name",HomeActivity.homeActivity.driver_name);
            intent.putExtra("request_id",HomeActivity.homeActivity.request_id);
            intent.putExtra("driver_image",HomeActivity.homeActivity.driver_image);
            intent.putExtra("deriver_token",HomeActivity.homeActivity.driver_token);
            intent.putExtra("device_type",HomeActivity.homeActivity.device_type);
        }else {
            intent = new Intent(this,HomeActivity.class);
          //  intent.putExtra("noti_type","home");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
      /* intent.putExtra("type",type);
       intent.putExtra("user_id",user_id);
       intent.putExtra("item_id",item_id);*/
       PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent,
               PendingIntent.FLAG_UPDATE_CURRENT);

       String channelId = getString(R.string.app_name);
       Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

      NotificationCompat.Builder notificationBuilder=new NotificationCompat.Builder(this,channelId)
                       .setSmallIcon(R.mipmap.ic_launcher)
                       .setContentTitle(title)
                       .setContentText(sms)
                       .setAutoCancel(true)
                        .setPriority(Notification.PRIORITY_HIGH)
                       .setSound(defaultSoundUri)
                       .setContentIntent(pendingIntent);
       NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

       // Since android Oreo notification channel is needed.
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
           NotificationChannel channel = new NotificationChannel(channelId,
                   "Channel human readable title",
                   NotificationManager.IMPORTANCE_DEFAULT);
           notificationManager.createNotificationChannel(channel);
       }

       notificationManager.notify(0 , notificationBuilder.build());

   }
//12-20 17:52:54.986 2332-2332/com.c.infrazone E/FCMID: =======cVmVcsySZt4:APA91bEjpKISz_IaWYSS370LHnN_6YG-vpnyrmn12iKbV6vaOJKdJ3iyMHdQ0DnJdxWGD44FAKgQpfVsiMcPHeloxK7k3o-qau3gBfdzov4bI4IRytfWED_zmVZA74wsih74qAuzKWlU


}