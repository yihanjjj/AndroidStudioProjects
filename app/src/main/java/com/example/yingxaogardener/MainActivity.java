package com.example.yingxaogardener;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private Button logout;

    private Button add;
    private TextView humidity;
    private TextView moisture;
    private TextView sunlight;
    private TextView temperature;
    private TextView humidity_s;
    private TextView moisture_s;
    private TextView sunlight_s;
    private TextView temperature_s;
    private TextView plantType;

    private String plantT;
    //val plantList = resources.getStringArray(R.array.plantList);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logout = findViewById(R.id.logoutButton);
        AutoCompleteTextView edit = findViewById(R.id.PlantType);
        add = findViewById(R.id.plantTypeButton);
        humidity = findViewById(R.id.humidity);
        moisture = findViewById(R.id.moisture);
        sunlight = findViewById(R.id.sunlight);
        temperature = findViewById(R.id.temperature);
        humidity_s = findViewById(R.id.humidity_s);
        moisture_s = findViewById(R.id.moisture_s);
        sunlight_s = findViewById(R.id.sunlight_s);
        temperature_s = findViewById(R.id.temperature_s);
        plantType = findViewById(R.id.plantTypeDisplay);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.plantList));
        edit.setAdapter(adapter);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(MainActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, StartActivity.class));
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_name = edit.getText().toString();
                if (txt_name.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter a plant type", Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseDatabase.getInstance().getReference().child("automated-gardener-default-rtdb").child("realtime-plant-value").child("user1").child("plantType").setValue(txt_name);
                    Toast.makeText(MainActivity.this, "Submitted", Toast.LENGTH_SHORT).show();
                }
            }
        });

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("automated-gardener-default-rtdb");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                plantT = snapshot.child("realtime-plant-value").child("user1").child("plantType").getValue().toString();
                plantType.setText(snapshot.child("realtime-plant-value").child("user1").child("plantType").getValue().toString());
                humidity.setText(snapshot.child("realtime-plant-value").child("user1").child("humidity").getValue().toString());
                moisture.setText(snapshot.child("realtime-plant-value").child("user1").child("moisture_level").getValue().toString());
                sunlight.setText(snapshot.child("realtime-plant-value").child("user1").child("uv_index").getValue().toString());
                temperature.setText(snapshot.child("realtime-plant-value").child("user1").child("temperature_in_Celsius").getValue().toString());
                humidity_s.setText(snapshot.child("plantInfo").child(plantT).child("humidity_s").getValue().toString());
                moisture_s.setText(snapshot.child("plantInfo").child(plantT).child("moisture_s").getValue().toString());
                sunlight_s.setText(snapshot.child("plantInfo").child(plantT).child("sunlight_s").getValue().toString());
                temperature_s.setText(snapshot.child("plantInfo").child(plantT).child("temperatureC_s").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}