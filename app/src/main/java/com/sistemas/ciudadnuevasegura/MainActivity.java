package com.sistemas.ciudadnuevasegura;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;

import com.sistemas.ciudadnuevasegura.Login.Login_Activity;
import com.sistemas.ciudadnuevasegura.Registro.Login_Reg_Activity;

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
                // TODO: 23/03/2024 es parte de la modificacion Login_Reg_Activity colocar en el intent
                        // Login_Activity colocar luego   Prb_EnvioDate_Activity
                imageView.setVisibility(View.INVISIBLE); // Ocultar la imagen   Seleccion_Activity  Login_operario_Activity
                Intent intent = new Intent(MainActivity.this, Login_Activity.class);   // PruebaID_Activity
                startActivity(intent); // Iniciar el siguiente Activity
                finish(); // Finalizar el MainActivity para que no se pueda volver atrás
            }
        }.start();


    }

}