package com.sistemas.ciudadnuevasegura;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.se.omapi.Session;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.Properties;
import java.util.Random;

public class CodigoValidar_Activity extends AppCompatActivity {

    private EditText editTextEmail;
    private Button buttonEnviar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codigo_validar);
        editTextEmail = findViewById(R.id.editTextEmail);
        buttonEnviar = findViewById(R.id.buttonEnviar);

    }
}