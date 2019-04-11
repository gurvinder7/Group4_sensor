package com.example.group4_sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class Sensors extends AppCompatActivity implements SensorEventListener {

    private Sensor accelerometerSensor;
    private Sensor proximitySensor;
    private Sensor lightSensor;
    private Sensor stepCounterSensor;
    private Sensor tempSensor;
    private Sensor gyroscopeSensor;
    //private Sensor magnetometer;
    private Float light;
    private SensorManager sensorManager;
    TextView sensortxt;

    private int currentSensor;
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 600;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensors);
        sensortxt = findViewById(R.id.txtv_sensor);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
           // String value = extras.getString("lightsensor");
            //Float light = extras.getFloat("lightsensor");
            currentSensor=extras.getInt("sensor");
            //sensortxt.setText(String.valueOf(light));
            //The key argument here must match that used in the other activity
        }
        inializesensor();


    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == currentSensor) {

            if (currentSensor == Sensor.TYPE_LIGHT) {
                float valueZ = event.values[0];
                light = valueZ;

                System.out.println("---inside light "+light);
                String out="Brightness " + valueZ;
                sensortxt.setText(out);

                // textView.setText("Brightness " + valueZ);
            } else if (currentSensor == Sensor.TYPE_PROXIMITY) {

                float distance = event.values[0];
                sensortxt.setText("proximity: "+distance);
                System.out.println("---inside distance "+distance);

                //textView.setText("Proximity " + distance);
            }//else if (currentSensor == Sensor.TYPE_MAGNETIC_FIELD) {

               // float magnet = event.values[0];
                //sensortxt.setText("magnet: "+magnet);
                //System.out.println("---inside magnet "+magnet);

                //textView.setText("Proximity " + distance);
          //  }
            else if (currentSensor == Sensor.TYPE_STEP_COUNTER) {
                float steps = event.values[0];
                sensortxt.setText("steps: "+steps);
                System.out.println("---inside step_detect "+steps);

                // textView.setText("Steps : " + steps);
            } else if (currentSensor == Sensor.TYPE_ACCELEROMETER) {
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                long curTime = System.currentTimeMillis();

                if ((curTime - lastUpdate) > 100) {
                    long diffTime = (curTime - lastUpdate);
                    lastUpdate = curTime;

                    float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;

                    if (speed > SHAKE_THRESHOLD) {
                        Toast.makeText(getApplicationContext(), "Your phone just shook", Toast.LENGTH_LONG).show();
                    }

                    last_x = x;
                    last_y = y;
                    last_z = z;
                    System.out.println("---inside accel"+last_x+""+last_y+""+last_z);
                    sensortxt.setText("X: "+last_x+" Y: "+last_y+" Z: "+last_z);
                }
            } else if (currentSensor == Sensor.TYPE_GYROSCOPE) {
                if (event.values[2] > 0.5f) {

                    sensortxt.setText("Anti Clock");
                } else if (event.values[2] < -0.5f) {

                    sensortxt.setText("Clock");
                }
            } else if (currentSensor == Sensor.TYPE_AMBIENT_TEMPERATURE) {
                System.out.println("---inside temp"+event.values[0]);
                sensortxt.setText("Ambient Temp in Celsius :" + event.values[0]);
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

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

        sensorReg();
    }
    public void sensorReg(){
        sensorManager.registerListener(this, accelerometerSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, lightSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, proximitySensor,
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, stepCounterSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, tempSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, gyroscopeSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
        //sensorManager.registerListener(this,magnetometer,sensorManager.SENSOR_DELAY_NORMAL);
    }


    @Override
    protected void onResume() {
        super.onResume();

        sensorReg();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}
