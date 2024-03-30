package com.sistemas.ciudadnuevasegura.GPS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.sistemas.ciudadnuevasegura.Fragments.ConsultaFragment;
import com.sistemas.ciudadnuevasegura.Login.Login_Activity;
import com.sistemas.ciudadnuevasegura.Pruebas.Llamada_Activity;
import com.sistemas.ciudadnuevasegura.Pruebas.Ubicacion_Activity;
import com.sistemas.ciudadnuevasegura.R;

import java.util.HashMap;
import java.util.Map;

public class BotonPanic_Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int REQUEST_CHECK_GPS_SETTINGS = 2;
    private static final long GPS_CHECK_INTERVAL = 10000; // 10 segundos

    private boolean gpsEnabled = false;
    private AlertDialog enableGpsDialog; // Agregar esta línea
    private boolean permissionsRequested = false;
    private Handler gpsCheckHandler;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private boolean isRunning = false;
    private Handler handler;
    EditText EditTextLatitud;
    EditText EditTextLongitud;

    EditText EditText_NumberCritico;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    ImageView ImageBTN;
    Toolbar toolbar;

    private RequestQueue rq;
    RequestQueue requestQueue;
    private SharedPreferences sharedPreferences;

    DrawerLayout mmDrawerLayout;
    ActionBarDrawerToggle mDDrawerToggle;
    private static final int REQUEST_CALL_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.include_emergencia);     // activity_boton_panic

        // Establecer el texto recibido en el TextView

        //MetodoRecuperar();

        /**
         * ========================================================================================
         * CABECERA
         * ========================================================================================
         */

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.parseColor("#0A708A")); //Color de Toolbar diferente
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF")); //Color de Texto Blanco
        getSupportActionBar().setTitle("Ubicacion Emergenciaaa");

        /**
         * ========================================================================================
         * FIN DE CABECERA
         * ========================================================================================
         */

        EditTextLatitud = findViewById(R.id.EditTextLatitud);
        EditTextLongitud = findViewById(R.id.EditTextLongitud);


        /**
         * ========================================================================================
         * // Recuperar el texto de SharedPreferences
         *         SharedPreferences sharedPreferences = getSharedPreferences("my_shared_preferencesss", Context.MODE_PRIVATE);
         *         String receivedText = sharedPreferences.getString("text1", "");
         *
         *         EditText_NumberCritico.setText(receivedText);
         * ========================================================================================
         */
        EditText_NumberCritico = findViewById(R.id.EditText_NumberCritico);

        // Obtener la información de SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("my_shared_preferencess", Context.MODE_PRIVATE);
        String receivedInfo = sharedPreferences.getString("info", "");

        // Establecer la información recibida en el EditText
        EditText_NumberCritico.setText(receivedInfo);

        /**
         * ========================================================================================
         * ========================================================================================
         */

        handler = new Handler();
        gpsCheckHandler = new Handler();
        gpsCheckHandler.postDelayed(gpsCheckRunnable, GPS_CHECK_INTERVAL);

        mmDrawerLayout = findViewById(R.id.drawer_layoutt_transs);     // cambiar variable
        mDDrawerToggle = new ActionBarDrawerToggle(this, mmDrawerLayout,toolbar , R.string.open, R.string.close);

        mmDrawerLayout.addDrawerListener(mDDrawerToggle);
        mDDrawerToggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view1111);
        navigationView.setNavigationItemSelectedListener(this);
        //fragments
        showFragments(new ConsultaFragment());

        ImageBTN = findViewById(R.id.ImageBTN);
        ImageBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Metodo_Agregar("https://ciudad-nueva-segura.000webhostapp.com/API/data_in.php");     // falta revisar

                // https://devcraftinglab.com/securitygps/API/coordenadas.php
                Metodo_Agregar("https://devcraftinglab.com/securitygps/API/coordenadas.php");
               // Metodo_Agregar("http://192.168.18.31/Security_GPS/API/coordenadas.php");
                Toast.makeText(BotonPanic_Activity.this, "Auxilio Enviado, le ayudaremos", Toast.LENGTH_SHORT).show();
            }
        });

        /**
         * ==============================================
         * ==============================================
         */

        // Inicializar el cliente de ubicación fusionada
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Crear solicitud de ubicación

        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000); // Actualizar la ubicación cada segundo
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Crear el callback de ubicación
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Obtener la latitud y longitud actual
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    // Actualizar los TextViews con la latitud y longitud
                    EditTextLatitud.setText(String.valueOf(latitude));
                    EditTextLongitud.setText(String.valueOf(longitude));
                }
            }
        };

        checkAndRequestLocationPermission();
        //checkLoginButtonState();

        // ========================================================================


        /**
         *  FInal Codigo GPS - Final OnCreate
         */
    }


    private Runnable gpsCheckRunnable = new Runnable() {
        @Override
        public void run() {
            checkGpsStatus(); // Función que verificará el estado del GPS
            gpsCheckHandler.postDelayed(this, GPS_CHECK_INTERVAL);
        }
    };

    private void checkGpsStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // El GPS está activado, no es necesario mostrar un mensaje
        } else {
            // El GPS está desactivado, muestra un mensaje para activarlo
            showEnableGPSDialog();
        }
    }

    private void checkLoginButtonState() {
        /**
         *  if (!button_Estados.isEnabled()) {
         *             button_Estados.setEnabled(true);
         *         }
         */
    }

    /**
     * Enviar data a BaseDatos
     * =========================================
     * @param URL
     */
    private void Metodo_Agregar(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               // Toast.makeText(getApplicationContext(), "Se Ingreso Personal Nuevo...", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //se agrego
                Toast.makeText(BotonPanic_Activity.this, error.toString(), Toast.LENGTH_SHORT).show();
                Toast.makeText(BotonPanic_Activity.this, "Se Ocasiono un error Inesperado !!!", Toast.LENGTH_SHORT).show();
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // TODO: 12/03/2024 revisar este codigo ya que no envia me parece que hay que integrar el id_user
                Map<String, String> parametros=new HashMap<String, String>();
               // parametros.put("usu_nombre",txt_BD_Nombres.getText().toString());
                parametros.put("latitud",EditTextLatitud.getText().toString());
                parametros.put("longitud",EditTextLongitud.getText().toString());
                parametros.put("id_user",EditText_NumberCritico.getText().toString());
                //parametros.put("critico_n", EditText_NumberCritico.getText().toString());
                return parametros;
                //hay que modificar para que enlace a la tabla usuario
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
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
            Toast.makeText(BotonPanic_Activity.this, "Información de la App", Toast.LENGTH_LONG).show();
            //RutaTecnico();  //Metodo
            Metodo_InformacionApp();
        }
        return true;
    }

    private void Metodo_InformacionApp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Inflar el layout personalizado
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.modal_img, null);
        builder.setView(view);

        // Configurar el botón de cierre
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        // Mostrar el AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void Metodo_CallPhone() {
        if (ContextCompat.checkSelfPermission(BotonPanic_Activity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            realizarLlamada();
        } else {
            // Si no tienes permiso, solicita permiso al usuario.
            ActivityCompat.requestPermissions(BotonPanic_Activity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
        }
    }

    private void realizarLlamada() {
        String phoneNumber = "080013926"; // Reemplaza con el número de teléfono al que quieres llamar.
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));

        try {
            startActivity(intent);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void startLocationUpdates() {
        // Verificar permisos de ubicación
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        // Iniciar las actualizaciones de ubicación
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    private void stopLocationUpdates() {
        // Detener las actualizaciones de ubicación
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @androidx.annotation.NonNull String[] permissions, @androidx.annotation.NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            } else {
                Toast.makeText(this, "Se requieren permisos de ubicación para mostrar la ubicación en tiempo real", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void checkAndRequestLocationPermission() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // El GPS está activado
            gpsEnabled = true;
            showGpsActivatedMessage();
            // enableLoginButton();
        } else {
            // El GPS está desactivado, mostrar diálogo para solicitar activación
            showEnableGPSDialog();
        }

        if (!permissionsRequested) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // Permiso no concedido, solicitar permiso
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_ACCESS_FINE_LOCATION);
                permissionsRequested = true;
            } else {
                // Permiso concedido, realizar las acciones relacionadas con la ubicación
                startLocation();
            }
        }
    }

    private void startLocation() {
        if (gpsEnabled) {
            // Realizar acciones relacionadas con la ubicación aquí
        }
    }

    private void showEnableGPSDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Activar GPS");
        builder.setMessage("Para utilizar esta aplicación, debes activar el GPS.");
        builder.setPositiveButton("Activar GPS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestGpsActivation();
                dialog.dismiss(); // Cerrar el diálogo
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showEnableGPSDialog(); // Vuelve a mostrar el diálogo si se cancela
            }
        });
        builder.setCancelable(false);
        enableGpsDialog = builder.show(); // Asignar la instancia del diálogo a la variable enableGpsDialog
    }

    private void requestGpsActivation() {
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());
        task.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    // El GPS está activado
                    gpsEnabled = true;
                    showGpsActivatedMessage();
                    // enableLoginButton();
                } catch (ApiException exception) {
                    switch (exception.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                // El GPS está desactivado, pero se puede solucionar mostrando el diálogo de activación
                                ResolvableApiException resolvable = (ResolvableApiException) exception;
                                resolvable.startResolutionForResult(BotonPanic_Activity.this, REQUEST_CHECK_GPS_SETTINGS);
                                showGpsActivationDialog();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // El dispositivo no admite cambios en la configuración del GPS
                            break;
                    }
                }
            }
        });
    }

    private void showGpsActivationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Activar GPS");
        builder.setMessage("El GPS está desactivado. ¿Deseas activarlo?");
        builder.setPositiveButton("Activar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestGpsActivation();
                dialog.dismiss(); // Cerrar el diálogo
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showEnableGPSDialog(); // Vuelve a mostrar el diálogo de activación si se cancela
            }
        });
        builder.setCancelable(false);
        enableGpsDialog = builder.show(); // Asignar la instancia del diálogo a la variable enableGpsDialog
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (gpsCheckHandler != null) {
            gpsCheckHandler.removeCallbacks(gpsCheckRunnable);
        }

        isRunning = false;

        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    private void showGpsActivatedMessage() {
        Toast.makeText(this, "GPS Activado", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}