package nitish.build.com.post_test1;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText et_apiUrl;
    Button btn_execute, btn_PostHistory;
    TextView tv_devID, tv_imei1, tv_imei2,tv_cur_url;
    String cur_apiUrl;
    SharedPreferences post_pref;


    public void volley_test() {
        try {
//            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String url_p = "http://promadherchod.xyz/test.php";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("device_id", "dg234k2h3k42g3g");
            jsonBody.put("imei", "6461298353453535");
            jsonBody.put("sim", "1");
            jsonBody.put("timestamp", "1560573718");
            jsonBody.put("no", "8317369553");
            jsonBody.put("message", "This is a test message");
            final String requestBody = jsonBody.toString();

            //{"device_id":"dg234k2h3k42g3g","imei":"6461298353453535","sim":"1"
            // ,"timestamp":"1560573718","no":"8317369553","message":"This is a test message"}

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url_p, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.i("VOLLEY_R", response.toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();

                    Log.i("VOLLEY_E", error.toString());
                }
            });

            Volley.newRequestQueue(this).add(jsonRequest);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        post_pref = getApplicationContext().getSharedPreferences(getResources().getString(R.string.pref_post_main), MODE_PRIVATE);

        btn_execute = findViewById(R.id.btn_execute);
        et_apiUrl = findViewById(R.id.et_api_url);
        tv_devID = findViewById(R.id.tv_devId);
        tv_imei1 = findViewById(R.id.tv_imei1);
        tv_imei2 = findViewById(R.id.tv_imei2);
        btn_PostHistory = findViewById(R.id.btn_History);
        tv_cur_url = findViewById(R.id.tv_cur_url);

        btn_execute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_PostHistory.callOnClick();
                String enteredUrl = et_apiUrl.getText().toString();
                SharedPreferences.Editor et = post_pref.edit();
                et.putString(getResources().getString(R.string.pref_post_api_url),enteredUrl).apply();
                cur_apiUrl = post_pref.getString(getResources().getString(R.string.pref_post_api_url), "http://promadherchod.xyz/test.php");
                tv_cur_url.setText("Started @ :"+cur_apiUrl);
                if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
                    startForegroundService(new Intent(getApplicationContext(),PostService.class));
                }else
                    startService(new Intent(getApplicationContext(),PostService.class));
            }
        });




        TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    1);
            return;
        }else {
            String temp1 = manager.getDeviceId(0);
            String temp2 = manager.getDeviceId(1);

            SharedPreferences.Editor editor = post_pref.edit();
            editor.putString(getResources().getString(R.string.pref_post_imei1),temp1).apply();
            editor.putString(getResources().getString(R.string.pref_post_imei2),temp2).apply();

            if (manager.getPhoneCount() == 1) {
                tv_imei1.setText("imei1 : "+temp1);
                tv_imei2.setText("imei2 : ---");
            }else if (manager.getPhoneCount() == 2){
                tv_imei1.setText("imei1 : "+temp1);
                tv_imei2.setText("imei2 : "+temp2);
            }
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_SMS},
                    1);
            return;
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent();
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                startActivity(intent);
            }
        }




        btn_PostHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(new Intent(getApplicationContext(),PostService.class));
                tv_cur_url.setText("Service Stoped");
            }
        });


        PowerManager mgr = (PowerManager)getApplicationContext().getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"MyPostMan:MyWakeLock");
        wakeLock.acquire();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(new NetWatcher(), intentFilter);

        String unique_id = android.provider.Settings.Secure
                .getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        tv_devID.setText("device_id : " + unique_id);





        cur_apiUrl = post_pref.getString(getResources().getString(R.string.pref_post_api_url), "http://promadherchod.xyz/test.php");
        tv_cur_url.setText("Started @ :"+cur_apiUrl);

    }
}
