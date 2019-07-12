package a4720.virginia.cs.uva.sous_chef.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import a4720.virginia.cs.uva.sous_chef.AddRecipeActivity;
import a4720.virginia.cs.uva.sous_chef.MainActivity;
import a4720.virginia.cs.uva.sous_chef.R;
import a4720.virginia.cs.uva.sous_chef.Recipe;
import a4720.virginia.cs.uva.sous_chef.RecipeListAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class CookbookFragment extends Fragment {
    ArrayList<Recipe> recipes;
    RecyclerView rvRecipes;

    static final int ADD_ACTIVITY_REQUEST = 1;
    static final int EDIT_ACTIVITY_REQUEST = 2;

    private FloatingActionButton addButton;

    public CookbookFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cookbook, container, false);
        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        rvRecipes = (RecyclerView) view.findViewById(R.id.rvRecipes);

        recipes = Recipe.loadRecipeList();
        RecipeListAdapter adapter = new RecipeListAdapter(getActivity(), recipes);
        rvRecipes.setAdapter(adapter);
        rvRecipes.setLayoutManager(new LinearLayoutManager(getActivity()));

        addButton = (FloatingActionButton) getView().findViewById(R.id.add_button);

        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Let's add a recipe!", Toast.LENGTH_SHORT).show();
                Intent addRecipe = new Intent(getActivity(), AddRecipeActivity.class);
                getActivity().startActivityForResult(addRecipe, ADD_ACTIVITY_REQUEST);
            }
        });
    }
}
