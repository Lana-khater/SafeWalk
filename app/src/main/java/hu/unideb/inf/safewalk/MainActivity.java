package hu.unideb.inf.safewalk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private SensorManager sensorManager;
    private Button StopButton;
    private float acelVal, acelLast, shake; //current-Last-Differ
    MediaPlayer player;
    private Button Location_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(sensorListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        acelVal = SensorManager.GRAVITY_EARTH;
        acelLast = SensorManager.GRAVITY_EARTH;
        shake = 0.00f;

        StopButton = (Button) findViewById(R.id.stopButton);
        StopButton.setOnClickListener(this);

        Location_btn=(Button) findViewById(R.id.location_btn);
        Location_btn.setOnClickListener(this);

    }

    public void Play(){
        if (player == null){
            player = MediaPlayer.create(this, R.raw.siren1);
        }
        player.start();
        }
    public void Stop(){
        if (player != null){
            player.release();
            player = null;
        }
    }

    public final SensorEventListener sensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {

            float x= event.values[0];
            float y= event.values[1];
            float z= event.values[2];
            acelLast = acelVal;
            acelVal = (float) Math.sqrt((double) (x*x + y*y + z*z));
            float delta = acelVal-acelLast;
            shake = shake*0.9f + delta;
            if (shake>5){
               Play();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

    };

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.stopButton:
                Stop();
                break;
            case R.id.location_btn:
                startActivity(new Intent(this, CurrecntLocation.class));
                break;
        }

    };
}
