package com.sistemas.ciudadnuevasegura;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class RecepcionDate_Activity extends AppCompatActivity {
    private TextView editText1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recepcion_date);

        editText1 = findViewById(R.id.editText1);

        // Recuperar el texto de SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("my_shared_preferences", Context.MODE_PRIVATE);
        String receivedText = sharedPreferences.getString("text", "");

        // Establecer el texto recibido en el TextView
        editText1.setText(receivedText);
    }
}