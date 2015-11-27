package monitorChanges;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Created by jhoanes on 27/11/15.
 */
public class BatteryLevelReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //Nao sei se isso e aqui...
        //Nao entendi bem...
        //Veja: https://developer.android.com/intl/pt-br/training/monitoring-device-state/docking-monitoring.html#CurrentDockState

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_DOCK_EVENT);
        Intent dockStatus = context.registerReceiver(null, ifilter);

        int dockState = intent.getIntExtra(Intent.EXTRA_DOCK_STATE, -1);
        boolean isDocked = dockState != Intent.EXTRA_DOCK_STATE_UNDOCKED;

        boolean isCar = dockState == Intent.EXTRA_DOCK_STATE_CAR;
        boolean isDeck = dockState == Intent.EXTRA_DOCK_STATE_DESK ||
                dockState == Intent.EXTRA_DOCK_STATE_LE_DESK ||
                dockState == Intent.EXTRA_DOCK_STATE_HE_DESK;

        /*String g = "" + isDocked;
        Toast.makeText(context, g, Toast.LENGTH_LONG).show();
        String g1 = "" + isCar;
        Toast.makeText(context, g1, Toast.LENGTH_LONG).show();
        String g2 = "" + isDeck;
        Toast.makeText(context, g2, Toast.LENGTH_LONG).show();*/
    }
}
