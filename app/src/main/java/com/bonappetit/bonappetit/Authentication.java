package com.bonappetit.bonappetit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Authentication extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ProgressDialog mDialog;
    EditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        mAuth = FirebaseAuth.getInstance();
        mDialog = new ProgressDialog(this);

        TextView registerText = findViewById(R.id.registerText);
        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),Registration.class);
                startActivity(i);
            }
        });


        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        Button loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String em = email.getText().toString().trim();
                String pw = password.getText().toString().trim();

                if (TextUtils.isEmpty(em)) {
                    email.setError("Required Field");
                    return;
                }
                if (TextUtils.isEmpty(pw)) {
                    password.setError("Required Field");
                    return;
                }

                mDialog.setMessage("Please wait...");
                mDialog.show();
                mAuth.signInWithEmailAndPassword(em,pw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            Toast.makeText(getApplicationContext(),"Logged in",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Authentication.this,MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                            startActivity(intent);
                            mDialog.dismiss();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Something went wrong. Please try again",Toast.LENGTH_LONG).show();
                            mDialog.dismiss();
                        }
                    }
                });

            }
        });

    }
}
