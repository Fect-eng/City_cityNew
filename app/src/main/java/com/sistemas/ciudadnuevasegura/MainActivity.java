package com.sistemas.ciudadnuevasegura;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;

import com.sistemas.ciudadnuevasegura.GPS.BotonPanic_Activity;
import com.sistemas.ciudadnuevasegura.GPS.ValidarGPS_Activity;
import com.sistemas.ciudadnuevasegura.Login.Login_Activity;
import com.sistemas.ciudadnuevasegura.Pruebas.Validate_GPS_Activity;
import com.sistemas.ciudadnuevasegura.Registro.Registro_Activity;

public class MainActivity extends AppCompatActivity {


    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Establecer el icono de la aplicación
        //getSupportActionBar().setIcon(R.drawable.segura_validar); // Reemplaza "ic_launcher" con el nombre de tu icono



        imageView = findViewById(R.id.imageView);
        imageView.setVisibility(View.VISIBLE);
        // Hacer visible la imagen

        new CountDownTimer(2000, 1000) {
            public void onTick(long millisUntilFinished) {
                // No hacer nada en cada tick
            }

            public void onFinish() {    // Login_operario_Activity     Login_Activity   // Registro_Activity     //Delimitacion_gps_Activity
                // Delimitacion_gps_Activity
                imageView.setVisibility(View.INVISIBLE); // Ocultar la imagen   Seleccion_Activity  Login_operario_Activity
                Intent intent = new Intent(MainActivity.this, Login_Activity.class);
                startActivity(intent); // Iniciar el siguiente Activity
                finish(); // Finalizar el MainActivity para que no se pueda volver atrás
            }
        }.start();


    }

}