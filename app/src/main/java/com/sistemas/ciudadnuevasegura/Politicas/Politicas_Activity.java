package com.sistemas.ciudadnuevasegura.Politicas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.sistemas.ciudadnuevasegura.Fragments.ConsultaFragment;
import com.sistemas.ciudadnuevasegura.R;

public class Politicas_Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    Toolbar toolbar;
    DrawerLayout mmDrawerLayout;
    ActionBarDrawerToggle mDDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.include_politicas);  //activity_politicas

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.parseColor("#0A708A")); //Color de Toolbar diferente
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF")); //Color de Texto Blanco
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);          // Flecha Retroceso
        getSupportActionBar().setTitle("Mesa de Partes - Virtual");

        mmDrawerLayout = findViewById(R.id.drawer_layoutt_trans);     // cambiar variable
        mDDrawerToggle = new ActionBarDrawerToggle(this, mmDrawerLayout,toolbar , R.string.open, R.string.close);

        mmDrawerLayout.addDrawerListener(mDDrawerToggle);
        mDDrawerToggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view111);
        navigationView.setNavigationItemSelectedListener(this);
        //fragments
        showFragments(new ConsultaFragment());

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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}