package com.bonappetit.bonappetit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Favorites extends Fragment {

    private RecyclerView recyclerView;
    private ResultsAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Recipe> faves = new ArrayList<Recipe>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       // pullDataFromDatabase();


    }

    @Override
    public void onResume() {
        super.onResume();

        pullDataFromDatabase();
    }

    private void pullDataFromDatabase() {
        faves.clear();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser=mAuth.getCurrentUser();
        String uId=mUser.getUid();
        DatabaseReference rootReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference Ref = rootReference.child("Favorites").child(uId);
        Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("usercheck-before", dataSnapshot.toString());
                for(DataSnapshot data:dataSnapshot.getChildren()) {
                    Log.e("usercheck", data.toString());
                    Recipe recipe = data.getValue(Recipe.class);
                    faves.add(recipe);
                }

                if (faves.isEmpty()) {
                    Log.e("usercheck","faves empty");
                }
                else {
                    recyclerView=getView().findViewById(R.id.results_view);
                    recyclerView.setHasFixedSize(true);
                    layoutManager=new LinearLayoutManager(getContext());
                    adapter=new ResultsAdapter(faves);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);

                    adapter.setOnItemClickListener(new ResultsAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {

                            Intent i = new Intent(getContext(),RecipeDetails.class);
                            i.putExtra("id", faves.get(position).getId());
                            i.putExtra("calories",faves.get(position).getCalories());
                            startActivity(i);
                        }
                    });
                }


                Log.e("tag",faves.toArray().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
