package com.bonappetit.bonappetit;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class EditUserDetails extends Fragment {

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
    RadioGroup preference;
    String fn,ln;
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e("enter","entered on create view");
        return inflater.inflate(R.layout.fragment_edit_user_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.e("enter","entered on view created");


        restrictions=new ArrayList<String>();
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

        //fn=getActivity().getIntent().getStringExtra("firstName");
        //ln=getActivity().getIntent().getStringExtra("lastName");


        //Log.e("extra",fn);
        //Log.e("extra",ln);


       /// Log.e("enter",peanut.toString());
        Button saveDetails = view.findViewById(R.id.save_details);

        saveDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                restrictions.clear();
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
                Ref.child("diet").setValue(diet);
                Ref.child("intolerances").setValue(restrictions);

              }

        });

    }
}
