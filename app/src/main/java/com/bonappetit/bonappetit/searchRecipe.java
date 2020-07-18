package com.bonappetit.bonappetit;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class searchRecipe extends androidx.fragment.app.Fragment implements FiltersDialog.OnFiltersChanged {


    public static final String TAG = "searchRecipe";



    JSONArray results;
    ArrayList<Recipe> resultsArray = new ArrayList<>();
    ArrayList<Recipe> rec = new ArrayList<>();
    RequestQueue requestQueue;
    TextView cal,error;
    SeekBar calorieLimit;
    EditText searchTerm;
    boolean firstClickQuery,firstClickFilter;
    final String apiURL = "https://api.spoonacular.com/recipes";
    final String apiID ="&apiKey=51c719f6a4674569aef3e2ef6f81fdf0";
    private RecyclerView recyclerView;
    private ResultsAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    String diet="";
    String intolerances="";

    public searchRecipe() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_recipe, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView=getView().findViewById(R.id.results_view);


        firstClickQuery=true;

        cal=view.findViewById(R.id.cal);


        requestQueue = Volley.newRequestQueue(getContext());
        calorieLimit = view.findViewById(R.id.calorie_limit);

        pullUserDetailsFromDb();


        searchTerm = view.findViewById(R.id.search_term);
        searchTerm.setText("");

        searchTerm.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!firstClickQuery) {
                    checkAndSendRequest();
                }
                else firstClickQuery=false;
            }
        });

        Button filterButton = view.findViewById(R.id.filter_button);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateFilters();
            }
        });


        calorieLimit.setMax(1500);


        calorieLimit.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int limit, boolean b) {
                if (limit==0) {
                    cal.setText("");
                    if (!resultsArray.isEmpty()) {

                        for (Recipe r:resultsArray) {
                            r.setHide("false");
                        }
                    }
                }

                else
                    cal.setText(""+limit);

                if (!resultsArray.isEmpty()) {

                    for (Recipe r:resultsArray) {
                        if (Float.parseFloat(r.getCalories())>limit) {
                            r.setHide("true");

                        }
                        else if (Float.parseFloat(r.getCalories())<=limit) r.setHide("false");


                    }
                }
                showResults(resultsArray);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    private void checkAndSendRequest() {
        if (diet.equals("") && intolerances.equals(""))
            sendRequest();
        if (!diet.equals("") && intolerances.equals(""))
            sendRequestWithDiet();
        if (diet.equals("") && !intolerances.equals(""))
            sendRequestWithIntolerances();
        if (!diet.equals("") && !intolerances.equals(""))
            sendRequestWithFilters();
    }


    private void showResults (ArrayList<Recipe> results) {

        rec.clear();
        for (Recipe r: results) {
            if (r.getHide().equals("false"))
                rec.add(r);

        }



        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(getContext());
        adapter=new ResultsAdapter(rec);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new ResultsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                Intent i = new Intent(getContext(),RecipeDetails.class);
                i.putExtra("id", rec.get(position).getId());
                i.putExtra("calories",rec.get(position).getCalories());
                startActivity(i);
            }
        });

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
                if (dataSnapshot.child("diet").getValue().toString()!=null)
                    diet=dataSnapshot.child("diet").getValue().toString();
                if (dataSnapshot.child("intolerances").getValue()!=null)
                    intolerances=dataSnapshot.child("intolerances").getValue().toString().replace("]","").replace(" ","").replace("[","").trim();
                Log.e("userc",intolerances);
                Log.e("userc",diet);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void updateFilters() {

        diet="";
        intolerances="";
        FiltersDialog filtersDialog = new FiltersDialog();
        filtersDialog.setTargetFragment(searchRecipe.this,1);
        filtersDialog.show(getFragmentManager(),"filtersDialog");

    }


    @Override
    public void selectFilters(String preference, ArrayList<String> restrictions) {
        Log.e("sending_r",preference+" "+restrictions.toString());
        diet=preference;
        intolerances=restrictions.toString().replace("]","").replace(" ","").replace("[","").trim();
        Log.e("sending_set",diet+" "+intolerances);
        checkAndSendRequest();

    }


    private void sendRequest() {


        JsonObjectRequest arrayRequest = new JsonObjectRequest(Request.Method.GET,
                apiURL + "/complexSearch?maxCalories=10000&number=100&query="+searchTerm.getText().toString().trim().replace(" ","+") + apiID,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        resultsArray.clear();
                        calorieLimit.setProgress(0);
                        try {
                            JSONObject temp;
                            results=response.getJSONArray("results");
                            for (int i=0;i<results.length();i++) {
                                temp = results.getJSONObject(i);
                                resultsArray.add(new Recipe(temp.getString("image"),temp.getString("title"),temp.getJSONArray("nutrition").getJSONObject(0).getString("amount"),temp.getString("id"),"false"));
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        showResults(resultsArray);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();

                    }
                });
        Log.e("request_sr",diet+"/"+intolerances);
        requestQueue.add(arrayRequest);
    }
    private void sendRequestWithDiet() {


        JsonObjectRequest arrayRequest = new JsonObjectRequest(Request.Method.GET,
                apiURL + "/complexSearch?maxCalories=10000&number=100&query="+searchTerm.getText().toString().trim().replace(" ","+") +"&diet="+diet+apiID,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        resultsArray.clear();
                        calorieLimit.setProgress(0);
                        try {
                            JSONObject temp;
                            results=response.getJSONArray("results");
                            for (int i=0;i<results.length();i++) {
                                temp = results.getJSONObject(i);
                                resultsArray.add(new Recipe(temp.getString("image"),temp.getString("title"),temp.getJSONArray("nutrition").getJSONObject(0).getString("amount"),temp.getString("id"),"false"));
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        showResults(resultsArray);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();

                    }
                });
        Log.e("request_d",diet+"/"+intolerances);
        requestQueue.add(arrayRequest);
    }
    private void sendRequestWithIntolerances() {



        JsonObjectRequest arrayRequest = new JsonObjectRequest(Request.Method.GET,
                apiURL + "/complexSearch?maxCalories=10000&number=100&query="+searchTerm.getText().toString().trim().replace(" ","+") +"&intolerances="+intolerances+ apiID,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        resultsArray.clear();
                        calorieLimit.setProgress(0);
                        try {
                            JSONObject temp;
                            results=response.getJSONArray("results");
                            for (int i=0;i<results.length();i++) {
                                temp = results.getJSONObject(i);
                                resultsArray.add(new Recipe(temp.getString("image"),temp.getString("title"),temp.getJSONArray("nutrition").getJSONObject(0).getString("amount"),temp.getString("id"),"false"));
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        showResults(resultsArray);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();

                    }
                });
        Log.e("request_i",diet+"/"+intolerances);
        requestQueue.add(arrayRequest);
    }
    private void sendRequestWithFilters() {



        JsonObjectRequest arrayRequest = new JsonObjectRequest(Request.Method.GET,
                apiURL + "/complexSearch?maxCalories=10000&number=100&query="+searchTerm.getText().toString().trim().replace(" ","+") +"&diet="+diet+"&intolerances="+intolerances+ apiID,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        resultsArray.clear();
                        calorieLimit.setProgress(0);
                        try {
                            JSONObject temp;
                            results=response.getJSONArray("results");
                            for (int i=0;i<results.length();i++) {
                                temp = results.getJSONObject(i);
                                resultsArray.add(new Recipe(temp.getString("image"),temp.getString("title"),temp.getJSONArray("nutrition").getJSONObject(0).getString("amount"),temp.getString("id"),"false"));
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        showResults(resultsArray);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();

                    }
                });
        Log.e("request_f",diet+"/"+intolerances);
        requestQueue.add(arrayRequest);
    }
}


