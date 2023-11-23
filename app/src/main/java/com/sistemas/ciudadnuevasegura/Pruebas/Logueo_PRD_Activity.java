package com.sistemas.ciudadnuevasegura.Pruebas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sistemas.ciudadnuevasegura.R;

public class Logueo_PRD_Activity extends AppCompatActivity {
    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonLogin;

    private static final String PREF_NAME = "MisPreferencias";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logueo_prd);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);

        // Recuperar las credenciales guardadas (si existen)
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String savedUsername = sharedPreferences.getString(KEY_USERNAME, "");
        String savedPassword = sharedPreferences.getString(KEY_PASSWORD, "");

        // Mostrar las credenciales guardadas
        editTextUsername.setText(savedUsername);
        editTextPassword.setText(savedPassword);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener las credenciales ingresadas
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();

                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
                    // Guardar las credenciales en SharedPreferences
                    guardarCredenciales(username, password);
                    mostrarMensaje("Inicio de sesión exitoso");
                } else {
                    mostrarMensaje("Por favor, ingrese las credenciales");
                }
            }
        });
    }

    private void guardarCredenciales(String username, String password) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Guardar las credenciales
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_PASSWORD, password);

        // Aplicar los cambios
        editor.apply();
    }

    private void mostrarMensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }
}