package com.sistemas.ciudadnuevasegura.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.sistemas.ciudadnuevasegura.Fragments.ConsultaFragment;
import com.sistemas.ciudadnuevasegura.MainActivity;
import com.sistemas.ciudadnuevasegura.PruebaID_Activity;
import com.sistemas.ciudadnuevasegura.R;
import com.sistemas.ciudadnuevasegura.Registro.Registro_Activity;
import com.sistemas.ciudadnuevasegura.GPS.ValidarGPS_Activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Login_Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    TextView TXT_registrar;
    Toolbar toolbar;

    private EditText EDT_Cuenta1;
    private EditText EDT_Password1;
    Button buttonLogin;
    private String textToSend1;

    private TextView txtVisualizador;    // ver data exportada
    private EditText txtVisualizador1;  // TODO: 27/03/2024 se realizo cambio antes era TextView
    private RequestQueue rq;
    RequestQueue requestQueue;
    private SharedPreferences sharedPreferences;
    DrawerLayout mmDrawerLayout;
    ActionBarDrawerToggle mDDrawerToggle;

    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String FIRST_RUN = "firstRun";

    private static final int CHECK_GPS_INTERVAL = 3000; // Verificar cada 3 segundos
    private Handler handler;
    private LocationManager locationManager;

    private static final String PREF_NAME = "MisPreferencias";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.include_login);

        handler = new Handler();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //activity_login

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.parseColor("#0A708A")); //Color de Toolbar diferente
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF")); //Color de Texto Blanco
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);          // Flecha Retroceso
        getSupportActionBar().setTitle("Inicio de Sesión");

        mmDrawerLayout = findViewById(R.id.drawer_layoutt_trans);     // cambiar variable
        mDDrawerToggle = new ActionBarDrawerToggle(this, mmDrawerLayout,toolbar , R.string.open, R.string.close);

        mmDrawerLayout.addDrawerListener(mDDrawerToggle);
        mDDrawerToggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view111);
        navigationView.setNavigationItemSelectedListener(this);
        //fragments
        showFragments(new ConsultaFragment());

        EDT_Cuenta1 = findViewById(R.id.EDT_Cuenta1);
        EDT_Password1 = findViewById(R.id.EDT_Password1);

        /**
         * ========================================================================================
         *
         * ========================================================================================
         */
        txtVisualizador = findViewById(R.id.txtVisualizador);
        txtVisualizador1 = findViewById(R.id.txtVisualizador1);
        textToSend1 = txtVisualizador1.getText().toString();  // Variable para visualizar

        txtVisualizador1.setEnabled(false);

        /**
         * ========================================================================================
         *ESTABLECE LA INFORMACION DINAMICA EN EL EDTITTEXT  = txtVisualizador1
         * ========================================================================================
         */

        String informacionDinamica = obtenerInformacionDinamica();
        // Establecer el texto dinámico en el TextView
        txtVisualizador1.setText(informacionDinamica);

        /**
         * ========================================================================================
         *
         * ========================================================================================
         */


        // Agregar un TextWatcher al textView1 para detectar cambios de texto
        txtVisualizador.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No se necesita implementar
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Cuando el texto en textView1 cambia, actualiza textView2
                txtVisualizador1.setText(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No se necesita implementar
            }
        });


        /**
         * // 
         */
        // Recuperar las credenciales guardadas (si existen)
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String savedUsername = sharedPreferences.getString(KEY_USERNAME, "");
        String savedPassword = sharedPreferences.getString(KEY_PASSWORD, "");

        Metodo_EnviarData_activitys();

        // Mostrar las credenciales guardadas
        EDT_Cuenta1.setText(savedUsername);
        EDT_Password1.setText(savedPassword);

        Metod_Autenticacion_viewID();
       // Metodo_EnviarData_activitys();

        buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // ejecutarMetodosConRetraso();   =================================================

                // TODO: 25/03/2024 Funcional

                Metod_Autenticacion_viewID();           // Valida el ID de usuario - captura

                Metod_EnvioLogin_Date();                // Validar acceso Login

                MetodoPrueba_Edttxt();
                //Metodo_EnviarData_activitys();
                //Metodo_EnviarData_activitys();          // TODO: 25/03/2024  realizando desarrollo
            }
        });

        TXT_registrar = findViewById(R.id.TXT_registrar);
        TXT_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login_Activity.this, Registro_Activity.class);
                startActivity(intent);
                //finish();
            }
        });

        //String url = "https://ciudad-nueva-segura.000webhostapp.com/API/login.php";
    }

    private void MetodoPrueba_Edttxt() {
        // TODO: 27/03/2024
        // Obtener la información del EditText
        String infoToSend = txtVisualizador1.getText().toString();

        // Guardar la información en SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("my_shared_preferencess", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("info", infoToSend);
        editor.apply();

    }

    private String obtenerInformacionDinamica() {
        /**
         * esto no mover lo que hace es copiar la informacion del TextView = txtVisualizador1
         */
        // Por ejemplo, aquí podrías obtener la información de una base de datos, de un servicio web, etc.
        // En este ejemplo, simplemente retornaremos un string estático
        return "Información Dinámica";
    }

    /**
     * ============================================================================================
     * MODIFICACION // TODO: 27/03/2024
     * ============================================================================================
     */
    private void Metodo_EnviarData_activitys() {
        // Guardar el texto en SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("my_shared_preferencesss", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("text1", textToSend1);
        editor.apply();
        // TODO: 26/03/2024  revisar 
    }

    /**
     * ============================================================================================
     * MODIFICACION // TODO: 27/03/2024
     * ============================================================================================
     */
    private void Metod_Autenticacion_viewID() {
        // TODO: 25/03/2024 === Se incorpora Metodo Volley para revisar mediante el ID - Password - el ID de la DataBase para autenticar el boton de panico es decir que lo identifique
        Metodo_del_BotonID();
    }

    private void Metodo_del_BotonID() {
        String value1 = EDT_Cuenta1.getText().toString();
        String value2 = EDT_Password1.getText().toString();
        new SendRequestTask().execute(value1, value2);
    }

    private class SendRequestTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String value1 = params[0];
            String value2 = params[1];
            try {
                URL url = new URL(buildUrlString(value1, value2));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                conn.disconnect();
                return response.toString();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String response) {
            if (response != null) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("id")) {
                        String id = jsonObject.getString("id");
                        updateResponseTextView("" + id);     // ID:

                    } else {
                        showErrorMessage("Error: No se encontró 'id' en la respuesta JSON");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showErrorMessage("Error: No se pudo analizar la respuesta JSON");
                }
            } else {
                showErrorMessage("Error: No se recibió respuesta del servidor");
            }
        }
    }

    private String buildUrlString(String value1, String value2) {

        //return "http://192.168.18.31/Security_GPS/API/query_data.php?dni_usr=" + value1 + "&password=" + value2;

        return "https://devcraftinglab.com/securitygps/API/query_data.php?dni_usr=" + value1 + "&password=" + value2;

    }

    private void updateResponseTextView(String result) {
        txtVisualizador.setText(result);   // Elemento que visualiza la data de la BD
        // TODO: 25/03/2024
    }

    private void showErrorMessage(String message) {
        Toast.makeText(Login_Activity.this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * ============================================================================================
     * ============================================================================================
     */
    private void Metod_EnvioLogin_Date() {
        // TODO: 25/03/2024 aca colocaremos toda la informacion tanto para guardar la informacion del ID de Usuario y luego Autenticael usuario
        // Obtener las credenciales ingresadas
        String username = EDT_Cuenta1.getText().toString();
        String password = EDT_Password1.getText().toString();
        guardarCredenciales(username, password);

        // http://localhost/Security_GPS/API/login_android.php
        // https://devcraftinglab.com/securitygps/API/login_android.php

        Metodo_Boton_Login_Activity("https://devcraftinglab.com/securitygps/API/login_android.php");
        // Metodo_Boton_Login_Activity("http://192.168.18.31/Security_GPS/API/login_android.php");
    }

    /**
     * ============================================================================================
     * ============================================================================================
     */

    private void Metodo_Boton_Login_Activity(String URL) {

    // TODO: 22/03/2024 Funcional a nivel de Peticiones en Base de datos Local - lo pasaremos a produccion ---

        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()) {
                    Intent intent = new Intent(getApplicationContext(), ValidarGPS_Activity.class);
                    Toast.makeText(Login_Activity.this, "Bienvenido Al Sistema de Seguridad Ciudad Nueva!!", Toast.LENGTH_SHORT).show();
                    // Limpiar_TextView();
                    startActivity(intent);
                } else {
                    Toast.makeText(Login_Activity.this, "Usuario o contraseña incorrecta, vuelva a intentar", Toast.LENGTH_SHORT).show();
                    //  Limpiar_TextView();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Login_Activity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametross=new HashMap<String,String>();
                parametross.put("dni_usr",EDT_Cuenta1.getText().toString());        //validado
                parametross.put("password",EDT_Password1.getText().toString());     //validado
                return  parametross;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private void ejecutarMetodosConRetraso() {
        // Ejecuta el primer método inmediatamente directo desde la API
        //falta cambiar
        // http://localhost/Ciudad_Nueva/API/selct_name.php?usuarios_wenco=admin
        //http://localhost/Security_GPS/API/selct_name.php?name=
        BuscarUser_Representar("http://192.168.18.31/Security_GPS/API/selct_name.php?name" + EDT_Cuenta1.getText()); // esta mandando error
        // funciona de manera local la URL
        // BuscarUser_Representar("https://ciudad-nueva-segura.000webhostapp.com/API/selct_name.php?usuarios_wenco=" + EDT_Cuenta1.getText()); // esta mandando error


    }

    private void BuscarUser_Representar(String URL) {
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        // EDT_Cuenta1.setText(jsonObject.getString("name"));
                        txtVisualizador.setText(jsonObject.getString("name"));
                        //  CopiarInformacion();
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error de conexion", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue=Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }


    // ????????????????????????????????????????????????????????????????????????????????????????????????????????????

    private void guardarCredenciales(String username, String password) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Guardar las credenciales
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_PASSWORD, password);

        // Aplicar los cambios
        editor.apply();
    }

    //  String url = "http://192.168.0.10/Ciudad_Nueva/API/login.php";


    @Override
    protected void onResume() {
        super.onResume();
        startGpsCheck();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopGpsCheck();
    }

    private Runnable gpsCheckRunnable = new Runnable() {
        @Override
        public void run() {
            if (!isGpsEnabled()) {
                showGpsDialog();
            }
            handler.postDelayed(this, CHECK_GPS_INTERVAL);
        }
    };

    private void showGpsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("El GPS está desactivado. ¿Desea activarlo?")
                .setCancelable(false)
                .setPositiveButton("Activar GPS", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    private boolean isGpsEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void stopGpsCheck() {
        handler.removeCallbacks(gpsCheckRunnable);
    }

    private void startGpsCheck() {
        handler.post(gpsCheckRunnable);
    }

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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {   // para Drawer ACtivity
        mmDrawerLayout.closeDrawer(GravityCompat.START);

        int id = item.getItemId();
        if (id == R.id.nav_phone) {
            // OpcionPrimaria1(); //Metodo
            Toast.makeText(this, "Se realizara Llamada a Seguridad Nacional: ", Toast.LENGTH_SHORT).show();
            Metodo_CallPhone();
        } else if (id == R.id.nav_information) {
            Toast.makeText(Login_Activity.this, "Información de la App", Toast.LENGTH_LONG).show();
            //RutaTecnico();  //Metodo
        }
        return true;
    }

    private void Metodo_CallPhone() {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
