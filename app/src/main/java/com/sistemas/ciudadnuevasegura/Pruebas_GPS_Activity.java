package com.sistemas.ciudadnuevasegura;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polygon;

import java.util.ArrayList;

public class Pruebas_GPS_Activity extends AppCompatActivity {

    private MapView mapView;
    private Polygon polygon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pruebas_gps);

        mapView = findViewById(R.id.mapView);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

        ArrayList<GeoPoint> polygonVertices = new ArrayList<>();
        polygonVertices.add(new GeoPoint(-17.980008, -70.244644));
        polygonVertices.add(new GeoPoint(-17.989560, -70.237820));
        polygonVertices.add(new GeoPoint(-17.978865, -70.223958));
        polygonVertices.add(new GeoPoint(-17.971191, -70.230224));
        polygonVertices.add(new GeoPoint(-17.980131, -70.244644));

        polygon = new Polygon();
        polygon.setFillColor(Color.argb(75, 255, 0, 0));
        polygon.setStrokeColor(Color.RED);
        polygon.setPoints(polygonVertices);
        mapView.getOverlayManager().add(polygon);

        mapView.getController().setCenter(new GeoPoint(17.979, -70.237));
        mapView.getController().setZoom(15);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDetach();
    }
}