package com.sistemas.ciudadnuevasegura;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PruebaID_Activity extends AppCompatActivity {

    EditText editText1, editText2;
    Button button;
    TextView responseTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba_id);

        editText1 = findViewById(R.id.editText1);                   // id
        editText2 = findViewById(R.id.editText2);                   // contraseña
        button = findViewById(R.id.button);                         // boton accion
        responseTextView = findViewById(R.id.responseTextView);     // txtVisualizador

        // ========================================================================================
        // ========================================================================================
        // ========================================================================================
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Metodo_del_Boton();
            }
        });
    }

    private void Metodo_del_Boton() {
        String value1 = editText1.getText().toString();
        String value2 = editText2.getText().toString();
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
        return "http://192.168.18.31/Security_GPS/API/query_data.php?dni_usr=" + value1 + "&password=" + value2;
    }

    private void updateResponseTextView(String result) {
        responseTextView.setText(result);
    }

    private void showErrorMessage(String message) {
        Toast.makeText(PruebaID_Activity.this, message, Toast.LENGTH_SHORT).show();
    }

    // ========================================================================================
    // ========================================================================================
    // ========================================================================================
}