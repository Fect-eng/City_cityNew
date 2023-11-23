package com.sistemas.ciudadnuevasegura.Correo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sistemas.ciudadnuevasegura.GPS.BotonPanic_Activity;
import com.sistemas.ciudadnuevasegura.R;

import java.util.HashMap;
import java.util.Map;

public class Validacion_Code_Activity extends AppCompatActivity {

    private EditText Code_EditText;
    private Button btnEnviar;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validacion_code);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.parseColor("#0A708A")); //Color de Toolbar diferente
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF")); //Color de Texto Blanco

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Validacion de Codigo !!");


        Code_EditText = findViewById(R.id.Code_EditText);
        btnEnviar = findViewById(R.id.btnEnviar);
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EnvioSiguienteAct();
               // String valor = Code_EditText.getText().toString();
                //enviarDatosAlServidor("http://192.168.0.11/CiudadNueva_Panico/code_validator.php");
               // Toast.makeText(Validacion_Code_Activity.this, "Prueba", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void EnvioSiguienteAct() {
        Intent intent = new Intent(Validacion_Code_Activity.this, BotonPanic_Activity.class);
        startActivity(intent); // Iniciar el siguiente Activity
        finish(); //
    }

    private void enviarDatosAlServidor(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "Muchas Gracias Por Registrarse !!!", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Validacion_Code_Activity.this, error.toString(), Toast.LENGTH_SHORT).show();
                Toast.makeText(Validacion_Code_Activity.this, "Se Ocasiono un error Inesperado, revise su Conexión !!!", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros=new HashMap<String, String>();
                parametros.put("co_verification",Code_EditText.getText().toString());
                return parametros;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}