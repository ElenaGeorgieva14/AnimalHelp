package com.example.animalhelp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class LocationEditor extends AppCompatActivity {
    protected EditText latitude, longitude;
    protected Button changeLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_editor);
        latitude = (EditText) findViewById(R.id.latitude);
        longitude = (EditText) findViewById(R.id.longitude);
        changeLocation = (Button) findViewById(R.id.changeLocation);

        changeLocation.setOnClickListener((v) ->{
            Double d_latitude = Double.parseDouble(latitude.getText().toString());
            Double d_longitude = Double.parseDouble(longitude.getText().toString());
            MapsActivity.d_longitude = d_longitude;
            MapsActivity.d_latitude = d_latitude;
            Intent i = new Intent(LocationEditor.this, MapsActivity.class);
            startActivity(i);
        });
    }
}