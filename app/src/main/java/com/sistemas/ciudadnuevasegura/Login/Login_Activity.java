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
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
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
import com.sistemas.ciudadnuevasegura.R;
import com.sistemas.ciudadnuevasegura.Registro.Registro_Activity;
import com.sistemas.ciudadnuevasegura.GPS.ValidarGPS_Activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login_Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    Button buttonLogin;
    TextView TXT_registrar;
    Toolbar toolbar;
    private EditText EDT_Cuenta1;
    private EditText EDT_Password1;
    TextView txtVisualizador;
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
         * // TODO: 14/11/2023
         */
        // Recuperar las credenciales guardadas (si existen)
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String savedUsername = sharedPreferences.getString(KEY_USERNAME, "");
        String savedPassword = sharedPreferences.getString(KEY_PASSWORD, "");

        // Mostrar las credenciales guardadas
        EDT_Cuenta1.setText(savedUsername);
        EDT_Password1.setText(savedPassword);

        buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ejecutarMetodosConRetraso();
                // Obtener las credenciales ingresadas
                String username = EDT_Cuenta1.getText().toString();
                String password = EDT_Password1.getText().toString();

                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
                    // Guardar las credenciales en SharedPreferences
                    guardarCredenciales(username, password);
                    Metodo_Login(); // TODO: 4/11/2023
                   // mostrarMensaje("Inicio de sesión exitoso");
                } else {
                    mostrarMensaje("Por favor, ingrese las credenciales");
                }

               // Metodo_Login(); // TODO: 4/11/2023
               // finish();
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


        txtVisualizador = findViewById(R.id.txtVisualizador);

        //String url = "https://ciudad-nueva-segura.000webhostapp.com/API/login.php";
    }

    private void ejecutarMetodosConRetraso() {
        // Ejecuta el primer método inmediatamente directo desde la API
        //falta cambiar
        // http://localhost/Ciudad_Nueva/API/selct_name.php?usuarios_wenco=admin
        BuscarUser_Representar("https://ciudad-nueva-segura.000webhostapp.com/API/selct_name.php?usuarios_wenco=" + EDT_Cuenta1.getText());


    }


    private void enviarData_next() {
    }

    private void BuscarUser_Representar(String URL) {
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        EDT_Cuenta1.setText(jsonObject.getString("usuarios_wenco"));
                        txtVisualizador.setText(jsonObject.getString("telefono"));
                      //  CopiarInformacion();
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error de conexionnnnn", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue=Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
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

    //  String url = "http://192.168.0.10/Ciudad_Nueva/API/login.php";
    private void Metodo_Login() {
        /**
         * Colocar metodo de login con volley
         */
        String usuarios_wenco = EDT_Cuenta1.getText().toString();
        String password_wenco = EDT_Password1.getText().toString();

        String url = "https://ciudad-nueva-segura.000webhostapp.com/API/login.php";

        // Crear la solicitud POST con Volley
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        interpretarRespuesta(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mostrarMensaje("Error en la conexión al servidor.");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Parámetros para la solicitud POST
                Map<String, String> params = new HashMap<>();
                params.put("usuarios_wenco", usuarios_wenco);
                params.put("password_wenco", password_wenco);
                return params;
            }
        };

        // Agregar la solicitud a la cola de solicitudes de Volley
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    } // FIn de metodo Login

    private void interpretarRespuesta(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean success = jsonObject.optBoolean("success", false);
            String message = jsonObject.optString("message", "Respuesta inválida del servidor.");

            if (success) {
                // Inicio de sesión exitoso
                mostrarMensaje("Inicio de sesión exitoso.");
                // Redirigir a SecondActivity

                Intent intent = new Intent(Login_Activity.this, ValidarGPS_Activity.class);
                startActivity(intent);
                finish(); // Opcional: Finalizar MainActivity para que no pueda volver atrás con el botón "Atrás"
            } else {
                // Inicio de sesión fallido
                mostrarMensaje("Inicio de sesión fallido. " + message);
                // Aquí puedes realizar alguna acción después de un inicio de sesión fallido
            }
        } catch (JSONException e) {
            e.printStackTrace();
            mostrarMensaje("Error en el formato de la respuesta del servidor.");
        }
    }

    private void mostrarMensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    /**
     *
     */

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