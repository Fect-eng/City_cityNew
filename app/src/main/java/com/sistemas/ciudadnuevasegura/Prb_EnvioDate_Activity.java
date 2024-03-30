package com.sistemas.ciudadnuevasegura;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Prb_EnvioDate_Activity extends AppCompatActivity {

    private TextView textView;
    private Button button;
    private String textToSend;

    Button buttonN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prb_envio_date);

        textView = findViewById(R.id.textView);
        button = findViewById(R.id.button);
        textToSend = textView.getText().toString();

        buttonN = findViewById(R.id.buttonN);
        buttonN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RecepcionDate_Activity.class);
                Toast.makeText(Prb_EnvioDate_Activity.this, "Pruebaaaaa", Toast.LENGTH_SHORT).show();
                // Limpiar_TextView();
                startActivity(intent);
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Guardar el texto en SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("my_shared_preferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("text", textToSend);
                editor.apply();
            }
        });


    }
}