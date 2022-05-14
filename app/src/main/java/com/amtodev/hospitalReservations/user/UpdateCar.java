package com.amtodev.hospitalReservations.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.amtodev.hospitalReservations.Login;
import com.amtodev.hospitalReservations.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class UpdateCar extends AppCompatActivity {

    ImageView next_activityUpdate;
    EditText txtModelCarUpdate, txtMarkCarUpdate, txtYearCarUpdate;
    Button btnUpdateCar, btnDeleteCar;

    private FirebaseAuth objFirebase;
    private FirebaseAuth.AuthStateListener objFirebaseListener;

    //database
    FirebaseDatabase objDataBase;
    DatabaseReference dbReference;


    FirebaseUser objUsuario;
    private ProgressDialog objDialog;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_car);

        txtMarkCarUpdate = findViewById(R.id.txtMarkCarUpdate);
        txtModelCarUpdate = findViewById(R.id.txtCarModelUpdate);
        txtYearCarUpdate = findViewById(R.id.txtYearCarUpdate);

        btnUpdateCar = findViewById(R.id.btnSaveCarUpdate);
        btnDeleteCar = findViewById(R.id.btnDeleteCar);

        objDialog = new ProgressDialog(this);

        next_activityUpdate = findViewById(R.id.next_activityUpdate);
        next_activityUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMain();
            }
        });

        btnUpdateCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtModelCarUpdate.getText().toString().isEmpty()){
                    Toast.makeText(UpdateCar.this, "Don't leave field empty",
                            Toast.LENGTH_LONG).show();
                }if (txtMarkCarUpdate.getText().toString().isEmpty()){
                    Toast.makeText(UpdateCar.this, "Don't leave field empty",
                            Toast.LENGTH_LONG).show();
                }if (txtYearCarUpdate.getText().toString().isEmpty()){
                    Toast.makeText(UpdateCar.this, "Don't leave field empty",
                            Toast.LENGTH_LONG).show();
                }else {
                    actualizarVehiculo();
                    txtModelCarUpdate.getText().clear();
                    txtMarkCarUpdate.getText().clear();
                    txtYearCarUpdate.getText().clear();
                }
            }
        });

        btnDeleteCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog(view);
            }
        });


        FirebaseApp.initializeApp(UpdateCar.this);
        objDataBase = FirebaseDatabase.getInstance("https://parcial-pama-default-rtdb.firebaseio.com/");
        dbReference = objDataBase.getReference().child("tblVehiculos");

    }

    private void actualizarVehiculo() {
        objDialog.setMessage("Modificando datos...");
        objDialog.show();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        Adaptador objVehiculos = new Adaptador();
        Adaptador adap_edit = (Adaptador)getIntent().getSerializableExtra("edit");
        objVehiculos.setId_vehiculo(adap_edit.getId_vehiculo());
        objVehiculos.setMarca(txtMarkCarUpdate.getText().toString());
        objVehiculos.setModelo(txtModelCarUpdate.getText().toString());
        objVehiculos.setUsuario(adap_edit.getUsuario().toString());
        objVehiculos.setAnio(txtYearCarUpdate.getText().toString());
        dbReference.child(objVehiculos.getId_vehiculo()).setValue(objVehiculos);
        objDialog.dismiss();
        Toast.makeText(getApplicationContext(), "Datos modificado con exito", Toast.LENGTH_SHORT).show();
        this.finish();
    }

    private void openMain() {
        startActivity(new Intent(getApplicationContext(), UserMain.class));
        finish();
    }

    private void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }

    public void openDialog(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateCar.this);
        builder.setTitle("Confirm");
        builder.setMessage("Are you Sure Delete this Register Car");
        builder.setPositiveButton("Yes, Logout", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteVehiculo();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(UpdateCar.this, "No Delete", Toast.LENGTH_SHORT).show();
            }
        });
        builder.create();
        builder.show();
    }

    private void deleteVehiculo() {
        objDialog.setMessage("Eliminando datos...");
        objDialog.show();
        Adaptador adap_edit = (Adaptador)getIntent().getSerializableExtra("edit");
        Adaptador objVehiculos =  new Adaptador();
        objVehiculos.setId_vehiculo(adap_edit.getId_vehiculo());
        dbReference.child(objVehiculos.getId_vehiculo()).removeValue();
        objDialog.dismiss();
        Toast.makeText(getApplicationContext(), "Datos eliminado con exito", Toast.LENGTH_SHORT).show();
        this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Adaptador adap_edit = (Adaptador)getIntent().getSerializableExtra("edit");
        if(adap_edit != null){
            btnUpdateCar.setText("Actualizar");
            txtModelCarUpdate.setText(adap_edit.getModelo());
            txtMarkCarUpdate.setText(adap_edit.getMarca());
            txtYearCarUpdate.setText(adap_edit.getAnio());
        }
    }
}