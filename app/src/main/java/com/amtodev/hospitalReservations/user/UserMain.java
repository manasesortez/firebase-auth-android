package com.amtodev.hospitalReservations.user;

import static com.airbnb.lottie.L.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import com.amtodev.hospitalReservations.Login;
import com.amtodev.hospitalReservations.MainActivity;
import com.amtodev.hospitalReservations.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UserMain extends AppCompatActivity {

    Button btnChangePassword;
    private FirebaseAuth objFirebase;
    private FirebaseAuth.AuthStateListener objFirebaseListener;

    RecyclerView recycleViewHospital;

    private ArrayList<Adaptador> listaVehiculos = new ArrayList<>();

    listaAdaptadorVehiculo objListaAdaptadorVehiculos;

    TextView textView2;

    //Base de Datos
    FirebaseDatabase objBaseDatos;
    DatabaseReference dbReference;
    FirebaseUser objUser;
    ImageButton SearchHospital;
    ImageView addCarButton;
    SearchView txtCriterioDoctor;

    private ProgressDialog objDialog;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        ImageView logoutUser = findViewById(R.id.buttonLoggoutUser);
        logoutUser.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                openDialog(view);
            }
        });
        txtCriterioDoctor = findViewById(R.id.txtCriterioDoctor);

        textView2 = findViewById(R.id.textView2);

        SearchHospital = findViewById(R.id.btnSearchUserHospital);
        SearchHospital.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
            }
        });

        addCarButton = findViewById(R.id.addCarButton);
        addCarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCar();
            }
        });

        recycleViewHospital = findViewById(R.id.recycleViewHospital);

        objDialog = new ProgressDialog(this);

        objFirebase = FirebaseAuth.getInstance();

        objFirebaseListener = new FirebaseAuth.AuthStateListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser objUsuario = firebaseAuth.getCurrentUser();
                if (objUsuario != null){
                    textView2.setText("View Car Data");
                }
            }
        };

        FirebaseApp.initializeApp(UserMain.this);
        objBaseDatos = FirebaseDatabase.getInstance("https://agilereservations-default-rtdb.firebaseio.com/");
        dbReference = objBaseDatos.getReference().child("tblVehiculos");

    }

    public void CargarVehiculos(){
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaVehiculos.clear();
                for (DataSnapshot objFilaDatos : dataSnapshot.getChildren()){
                    Adaptador objVehiculos = objFilaDatos.getValue(Adaptador.class);
                    listaVehiculos.add(objVehiculos);
                }
                objListaAdaptadorVehiculos = new listaAdaptadorVehiculo(UserMain.this, listaVehiculos);
                recycleViewHospital.setAdapter(objListaAdaptadorVehiculos);
                objDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void buscar(){

    }

    @Override
    protected void onResume() {
        super.onResume();
        buscar();
        objDialog.setMessage("Obteniendo Datos...");
        objDialog.show();
        CargarVehiculos();
    }

    public void addCar(){
        startActivity(new Intent(getApplicationContext(), AddCar.class));
        finish();
    }

    public void logoutUser(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }

    public void openDialog(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(UserMain.this);
        builder.setTitle("Confirm");
        builder.setMessage("Are you Sure to Logout");
        builder.setPositiveButton("Yes, Logout", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                logoutUser();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(UserMain.this, "No Logout", Toast.LENGTH_SHORT).show();
            }
        });
        builder.create();
        builder.show();
    }

    protected void onStart() {
        super.onStart();
        objFirebase.addAuthStateListener(objFirebaseListener);
        if(txtCriterioDoctor != null){
            txtCriterioDoctor.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    search(s);
                    return true;
                }
            });
        }
    }

    private void search(String str) {
        ArrayList<Adaptador> myList = new ArrayList<>();
        for (Adaptador object: listaVehiculos){
            if (object.getModelo().toLowerCase().contains(str.toLowerCase())){
                myList.add(object);
            }
        }
        objListaAdaptadorVehiculos = new listaAdaptadorVehiculo(UserMain.this, myList);
        recycleViewHospital.setAdapter(objListaAdaptadorVehiculos);
    }

    @Override
    public void onStop() {
        super.onStop();
        UserMain.this.finish();
        if (objFirebaseListener != null){
            objFirebase.removeAuthStateListener(objFirebaseListener);
        }

    }

}