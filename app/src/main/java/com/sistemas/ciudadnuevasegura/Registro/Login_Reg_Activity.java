package com.sistemas.ciudadnuevasegura.Registro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;

import com.sistemas.ciudadnuevasegura.Login.Login_Activity;
import com.sistemas.ciudadnuevasegura.R;

public class Login_Reg_Activity extends AppCompatActivity {


    Button buttonLogin_reg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_reg);

        // Llamar a tu método después de que la actividad se haya creado
        realizarOperacionesDespuesDeLaCreacion();
    }

    private void realizarOperacionesDespuesDeLaCreacion() {
        // Obtener un contexto válido utilizando getApplicationContext()
        Context context = getApplicationContext();

        // Utilizar el contexto para inicializar SharedPreferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        // Verificar si la Actividad 2 ya se ha abierto
        boolean actividad2Abierta = preferences.getBoolean("actividad2_abierta", false);

        Intent intent;
        if (actividad2Abierta) {
            // Si la Actividad 2 ya se ha abierto, ir directamente a la Actividad 3
            intent = new Intent(this, Login_Activity.class);
        } else {
            // Si la Actividad 2 no se ha abierto, abrir la Actividad 2
            intent = new Intent(this, Login_Reg_Activity.class);

            // Marcar la Actividad 2 como abierta
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("actividad2_abierta", true);
            editor.apply();
        }

        startActivity(intent);
        finish(); // Esto evita que el usuario vuelva a la Actividad actual al presionar el botón "Atrás"
    }
}