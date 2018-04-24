package com.example.marco.kalmed;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Signin extends AppCompatActivity {

    @BindView(R.id.userETXML) EditText userET;
    @BindView(R.id.passETXML) EditText passET;

    //Elementos de FirebaseAuthentication
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if ( user != null){
                    Toast.makeText(Signin.this,"El usuario fue creado",Toast.LENGTH_LONG).show();
                }
            }
        };

    }

    public void signIn(View view) {

        String username = userET.getText().toString();
        String password = passET.getText().toString();

        mAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()){
                    Toast.makeText(Signin.this,"Hubo un error de registro",Toast.LENGTH_LONG).show();
                }
                else{
                    FirebaseUser user = mAuth.getCurrentUser();
                    user.sendEmailVerification();
                }
            }
        });

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

}