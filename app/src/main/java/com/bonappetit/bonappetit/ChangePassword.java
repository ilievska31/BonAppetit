package com.bonappetit.bonappetit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassword extends AppCompatActivity {

    EditText currentPassword, newPassword1, newPassword2;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);


        currentPassword = findViewById(R.id.current_password);
        newPassword1 = findViewById(R.id.new_password1);
        newPassword2 = findViewById(R.id.new_password2);

        Button change = findViewById(R.id.change);

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("usercheck", "OnClick entered");
                if (!newPassword2.getText().toString().equals(newPassword1.getText().toString())) {
                    newPassword2.setError("Password doesn't match");
                    newPassword1.setError("Password doesn't match");
                    Log.e("usercheck", "Passwords don't match");

                }
                else {

                    Log.e("usercheck", "Passwords match");
                    user = FirebaseAuth.getInstance().getCurrentUser();
                    AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword.getText().toString());

                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                user.updatePassword(newPassword1.getText().toString());
                                Toast.makeText(getApplicationContext(),"Password Updated!", Toast.LENGTH_LONG);
                                newPassword1.setText("");
                                newPassword2.setText("");
                                currentPassword.setText("");

                                Log.e("usercheck", "Changed");

                            }
                            else {
                                Toast.makeText(getApplicationContext(),"Something went wrong. Please try again later,", Toast.LENGTH_LONG);
                                Log.e("usercheck", "Error");
                            }
                        }
                    });

                }
            }
        });


    }
}
