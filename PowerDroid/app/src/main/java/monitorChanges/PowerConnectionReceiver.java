package monitorChanges;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by jhoanes on 27/11/15.
 */
public class PowerConnectionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        //Intent batteryStatus = context.registerReceiver(null, ifilter);

        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;

        int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

        //Determinando o nível da bateria
        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryPct = (level / (float) scale) * 100;

        //if(batteryPct > 20){
        //Pega a conectividade à rede
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();


        Log.i("IsCo", String.valueOf(isConnected));
        String n = "" + isConnected;
        if(isConnected)
            Toast.makeText(context, "Conected", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(context, "NoConected", Toast.LENGTH_LONG).show();
        //}

        //Pega o tipo de conecção
        boolean isWiFi =  activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
        Toast.makeText(context, "" + isWiFi, Toast.LENGTH_LONG).show();

        String b = "Bateria em " + (int) batteryPct + "%";
        Toast.makeText(context, b, Toast.LENGTH_LONG).show();

        BatteryLevelReceiver batteryLevelReceiver = new BatteryLevelReceiver();

        batteryLevelReceiver.onReceive(context, intent);
    }
}
