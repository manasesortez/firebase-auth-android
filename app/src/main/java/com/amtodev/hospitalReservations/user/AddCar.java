package com.amtodev.hospitalReservations.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.amtodev.hospitalReservations.Login;
import com.amtodev.hospitalReservations.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class AddCar extends AppCompatActivity {

    ImageView next_activity;
    EditText txtModelCar, txtMarkCar, txtYearCar;
    Button btnSaveCar;

    private FirebaseAuth objFirebase;
    private FirebaseAuth.AuthStateListener objFirebaseListener;

    //database
    FirebaseDatabase objDataBase;
    DatabaseReference dbReference;

    FirebaseUser objUsuario;
    private ProgressDialog objDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        txtMarkCar = findViewById(R.id.txtMarkCar);
        txtModelCar = findViewById(R.id.txtCarModel);
        txtYearCar = findViewById(R.id.txtYearCar);

        btnSaveCar = findViewById(R.id.btnSaveCar);

        objDialog = new ProgressDialog(this);

        next_activity = findViewById(R.id.next_activity);
        next_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMain();
            }
        });

        btnSaveCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarVehiculo();
            }
        });

        objFirebase = FirebaseAuth.getInstance();

        objFirebaseListener = new FirebaseAuth.AuthStateListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                objUsuario = firebaseAuth.getCurrentUser();
                if (objUsuario == null){
                    logoutUser();
                }
            }
        };

        FirebaseApp.initializeApp(AddCar.this);
        objDataBase = FirebaseDatabase.getInstance("https://agilereservations-default-rtdb.firebaseio.com/");
        dbReference = objDataBase.getReference();

    }

    private void registrarVehiculo() {
        objDialog.setMessage("Registrando Datos...");
        objDialog.show();
        Adaptador objVehiculo = new Adaptador();
        objVehiculo.setId_vehiculo(UUID.randomUUID().toString());
        objVehiculo.setModelo(txtModelCar.getText().toString());
        objVehiculo.setMarca(txtMarkCar.getText().toString());
        objVehiculo.setAnio(txtYearCar.getText().toString());
        objVehiculo.setUsuario(objUsuario.getEmail());
        dbReference.child("tblVehiculos").child(objVehiculo.getId_vehiculo()).setValue(objVehiculo);
        objDialog.dismiss();
        Toast.makeText(AddCar.this, "Datos Registrados correctament", Toast.LENGTH_SHORT).show();
        this.finish();
    }

    public void openMain(){
        startActivity(new Intent(getApplicationContext(), UserMain.class));
        finish();
    }

    public void logoutUser(){
       Intent objVentana = new Intent(AddCar.this, UserMain.class);
       startActivity(objVentana);
       this.finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        objFirebase.addAuthStateListener(objFirebaseListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (objFirebaseListener != null){
            objFirebase.removeAuthStateListener(objFirebaseListener);
        }
    }
}