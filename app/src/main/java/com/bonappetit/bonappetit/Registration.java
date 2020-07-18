package com.bonappetit.bonappetit;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

public class Registration extends AppCompatActivity {

    private FirebaseAuth mAuth;
    String fn,ln,em,pw,cpw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();

        final EditText firstName = findViewById(R.id.firstName);
        final EditText lastName = findViewById(R.id.lastName);
        final EditText email = findViewById(R.id.email);
        final EditText password = findViewById(R.id.password);
        final EditText confirmPassword = findViewById(R.id.passwordRepeat);

        TextView loginText = findViewById(R.id.loginText);
        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Authentication.class));
            }
        });

        Button registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fn = firstName.getText().toString().trim();
                ln = lastName.getText().toString().trim();
                em = email.getText().toString().trim();
                pw = password.getText().toString().trim();
                cpw = confirmPassword.getText().toString().trim();

                if (TextUtils.isEmpty(fn)) {
                    firstName.setError("Required field");
                    return;
                }

                if (TextUtils.isEmpty(ln)) {
                    lastName.setError("Required field");
                    return;
                }

                if (TextUtils.isEmpty(em)) {
                    email.setError("Required field");
                    return;
                }

                if (TextUtils.isEmpty(pw)) {
                    password.setError("Required field");
                    return;
                }

                if (TextUtils.isEmpty(cpw)) {
                    confirmPassword.setError("Required field");
                    return;
                }

                Log.e("","Finish checking");

              /*  if (!pw.equals(cpw)) {
                    password.setError("Password fields don't match");
                    confirmPassword.setError("Password fields don't match");
                    return;
                }
*/
                Log.e("","email"+em+"pw"+pw);
                mAuth.createUserWithEmailAndPassword(em,pw).addOnCompleteListener(Registration.this,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.e("","Task completed, em i pw are "+em+pw);
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),"Logged in",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Registration.this,UserDetails.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            Log.e("","Task succsesful");
                        }
                        else if (!task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Something went wrong. Please try again",Toast.LENGTH_LONG).show();
                            Toast.makeText(getApplicationContext(),"email"+em+"pw"+pw,Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });

    }


}
