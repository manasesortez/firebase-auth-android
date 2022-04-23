package com.amtodev.hospitalReservations;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.amtodev.hospitalReservations.user.UserMain;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class Login extends AppCompatActivity {

    EditText email, password;
    Button loginBtn;
    Boolean valid = true;
    FirebaseAuth fAuth;
    FirebaseUser user;
    FirebaseFirestore fStore;
    AuthResult result;

    @SuppressLint({"CutPasteId", "ObsoleteSdkInt"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user = fAuth.getCurrentUser();
        //for changing status bar icon colors
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        email = (EditText) findViewById(R.id.editTextEmailLogin);
        password = (EditText) findViewById(R.id.editTextPasswordLogin);
        loginBtn = (Button) findViewById(R.id.cirLoginButtonLogin);
        loginBtn.setOnClickListener(new  View.OnClickListener(){
            @Override
            public void onClick(View view) {
                checkField(email);
                checkField(password);
                signin();
            }
        });
    }

    private void signin(){
        if(valid){
            fAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        if(fAuth.getCurrentUser().isEmailVerified()){
                            Toast.makeText(Login.this,"Welcome to App", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), UserMain.class));
                        }else{
                            sendEmailVerification();
                            Toast.makeText(Login.this,"Please verify your E-mail", Toast.LENGTH_SHORT).show();
                            fAuth.signOut();
                        }
                    }else{
                        Toast.makeText(Login.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener(){
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Login.this, "Failed to Login", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void sendEmailVerification() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.sendEmailVerification()
        .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Login.this, "E-mail verification Sent" , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void onLoginClick(View View){
        startActivity(new Intent(this,Register.class));
        overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
        finish();
    }

    public void onFogetPasswordClick(View View){
        startActivity(new Intent(this,forget_password.class));
        overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
        finish();
    }

    public boolean checkField(EditText textfield){
        if(textfield.getText().toString().isEmpty()){
            textfield.setError("Error");
            valid = false;
            Toast.makeText(Login.this, "You cannot leave an empty field", Toast.LENGTH_SHORT).show();
        }else{
            valid = true;
        }
        return valid;
    }


    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            DocumentReference df = FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
            df.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>(){
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        if(fAuth.getCurrentUser().isEmailVerified()){
                            Toast.makeText(Login.this,"Welcome to App", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), UserMain.class));
                        }else{
                            Toast.makeText(Login.this,"Please verify your E-mail", Toast.LENGTH_SHORT).show();
                            sendEmailVerification();
                            fAuth.signOut();
                        }
                    }else{
                        Toast.makeText(Login.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener(){
                @Override
                public void onFailure(@NonNull Exception e) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getApplicationContext(), Login.class));
                    finish();
                }
            });

        }
    }
}