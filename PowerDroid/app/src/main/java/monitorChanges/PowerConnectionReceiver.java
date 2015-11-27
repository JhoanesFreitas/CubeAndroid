package monitorChanges;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.util.Log;
import android.widget.Toast;

import holocron.com.br.powerdroid.R;


/**
 * Created by jhoanes on 27/11/15.
 */
public class PowerConnectionReceiver extends BroadcastReceiver {

    private int scale = -1;
    private int level = -1;
    private int voltage = -1;
    private int temp = -1;

    @Override
    public void onReceive(Context context, Intent intent) {

        level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
        voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);

        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);

        double tp = temp / 10.0;

        Toast.makeText(context, "Nível da bateria: " + level + "%\n" + "Temperatura: "  + tp
                + "ºC\n" + "Voltagem: " + voltage, Toast.LENGTH_LONG).show();
        Log.e("BatteryManager", "level is " + level + "/" + scale + ", temp is " + temp + ", voltage is " + voltage);
        //End

        //Pega a conectividade à rede
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();


        Log.i("IsCo", String.valueOf(isConnected));

        if (isConnected)
            Toast.makeText(context, R.string.connected, Toast.LENGTH_LONG).show();
        else
            Toast.makeText(context, R.string.noConnected, Toast.LENGTH_LONG).show();
        //}

        //Pega o tipo de conecção
        boolean isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
        Toast.makeText(context, isWiFi ? "Connected via WiFi" : "No Connected via WiFi", Toast.LENGTH_LONG).show();

        BatteryLevelReceiver batteryLevelReceiver = new BatteryLevelReceiver();

        batteryLevelReceiver.onReceive(context, intent);
    }
}
