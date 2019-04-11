package com.example.group4_sensor;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView sensortxtv;
    private SensorManager sensorManager;

    private Sensor accelerometerSensor;
    private Sensor proximitySensor;
    private Sensor lightSensor;
    private Sensor stepCounterSensor;
    private Sensor tempSensor;
    private Sensor gyroscopeSensor;
   // private Sensor magnetometer;
    private Float light;

    private int currentSensor;

    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 600;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sensortxtv = findViewById(R.id.txtv_sensor);
        inializesensor();
        setSensortxtv();





        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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


  /*  @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometerSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
        //sensorManager.registerListener(this, lightSensor,
        //        SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, proximitySensor,
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, stepCounterSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, tempSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, gyroscopeSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_acc) {




            if (checkSensorAvailability(Sensor.TYPE_ACCELEROMETER)) {
                currentSensor = Sensor.TYPE_ACCELEROMETER;

                Intent intent = new Intent(this, Sensors.class);
                intent.putExtra("sensor",currentSensor);
                startActivity(intent);

            }


            // Handle the camera action
        } else if (id == R.id.nav_proxy) {
            if(checkSensorAvailability((Sensor.TYPE_PROXIMITY))){
                currentSensor=Sensor.TYPE_PROXIMITY;
                Intent intent = new Intent(this, Sensors.class);
                intent.putExtra("sensor",currentSensor);
                startActivity(intent);
            }
        } else if (id == R.id.nav_light) {

            if (checkSensorAvailability(Sensor.TYPE_LIGHT)) {
                currentSensor = Sensor.TYPE_LIGHT;
                //onSensorChanged(new SensorEvent());
                Intent intent = new Intent(this, Sensors.class);
                System.out.println("light:"+light);
                //intent.putExtra("lightsensor",light);
                intent.putExtra("sensor",currentSensor);
                startActivity(intent);
            }
            else{
                Toast.makeText(this,"sensor is not Available",Toast.LENGTH_SHORT).show();

            }


        } else if (id == R.id.nav_step) {
            if (checkSensorAvailability(Sensor.TYPE_STEP_COUNTER)){
                currentSensor = Sensor.TYPE_STEP_COUNTER;
                Intent intent = new Intent(this, Sensors.class);
                intent.putExtra("sensor",currentSensor);
                startActivity(intent);
            }


        } else if (id == R.id.nav_gest) {
            Intent intent = new Intent(this, GestureMain.class);
            startActivity(intent);
        } else if (id == R.id.nav_magnet) {
            //if (checkSensorAvailability(Sensor.TYPE_MAGNETIC_FIELD)){
              //  currentSensor = Sensor.TYPE_MAGNETIC_FIELD;
                Intent intent = new Intent(this, Direction_sensor.class);
               // intent.putExtra("sensor",currentSensor);
                startActivity(intent);



        } else if (id == R.id.nav_temp) {
            if (checkSensorAvailability(Sensor.TYPE_AMBIENT_TEMPERATURE)){
                currentSensor = Sensor.TYPE_AMBIENT_TEMPERATURE;
                Intent intent = new Intent(this, Sensors.class);
                intent.putExtra("sensor",currentSensor);
                startActivity(intent);
            }

        } else if (id == R.id.nav_gyro) {
            if (checkSensorAvailability(Sensor.TYPE_GYROSCOPE)){
                currentSensor = Sensor.TYPE_GYROSCOPE;
                Intent intent = new Intent(this, Sensors.class);
                intent.putExtra("sensor",currentSensor);
                startActivity(intent);
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void inializesensor(){
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        tempSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
       // magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }
    public boolean checkSensorAvailability(int sensorType) {
        boolean isSensor = false;
        if (sensorManager.getDefaultSensor(sensorType) != null) {
            isSensor = true;
        }
        return isSensor;
    }

    public void setSensortxtv() {

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensortxtv = findViewById(R.id.txtv_sensor);
        List<Sensor> sensorlist = sensorManager.getSensorList(Sensor.TYPE_ALL);
        StringBuilder stringBuilder = new StringBuilder();
        for (Sensor s : sensorlist) {
            stringBuilder.append(s.getName() + "\n");
        }
        sensortxtv.setVisibility(View.VISIBLE);
        sensortxtv.setText(stringBuilder);
    }
}
