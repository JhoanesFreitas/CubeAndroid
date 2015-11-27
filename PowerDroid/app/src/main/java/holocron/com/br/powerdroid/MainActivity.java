package holocron.com.br.powerdroid;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import monitorChanges.PowerConnectionReceiver;

public class MainActivity extends AppCompatActivity {

    private TextView text;
    private TextView textCharge;
    private String[] mPlanetTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Desenvolvido por Holocron", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //Pega os processos em execução
        //ActivityManager activityManager = (ActivityManager)this.getSystemService(this.ACTIVITY_SERVICE);
        //String k = "" + activityManager.getRunningAppProcesses();
        //Toast.makeText(this, k, Toast.LENGTH_LONG).show();
        //end

        //Esse codigo faz com q a tela fique sempre acordada
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //Para voltar faz:
        //getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //Pegar a quantidade de bateria usado, voltagem e a temperatura
        BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
            int scale = -1;
            int level = -1;
            int voltage = -1;
            int temp = -1;
            @Override
            public void onReceive(Context context, Intent intent) {
                level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
                voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
                double tp = temp / 10.0;
                Toast.makeText(MainActivity.this, "Nível da bateria " + level + "\nTemperatuda: " + tp
                        + "ºC\nVoltagem: " + voltage, Toast.LENGTH_LONG).show();
                Log.e("BatteryManager", "level is " + level + "/" + scale + ", temp is " + temp + ", voltage is " + voltage);
            }
        };
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryReceiver, filter);
        //End

        text = (TextView) findViewById(R.id.text);
        textCharge = (TextView) findViewById(R.id.textCharge);

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void clearAndroid(View view){
        //Toast.makeText(this,"Teste 123", Toast.LENGTH_SHORT).show();

        //Toast.makeText(this, a, Toast.LENGTH_LONG).show();
        pegarEstadoCarregamento(this);
    }

    private void pegarEstadoCarregamento(Context context){

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);

        //Está carregando?
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;

        //Como está carregando?
        int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

        PowerConnectionReceiver powerConnectionReceiver = new PowerConnectionReceiver();
        powerConnectionReceiver.onReceive(this, batteryStatus);

        if(isCharging){
            text.setText(R.string.isCharge);
        }
        else{
            text.setText(R.string.isNotCharge);
            textCharge.setText("");
        }

        if(usbCharge){
            textCharge.setText(R.string.isUSB);
        }

        if(acCharge){
            textCharge.setText(R.string.isAc);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
