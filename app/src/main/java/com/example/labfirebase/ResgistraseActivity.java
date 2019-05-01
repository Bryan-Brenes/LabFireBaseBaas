package com.example.labfirebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ResgistraseActivity extends AppCompatActivity {

    EditText correoEditText;
    EditText passwordEditText;
    EditText confirmPasswordEditText;
    Button registrarseBtn;

    private final String TAG = "register";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resgistrase);

        correoEditText = findViewById(R.id.emailResgisterEditText);
        passwordEditText = findViewById(R.id.passwordRegisterEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        registrarseBtn = findViewById(R.id.registrarseBtn);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    public boolean validateForm(){

        String correo = correoEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        if (correo.equals("")){
            Toast.makeText(this, "Correo requerido", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.equals("")){
            Toast.makeText(this, "Contraseña requerida", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!confirmPassword.equals(password)){
            Toast.makeText(this, "Contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            irPantallaPrincipal();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // [START_EXCLUDE]
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }

    public void registrarUsuario(View view){
        if(validateForm()){
            createAccount(correoEditText.getText().toString(), passwordEditText.getText().toString());
        }
    }

    public void irPantallaPrincipal(){
        Intent intent = new Intent(getApplicationContext(), Inventario.class);
        startActivity(intent);
    }
}
