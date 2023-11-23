package com.sistemas.ciudadnuevasegura.GPS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.sistemas.ciudadnuevasegura.MainActivity;
import com.sistemas.ciudadnuevasegura.R;

public class NosePuede_Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nose_puede);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.parseColor("#0A708A")); //Color de Toolbar diferente
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF")); //Color de Texto Blanco
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);          // Flecha Retroceso
        getSupportActionBar().setTitle("Fuera del Limite");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) { // Verifica si se presionó la flecha hacia atrás
            // Inicia la nueva actividad aquí
            Intent intent = new Intent(this, MainActivity.class); // Reemplaza NuevaActividad con el nombre de la actividad a la que deseas navegar.
            startActivity(intent);
            return true; // Indica que has manejado la acción de la flecha hacia atrás
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}