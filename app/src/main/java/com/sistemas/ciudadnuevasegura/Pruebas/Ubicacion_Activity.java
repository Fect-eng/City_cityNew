package com.sistemas.ciudadnuevasegura.Pruebas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

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
import com.sistemas.ciudadnuevasegura.R;

public class Ubicacion_Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

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

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubicacion);

        EditTextLatitud = findViewById(R.id.EditTextLatitud);
        EditTextLongitud = findViewById(R.id.EditTextLongitud);

        handler = new Handler();

        /**
         * Inicio COdigo GPS
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
        /**
         *  FInal Codigo GPS
         */

    }

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
                                resolvable.startResolutionForResult(Ubicacion_Activity.this, REQUEST_CHECK_GPS_SETTINGS);
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

    private Runnable gpsCheckRunnable = new Runnable() {
        @Override
        public void run() {
            checkAndRequestLocationPermission();
            gpsCheckHandler.postDelayed(this, GPS_CHECK_INTERVAL);
        }
    };

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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}