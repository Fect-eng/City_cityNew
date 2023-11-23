package com.sistemas.ciudadnuevasegura;

import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import org.osmdroid.config.Configuration;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;

public class Delimitacion_gps_Activity extends AppCompatActivity {

    private MapView mapView;
    private MyLocationNewOverlay myLocationOverlay;
    private ArrayList<GeoPoint> polygonVertices;
    ImageView ImageBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delimitacion_gps);

        // Inicializa osmdroid
        Configuration.getInstance().load(this, getSharedPreferences("osmdroid", MODE_PRIVATE));

        // Inicializa el MapView
        mapView = findViewById(R.id.mapView);
        mapView.setMultiTouchControls(true);

        // Define los vértices del polígono que representa la región
        polygonVertices = new ArrayList<>();
        /**
         *   GeoPoint point1 = new GeoPoint(-17.986669, -70.253245);
         *         GeoPoint point2 = new GeoPoint(-18.000200, -70.253352);
         *         GeoPoint point3 = new GeoPoint(-18.000894, -70.226745);
         *         GeoPoint point4 = new GeoPoint(-17.985547, -70.227646);
         *         GeoPoint point5 = new GeoPoint(-17.984404, -70.253095);
         */
                    // Lugar donde vives Tacna --- Luther King 1046
        /**
         *
         */
                    GeoPoint point1 = new GeoPoint(-17.980008, -70.244644);
                    GeoPoint point2 = new GeoPoint(-17.989560, -70.237820);
                    GeoPoint point3 = new GeoPoint(-17.978865, -70.223958);
                    GeoPoint point4 = new GeoPoint(-17.971191, -70.230224);
                    GeoPoint point5 = new GeoPoint(-17.980131, -70.244644);

        polygonVertices.add(point1);
        polygonVertices.add(point2);
        polygonVertices.add(point3);
        polygonVertices.add(point4);
        polygonVertices.add(point5);

        // Agrega un polígono que representa la región
        Polygon regionPolygon = new Polygon(mapView);
        regionPolygon.setFillColor(0x300000FF); // Relleno azul transparente
        regionPolygon.setStrokeColor(0xFF0000FF); // Borde azul
        regionPolygon.setStrokeWidth(2);
        regionPolygon.setPoints(polygonVertices);
        mapView.getOverlayManager().add(regionPolygon);

        // Establece el centro del mapa en la región
        mapView.getController().setCenter(polygonVertices.get(0));
        mapView.getController().setZoom(13); // Ajusta el nivel de zoom según tus necesidades

        // Configura la capa de ubicación actual
        myLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), mapView);
        myLocationOverlay.enableMyLocation();
        myLocationOverlay.runOnFirstFix(() -> {
            runOnUiThread(() -> {
                mapView.getController().animateTo(myLocationOverlay.getMyLocation());

                // Verificar si la ubicación del usuario está dentro del polígono
                if (isLocationInsideRegion()) {
                    showToast("Dentro del polígono");
                } else {
                    showToast("Fuera del polígono");
                    // Redirige a otro activity

                    // Redirige a otro activity de manera instantánea
                  //  startActivity(new Intent(Delimitacion_gps_Activity.this, MainActivity.class));
                   // finish(); // Cierra esta actividad si es necesario
                }
            });
        });
        mapView.getOverlayManager().add(myLocationOverlay);

        ImageBTN = findViewById(R.id.ImageBTN);
        ImageBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Metodo_de_Imagen();
            }
        });
    }

    private void Metodo_de_Imagen() {
        Toast.makeText(this, "Sin Conexion a la DataBase, revise", Toast.LENGTH_SHORT).show();
    }

    /**
     * // TODO: 12/11/2023
     * @return
     */
    private boolean isLocationInsideRegion() {
        // Implementa la lógica para verificar si la ubicación del usuario está dentro del polígono
        // Puedes utilizar la misma lógica que mencioné anteriormente

        // Ejemplo de implementación (sustitúyelo con la lógica real)
        if (myLocationOverlay != null) {
            GeoPoint userLocation = myLocationOverlay.getMyLocation();
            if (userLocation != null) {
                return isPointInsidePolygon(userLocation, polygonVertices);
            }
        }

        return false;
    }

    private boolean isPointInsidePolygon(GeoPoint point, ArrayList<GeoPoint> polygonVertices) {
        // Implementa la lógica para verificar si un punto está dentro del polígono
        // Puedes usar la fórmula de punto dentro de un polígono

        // Ejemplo de implementación (sustitúyelo con la lógica real)
        int intersectCount = 0;
        double x1, x2, y1, y2;

        for (int i = 0; i < polygonVertices.size() - 1; i++) {
            x1 = polygonVertices.get(i).getLongitude();
            y1 = polygonVertices.get(i).getLatitude();
            x2 = polygonVertices.get(i + 1).getLongitude();
            y2 = polygonVertices.get(i + 1).getLatitude();

            if ((y1 > point.getLatitude()) != (y2 > point.getLatitude()) &&
                    (point.getLongitude() < (x2 - x1) * (point.getLatitude() - y1) / (y2 - y1) + x1)) {
                intersectCount++;
            }
        }

        return intersectCount % 2 != 0;
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}