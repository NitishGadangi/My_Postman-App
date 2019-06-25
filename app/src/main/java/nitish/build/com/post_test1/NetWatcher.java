package nitish.build.com.post_test1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

public class NetWatcher extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //here, check that the network connection is available. If yes, start your service. If not, stop your service.
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY,Boolean.FALSE)){
            //stop service
            Log.i("NETW","disc");
            Intent servInt = new Intent(context, PostService.class);
            context.stopService(servInt);
        }
        if (info != null) {
            if (info.getDetailedState()==NetworkInfo.DetailedState.CONNECTED) {
                //start service
                Log.i("NETW","conec");
                if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
                    context.startForegroundService(new Intent(context,PostService.class));
                }else
                    context.startService(new Intent(context,PostService.class));
            }
        }
    }


}
