package com.bonappetit.bonappetit;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecipeDetails extends AppCompatActivity {


    RequestQueue requestQueue;

    Recipe recipe = new Recipe();
    String id,calories,key="";
    TextView title;
    FloatingActionButton fab;
    Boolean exists=false;

    final String apiURL = "https://api.spoonacular.com/recipes";
    final String apiID ="apiKey=51c719f6a4674569aef3e2ef6f81fdf0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        requestQueue = Volley.newRequestQueue(this);

        title = findViewById(R.id.title);

        Bundle b = getIntent().getExtras();
        id = b.getString("id");
        calories = b.getString("calories");


        fab=findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser mUser=mAuth.getCurrentUser();
                String uId=mUser.getUid();
                DatabaseReference rootReference = FirebaseDatabase.getInstance().getReference();
                DatabaseReference Ref = rootReference.child("Favorites").child(uId);
                if (!exists) {
                    Ref.push().setValue(recipe);
                    fab.setImageResource(R.drawable.ic_favorite_white);
                    exists=true;
                }
                else if (exists) {
                    Ref.child(key).setValue(null);
                    fab.setImageResource(R.drawable.ic_favorite_white_outline);
                    exists=false;
                }
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        checkDatabase();
        sendRequests();

    }

    private void sendRequests() {
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                apiURL + "/" + id + "/information?" + apiID,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            recipe.setCalories(calories);
                            recipe.setId(id);
                            recipe.setImage(response.getString("image"));
                            ImageView image = findViewById(R.id.image);
                            Picasso.get().load(response.getString("image")).fit().centerCrop().into(image);

                            TextView title = findViewById(R.id.title);
                            recipe.setTitle(response.getString("title"));
                            title.setText(response.getString("title"));
                            JSONArray temp = response.getJSONArray("extendedIngredients");
                            TextView ingredients = findViewById(R.id.ingredients);
                            ArrayList<String> list = new ArrayList<String>();
                            ingredients.append("Ingredients \n");

                            for (int i=0;i<temp.length();i++) {
                                JSONObject ingredient = temp.getJSONObject(i);
                                String ing = ingredient.getString("original");
                                ingredients.append(ing+"\n");
                                list.add(ing);
                            }

                            recipe.setIngredients(list);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );
        requestQueue.add(objectRequest);

        JsonArrayRequest arrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                apiURL+"/"+id+"/analyzedInstructions?"+apiID,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ArrayList<String> inst = new ArrayList<String>();

                        try {

                            TextView instructions = findViewById(R.id.instructions);
                            instructions.append("Instructions \n");
                            JSONArray temp = response.getJSONObject(0).getJSONArray("steps");

                            for (int i=0;i<temp.length();i++) {
                                inst.add(temp.getJSONObject(i).getString("number")+". "+temp.getJSONObject(i).getString("step"));
                                instructions.append(temp.getJSONObject(i).getString("number")+". "+temp.getJSONObject(i).getString("step")+"\n");
                            }

                            recipe.setInstructions(inst);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        requestQueue.add(arrayRequest);


    }
    private void checkDatabase() {
        Log.e("usercheck","entered check db");
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser=mAuth.getCurrentUser();
        String uId=mUser.getUid();
        DatabaseReference rootReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference Ref = rootReference.child("Favorites").child(uId);
        Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data: dataSnapshot.getChildren()
                ) {
                    Recipe r = data.getValue(Recipe.class);
                    if (r.getId().equals(id)) {
                        exists=true;
                        key=data.getKey();
                        Log.e("usercheck",key);
                        break;

                    }
                    else exists=false;

                    Log.e("usercheck",""+exists+" "+r.getTitle() );
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        if (exists) {
            fab.setImageResource(R.drawable.ic_favorite_white);

        }
        else if (!exists) {
            fab.setImageResource(R.drawable.ic_favorite_white_outline);

        }
    }

}
