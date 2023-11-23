package com.sistemas.ciudadnuevasegura.Pruebas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.sistemas.ciudadnuevasegura.R;

public class Llamada_Activity extends AppCompatActivity {
    private static final int REQUEST_CALL_PERMISSION = 1;
    private Button btnCall;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llamada);

        btnCall = findViewById(R.id.btnCall);

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Comprueba si tienes permiso para realizar llamadas telefónicas.
               Metodo();
            }
        });
    }

    private void Metodo() {
        if (ContextCompat.checkSelfPermission(Llamada_Activity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            realizarLlamada();
        } else {
            // Si no tienes permiso, solicita permiso al usuario.
            ActivityCompat.requestPermissions(Llamada_Activity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
        }
    }

    private void realizarLlamada() {
        String phoneNumber = "956999928"; // Reemplaza con el número de teléfono al que quieres llamar.
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));

        try {
            startActivity(intent);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // El usuario ha concedido permisos, realiza la llamada.
                realizarLlamada();
            }
        }
    }
}