package com.bonappetit.bonappetit;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FiltersDialog extends DialogFragment {

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
    RadioGroup preference;
    ArrayList<String> restrictions;
    Button ok,cancel;
    String d,i;

    String pref="";


    public interface OnFiltersChanged {
        void selectFilters(String preference, ArrayList<String> restrictions);
    }

    public OnFiltersChanged onFiltersChanged;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.layout_dialog,container,false);





        peanut = view.findViewById(R.id.peanut);
        dairy = view.findViewById(R.id.dairy);
        egg = view.findViewById(R.id.egg);
        gluten = view.findViewById(R.id.gluten);
        grain = view.findViewById(R.id.grain);
        seafood = view.findViewById(R.id.seafood);
        sesame = view.findViewById(R.id.sesame);
        shellfish = view.findViewById(R.id.shellfish);
        soy = view.findViewById(R.id.soy);

        treeNut = view.findViewById(R.id.treenut);
        wheat = view.findViewById(R.id.wheat);
        preference = view.findViewById(R.id.diet);
        ok = view.findViewById(R.id.ok);
        cancel = view.findViewById(R.id.cancel);
        restrictions=new ArrayList<String>();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
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

                int selectedDiet = preference.getCheckedRadioButtonId();

                if (selectedDiet == R.id.vegan) {
                    pref="vegan";
                }
                if (selectedDiet==R.id.vegetarian){

                    pref="vegetarian";
                }
                if (selectedDiet==R.id.pescetarian){
                    pref="pescetarian";
                }
                if (selectedDiet==R.id.whole30)
                    pref="whole30";
                if (selectedDiet==R.id.ketogenic)
                    pref="ketogenic";
                if (selectedDiet==R.id.glutenFree)
                    pref="gluten-free";


                onFiltersChanged.selectFilters(pref,restrictions);
                Log.e("sending",pref+" "+restrictions.toString());

                getDialog().dismiss();
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            onFiltersChanged = (OnFiltersChanged) getTargetFragment();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    private void pullUserDetailsFromDb() {

        FirebaseDatabase rootDb = FirebaseDatabase.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser=mAuth.getCurrentUser();
        String uId=mUser.getUid();
        DatabaseReference db = rootDb.getReference().child("UserDetails").child(uId);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                d=dataSnapshot.child("diet").getValue().toString();
                i=dataSnapshot.child("intolerances").getValue().toString().replace("]","").replace(" ","").replace("[","").trim();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
