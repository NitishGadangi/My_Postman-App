package nitish.build.com.post_test1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class StartMyServiceAtBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
            Intent serviceIntent = new Intent(context, PostService.class);
            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
                context.startForegroundService(serviceIntent);
            }else
            context.startService(serviceIntent);
        }
    }
}
