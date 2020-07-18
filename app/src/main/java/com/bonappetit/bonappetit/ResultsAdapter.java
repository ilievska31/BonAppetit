package com.bonappetit.bonappetit;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ResultsViewHolder> {


    private OnItemClickListener itemClickListener;
    private ArrayList<Recipe> recipeArrayList;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        itemClickListener=listener;
    }

    public static class ResultsViewHolder extends RecyclerView.ViewHolder {

        public ImageView recipeImage;
        public TextView recipeTitle;
        public TextView recipeCalories;



        public ResultsViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            recipeImage=itemView.findViewById(R.id.recipe_image);
            recipeTitle=itemView.findViewById(R.id.recipe_title);
            recipeCalories =itemView.findViewById(R.id.recipe_nutrition);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (listener!=null) {
                        int position=getAdapterPosition();
                        if (position!=RecyclerView.NO_POSITION)
                            listener.onItemClick(position);
                    }
                }
            });
        }
    }

    public ResultsAdapter(ArrayList<Recipe> recipes) {
        recipeArrayList=recipes;
    }

    @NonNull
    @Override
    public ResultsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.results_card,viewGroup,false);
        ResultsViewHolder rvh = new ResultsViewHolder(view,itemClickListener);
        return rvh;
    }

    @Override
    public void onBindViewHolder(@NonNull ResultsViewHolder resultsViewHolder, int i) {


        Recipe current = recipeArrayList.get(i);
        resultsViewHolder.recipeTitle.setText(current.getTitle());
        resultsViewHolder.recipeCalories.setText(current.getCalories()+" cal");

        Picasso.get().load(current.getImage()).into(resultsViewHolder.recipeImage);



    }

    @Override
    public int getItemCount() {
        return recipeArrayList.size();
    }
}
