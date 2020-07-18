package com.bonappetit.bonappetit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class UserDetails extends AppCompatActivity {

    String diet="";
    ArrayList<String> restrictions;

    CheckBox peanut;
    CheckBox dairy;
    CheckBox egg;
    CheckBox gluten;
    CheckBox grain;
    CheckBox seafood;
    CheckBox sesame;
    CheckBox shellfish;
    CheckBox soy;
    CheckBox treeNut;
    CheckBox wheat;

    String fn,ln;
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);



        restrictions=new ArrayList<String>();
        peanut = findViewById(R.id.peanut);
        dairy = findViewById(R.id.dairy);
        egg = findViewById(R.id.egg);
        gluten = findViewById(R.id.gluten);
        grain = findViewById(R.id.grain);
        seafood = findViewById(R.id.seafood);
        sesame = findViewById(R.id.sesame);
        shellfish = findViewById(R.id.shellfish);
        soy = findViewById(R.id.soy);

        treeNut = findViewById(R.id.treenut);
        wheat = findViewById(R.id.wheat);

        fn=getIntent().getStringExtra("firstName");
        ln=getIntent().getStringExtra("lastName");





        Button saveDetails = findViewById(R.id.save_details);

        saveDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (peanut.isChecked()) {
                    restrictions.add("peanut");
                }

                if (dairy.isChecked()) {
                    restrictions.add("dairy");
                }

                if (egg.isChecked()) {
                    restrictions.add("egg");
                }

                if (gluten.isChecked()) {
                    restrictions.add("gluten");
                }

                if (grain.isChecked()) {
                    restrictions.add("grain");
                }

                if (seafood.isChecked()) {
                    restrictions.add("seafood");
                }

                if (sesame.isChecked()) {
                    restrictions.add("sesame");
                }

                if (shellfish.isChecked()) {
                    restrictions.add("shellfish");
                }

                if (soy.isChecked()) {
                    restrictions.add("soy");
                }


                if (treeNut.isChecked()) {
                    restrictions.add("tree-nut");
                }

                if (wheat.isChecked()) {
                    restrictions.add("wheat");
                }

                RadioGroup preference = findViewById(R.id.diet);

                int selectedDiet = preference.getCheckedRadioButtonId();

                if (selectedDiet == R.id.vegan) {
                    diet="vegan";
                }
                else if (selectedDiet==R.id.vegetarian){

                    diet="vegetarian";
                }
                else if (selectedDiet==R.id.pescetarian){
                    diet="pescetarian";
                }
                else if (selectedDiet==R.id.whole30)
                    diet="whole30";
                else if (selectedDiet==R.id.ketogenic)
                    diet="ketogenic";
                else if (selectedDiet==R.id.glutenFree)
                    diet="gluten-free";

                mAuth = FirebaseAuth.getInstance();
                FirebaseUser mUser=mAuth.getCurrentUser();
                String uId=mUser.getUid();


                Log.e("",""+uId);
                Log.e("",""+diet);
                Log.e("",""+restrictions);



                DatabaseReference rootReference = FirebaseDatabase.getInstance().getReference();
                DatabaseReference Ref = rootReference.child("UserDetails").child(uId);

                User user = new User(fn,ln,diet,restrictions);
                Ref.setValue(user);
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


            }

        });




    }

    class BackgroundThread extends Thread{
        @Override
        public void run() {
            super.run();


        }
    }
}
