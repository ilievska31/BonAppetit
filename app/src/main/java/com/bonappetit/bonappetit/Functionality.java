package com.bonappetit.bonappetit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class Functionality extends Fragment {
    FragmentTransaction fragmentTransaction;
    Fragment searchRecipe, searchIngredients;
    RadioButton sr,si;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       return inflater.inflate(R.layout.fragment_functionality, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        searchRecipe = new com.bonappetit.bonappetit.searchRecipe();
        searchIngredients = new com.bonappetit.bonappetit.searchIngredients();


        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fl_fragment, searchRecipe);
        fragmentTransaction.commit();


        sr = view.findViewById(R.id.search_recipes);
        si = view.findViewById(R.id  .search_ingredients);

        sr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fl_fragment, searchRecipe);
                fragmentTransaction.commit();
                sr.setTextColor(getResources().getColor(R.color.white));
                si.setTextColor(getResources().getColor(R.color.colorPrimary));

            }
        });

        si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fl_fragment, searchIngredients);
                fragmentTransaction.commit();
                sr.setTextColor(getResources().getColor(R.color.colorPrimary));
                si.setTextColor(getResources().getColor(R.color.white));

            }
        });

    }
}
