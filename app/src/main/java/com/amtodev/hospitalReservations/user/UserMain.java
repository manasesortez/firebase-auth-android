package com.amtodev.hospitalReservations.user;

import static com.airbnb.lottie.L.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.Toast;
import com.amtodev.hospitalReservations.Login;
import com.amtodev.hospitalReservations.MainActivity;
import com.amtodev.hospitalReservations.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class UserMain extends AppCompatActivity {

    Button btnChangePassword;

    FirebaseAuth fAuth;
    FirebaseUser user;

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

        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogChangePassword(view);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void logoutUser(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }

    public void openDialogChangePassword(View view){
        final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(UserMain.this);
        final EditText resetPassword = new EditText(view.getContext());
        passwordResetDialog.setTitle("Cambiar Contraseña");
        passwordResetDialog.setMessage("Ingrese una nueva contraseña de mas de 6 Caracteres");
        passwordResetDialog.setView(resetPassword);
        passwordResetDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newPassword = resetPassword.getText().toString().trim();
                user.updatePassword(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(@NonNull Void unused) {
                        Toast.makeText(UserMain.this, "Contraseña Actualizada Correctamente", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UserMain.this, "Fallo al Actulizar Contraseña",  Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(UserMain.this, "No Actulizada", Toast.LENGTH_SHORT).show();
            }
        });
        passwordResetDialog.create().show();

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


    @Override
    public void onStop() {
        super.onStop();
        UserMain.this.finish();
    }

}