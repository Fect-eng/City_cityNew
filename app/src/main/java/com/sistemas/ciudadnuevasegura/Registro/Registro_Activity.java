package com.sistemas.ciudadnuevasegura.Registro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.sistemas.ciudadnuevasegura.Correo.Validacion_Code_Activity;
import com.sistemas.ciudadnuevasegura.Fragments.ConsultaFragment;
import com.sistemas.ciudadnuevasegura.GPS.BotonPanic_Activity;
import com.sistemas.ciudadnuevasegura.MainActivity;
import com.sistemas.ciudadnuevasegura.Politicas.Politicas_Activity;
import com.sistemas.ciudadnuevasegura.R;
import android.content.DialogInterface;
import java.util.HashMap;
import java.util.Map;

public class Registro_Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    Toolbar toolbar;
    Button Btn_RegistrarUsuario;
    TextView TXT_Terminos_Condiciones;
    DrawerLayout mmDrawerLayout;
    ActionBarDrawerToggle mDDrawerToggle;

    TextInputEditText Register_editText_Nombres;
    TextInputEditText Register_DNI_Pasaporte;
    TextInputEditText Register_editText_Telefono;
    TextInputEditText Register_direccion;
    TextInputEditText Register_editText_password;
    TextInputEditText Register_editText_Referencia;
    TextInputEditText Register_editText_CorreoE;
    private RequestQueue rq;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.include_register);   // activity_registro

        TXT_Terminos_Condiciones = findViewById(R.id.TXT_Terminos_Condiciones);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.parseColor("#0A708A")); //Color de Toolbar diferente
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF")); //Color de Texto Blanco
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);          // Flecha Retroceso
        getSupportActionBar().setTitle("Registro Usuario");

        mmDrawerLayout = findViewById(R.id.drawer_layoutt_trans);     // cambiar variable
        mDDrawerToggle = new ActionBarDrawerToggle(this, mmDrawerLayout,toolbar , R.string.open, R.string.close);

        mmDrawerLayout.addDrawerListener(mDDrawerToggle);
        mDDrawerToggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view111);
        navigationView.setNavigationItemSelectedListener(this);
        //fragments
        showFragments(new ConsultaFragment());

        /**
         * =========================================================================================
         * =========================================================================================
         */
        Register_editText_Nombres = findViewById(R.id.Register_editText_Nombres);
        Register_DNI_Pasaporte = findViewById(R.id.Register_DNI_Pasaporte);
        Register_editText_Telefono = findViewById(R.id.Register_editText_Telefono);
        Register_editText_password = findViewById(R.id.Register_editText_password);
        Register_direccion = findViewById(R.id.Register_direccion);
        Register_editText_Referencia = findViewById(R.id.Register_editText_Referencia);
        Register_editText_CorreoE = findViewById(R.id.Register_editText_CorreoE);
        /**
         * =========================================================================================
         * =========================================================================================
         */

        Btn_RegistrarUsuario = findViewById(R.id.Btn_RegistrarUsuario);
        Btn_RegistrarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MostrarDialog_Data();
            }
        });

        TXT_Terminos_Condiciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Registro_Activity.this, Politicas_Activity.class);
                startActivity(intent); // Iniciar el siguiente Activity
                finish(); // destruye actividad
            }
        });
    }

    private void MostrarDialog_Data() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Configurar el AlertDialog
        builder.setMessage("Esta de acuerdo en Registrarse?")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                        VolleyRegister_DataUSR("https://devcraftinglab.com/securitygps/API/registro.php");
                       // VolleyRegister_DataUSR("http://192.168.18.31/Security_GPS/API/registro.php");

                        //VolleyRegister_DataUSR("https://ciudad-nueva-segura.000webhostapp.com/API/registro.php");
                        //http://localhost/Security_GPS/API/registro.php
                       // Toast.makeText(Registro_Activity.this, "Ingreso Exitoso", Toast.LENGTH_SHORT).show();

                        //https://ciudad-nueva-segura.000webhostapp.com/API/registro.php
                        enviarAlPrincipio();
                        // Acciones a realizar al hacer clic en Aceptar
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Acciones a realizar al hacer clic en Cancelar
                        dialog.dismiss();
                    }
                });

        // Mostrar el AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void enviarAlPrincipio() {
        Intent intent = new Intent(Registro_Activity.this, MainActivity.class);
        startActivity(intent); // Iniciar el siguiente Activity
        finish();
    }

    /**
     * =========================================================================================
     * =========================================================================================
     */
    private void VolleyRegister_DataUSR(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Descomentar la línea siguiente si deseas mostrar un mensaje de éxito
                Toast.makeText(getApplicationContext(), "Se Ingreso Personal Nuevo...", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Mostrar detalles del error
                Toast.makeText(Registro_Activity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(Registro_Activity.this, "Se Ocasionó un error inesperado !!!", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("name", Register_editText_Nombres.getText().toString());
                parametros.put("dni_usr", Register_DNI_Pasaporte.getText().toString());
                parametros.put("phone", Register_editText_Telefono.getText().toString());
                parametros.put("address", Register_direccion.getText().toString());
                parametros.put("reference", Register_editText_Referencia.getText().toString());
                parametros.put("password", Register_editText_password.getText().toString());
                parametros.put("email", Register_editText_CorreoE.getText().toString());

                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    /**
     * =========================================================================================
     * =========================================================================================
     */
    private void showFragments(ConsultaFragment consultaFragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // ft.replace(R.id.frame_layout, ConsultaFragment);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        if (mmDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mmDrawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
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