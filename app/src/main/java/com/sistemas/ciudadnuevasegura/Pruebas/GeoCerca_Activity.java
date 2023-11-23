package com.sistemas.ciudadnuevasegura.Pruebas;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import com.sistemas.ciudadnuevasegura.R;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.IMyLocationConsumer;
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class GeoCerca_Activity extends AppCompatActivity implements IMyLocationConsumer {

    private MapView mapView;
    private Polygon geoPolygon;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private MyLocationNewOverlay locationOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_cerca);
        mapView = findViewById(R.id.mapview);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

        // Define los puntos que formarán el polígono
        GeoPoint point1 = new GeoPoint(40.0, -74.0);
        GeoPoint point2 = new GeoPoint(41.0, -75.0);
        GeoPoint point3 = new GeoPoint(41.0, -73.0);

        List<GeoPoint> geoPoints = new ArrayList<>();
        geoPoints.add(point1);
        geoPoints.add(point2);
        geoPoints.add(point3);

        geoPolygon = new Polygon();
        geoPolygon.setPoints(geoPoints);
        geoPolygon.setFillColor(Color.argb(75, 255, 0, 0));
        geoPolygon.setStrokeColor(Color.rgb(255, 0, 0));
        geoPolygon.setStrokeWidth(2);

        mapView.getOverlays().add(geoPolygon);

        // Centra el mapa en el polígono
        mapView.zoomToBoundingBox(geoPolygon.getBounds(), true);

        // Inicializa el overlay de ubicación
        locationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), mapView);
        locationOverlay.enableMyLocation();
        locationOverlay.enableFollowLocation();
        mapView.getOverlays().add(locationOverlay);

        // Solicitar permiso de ubicación
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso de ubicación concedido, mostrar la ubicación actual
                mapView.getController().setCenter(locationOverlay.getMyLocation());
                mapView.getController().setZoom(18); // Establece el nivel de zoom que desees

                // Obtener información de la ciudad
                String cityName = getCityName(locationOverlay.getMyLocation());
                // Haz lo que desees con el nombre de la ciudad (por ejemplo, mostrarlo en una vista).
            }
        }
    }

    private String getCityName(GeoPoint location) {
        String cityName = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (!addresses.isEmpty()) {
                cityName = addresses.get(0).getLocality();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cityName;
    }

    @Override
    public void onLocationChanged(@NonNull Location location, IMyLocationProvider source) {
        // Este método se llama cuando la ubicación del usuario cambia en tiempo real
        if (location != null) {
            GeoPoint userLocation = new GeoPoint(location.getLatitude(), location.getLongitude());
            // Puedes hacer lo que desees con la ubicación del usuario en tiempo real
        }
    }
}