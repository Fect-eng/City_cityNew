package com.sistemas.ciudadnuevasegura.Pruebas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.sistemas.ciudadnuevasegura.GPS.NosePuede_Activity;
import com.sistemas.ciudadnuevasegura.R;
import com.sistemas.ciudadnuevasegura.Registro.Registro_Activity;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polygon;

import java.util.ArrayList;

public class Validate_GPS_Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final GeoPoint[] POLYGON_COORDS = {
            new GeoPoint(-17.980008, -70.244644),
            new GeoPoint(-17.989560, -70.237820),
            new GeoPoint(-17.978865, -70.223958),
            new GeoPoint(-17.971191, -70.230224),
            new GeoPoint(-17.980131, -70.244644)

    };
    private Polygon polygon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate_gps);

        MapView mapView = findViewById(R.id.mapView);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

        // Crear el polígono con los vértices especificados
        polygon = crearPoligono();

        MetodoCalibrar();
        
    }

    private void MetodoCalibrar() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Verificar si el GPS está activado
        if (locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // Obtener la ubicación actual (este es solo un ejemplo, deberías implementar tu propia lógica para obtener la ubicación actual)
            Location ubicacionActual = obtenerUbicacionActual();

            // Verificar si la ubicación está dentro del polígono
            if (ubicacionActual != null && puntoEstaDentroDelPoligono(new GeoPoint(ubicacionActual.getLatitude(), ubicacionActual.getLongitude()))) {
                // Si la ubicación está dentro del polígono, esperar 1 segundo y luego iniciar Plugin_Activity
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Iniciar Plugin_Activity aquí
                        Intent intent = new Intent(Validate_GPS_Activity.this, Registro_Activity.class);
                        startActivity(intent);
                        finish(); // Opcional, dependiendo de tu flujo de la aplicación
                    }
                }, 1000); // 1000 milisegundos = 1 segundo
            } else {
                // La ubicación no está dentro del polígono, esperar 1 segundo y luego iniciar Final_Activity
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Iniciar Final_Activity aquí
                        Intent intent = new Intent(Validate_GPS_Activity.this, NosePuede_Activity.class);
                        startActivity(intent);
                        finish(); // Opcional, dependiendo de tu flujo de la aplicación
                    }
                }, 1000); // 1000 milisegundos = 1 segundo
            }
        } else {
            // El GPS no está activado, puedes manejar esto según tus necesidades
            // Por ejemplo, mostrar un mensaje de alerta o solicitar al usuario que active el GPS
        }
    }

    private Polygon crearPoligono() {
        ArrayList<GeoPoint> geoPoints = new ArrayList<>();
        for (GeoPoint coord : POLYGON_COORDS) {
            geoPoints.add(coord);
        }

        Polygon polygon = new Polygon();
        polygon.setPoints(geoPoints);
        return polygon;
    }

    private boolean puntoEstaDentroDelPoligono(GeoPoint punto) {
        int intersectCount = 0;
        ArrayList<GeoPoint> puntosPoligono = (ArrayList<GeoPoint>) polygon.getPoints();

        for (int i = 0; i < puntosPoligono.size() - 1; i++) {
            GeoPoint p1 = puntosPoligono.get(i);
            GeoPoint p2 = puntosPoligono.get(i + 1);

            if (interseccionRayo(punto, p1, p2)) {
                intersectCount++;
            }
        }

        return (intersectCount % 2) == 1;
    }

    private boolean interseccionRayo(GeoPoint punto, GeoPoint p1, GeoPoint p2) {
        double puntoLat = punto.getLatitude();
        double puntoLon = punto.getLongitude();
        double p1Lat = p1.getLatitude();
        double p1Lon = p1.getLongitude();
        double p2Lat = p2.getLatitude();
        double p2Lon = p2.getLongitude();

        if ((p1Lon > puntoLon) == (p2Lon > puntoLon)
                || puntoLat > Math.max(p1Lat, p2Lat)
                || puntoLat < Math.min(p1Lat, p2Lat)) {
            return false;
        }

        double m = (p2Lat - p1Lat) / (p2Lon - p1Lon);
        double x = (puntoLon - p1Lon) / m + p1Lat;

        return x > puntoLat;
    }

    private Location obtenerUbicacionActual() {
        // Aquí deberías implementar la lógica para obtener la ubicación actual, por ejemplo, utilizando la API de ubicación de Android
        // Este es solo un ejemplo, no es funcional
        return null;
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