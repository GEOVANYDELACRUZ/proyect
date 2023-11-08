package delacruz.examenfinal.alumnoapp.ui.Splash;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

import delacruz.examenfinal.alumnoapp.R;
import delacruz.examenfinal.alumnoapp.ui.IniciarSesion.iniciarsesion;

public class splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Intent i = new Intent(splash.this,iniciarsesion.class);
                startActivity(i);
            }
        },3000);
    }
}