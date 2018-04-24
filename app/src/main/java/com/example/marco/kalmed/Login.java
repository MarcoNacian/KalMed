package com.example.marco.kalmed;

import  android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.widget.Toast.LENGTH_SHORT;

public class Login extends AppCompatActivity {

    //CASTEO
    //Elementos Graficos del activity_login.xml
    @BindView(R.id.userETXML) EditText userET;
    @BindView(R.id.passETXML) EditText passET;
    @BindView(R.id.loginBTNXML) Button loginBTN;
    @BindView(R.id.l_signInBTNXML) Button signinBTN;

    //Elementos de FirebaseAuthentication
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authListener;


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if ( user != null){
                    if(!user.isEmailVerified()){
                        Toast.makeText(Login.this,"Email no verificado" + user.getEmail(),Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(Login.this,"Navegar a la actividad de calendario",Toast.LENGTH_LONG).show();
                        openHome();
                    }
                }
            }
        };
    }

    private void openHome() {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authListener);
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (authListener != null)
            mAuth.removeAuthStateListener(authListener);
    }


    public void logIn(View view) {
        String username = userET.getText().toString();
        String password = passET.getText().toString();

        mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()){
                    Toast.makeText(Login.this,"Hubo un Error",Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    public void signIn(View view) {
        Intent intent = new Intent(this,Signin.class);
        startActivity(intent);
    }
}


