package a4720.virginia.cs.uva.sous_chef.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;

import a4720.virginia.cs.uva.sous_chef.APIConnect.RecipeService;
import a4720.virginia.cs.uva.sous_chef.Objects.SearchResults;
import a4720.virginia.cs.uva.sous_chef.R;
import a4720.virginia.cs.uva.sous_chef.Recipe;
import a4720.virginia.cs.uva.sous_chef.RecipeDetailsActivity;
import a4720.virginia.cs.uva.sous_chef.RecipeListAdapter;
import a4720.virginia.cs.uva.sous_chef.Objects.InnerClasses.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class FindRecipesFragment extends Fragment {
    ArrayList<Recipe> recipes;
    RecyclerView rvRecipes;
    SearchView searchview;
    TextView searchResultsTextView;
    TextView noResultsTextView;

    Button prevButton;
    Button nextButton;

    TextView attributionText;
    TextView attributionURL;
    ImageView attributionImage;

    private String searchQuery;
    private int max = 10;
    private int start = 0;
    private int totalMatchCount;

    private static final String id = "cb47c5e6";
    private static final String key = "134207b176bbda4b3dafd4c9378bd3f2";
    private static final String baseURL = "https://api.yummly.com/v1/api/";

    private SearchResults results = new SearchResults();

    public FindRecipesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (savedInstanceState != null) {
            results = Parcels.unwrap(savedInstanceState.getParcelable("searchResults"));
            totalMatchCount = results.totalMatchCount;
            start = Parcels.unwrap(savedInstanceState.getParcelable("start"));
            searchQuery = Parcels.unwrap(savedInstanceState.getParcelable("searchQuery"));
        }

        View rootView = inflater.inflate(R.layout.fragment_find_recipes, container, false);
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("searchResults", Parcels.wrap(results));
        outState.putParcelable("start", Parcels.wrap(start));
        outState.putParcelable("searchQuery", Parcels.wrap(searchQuery));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        rvRecipes = (RecyclerView) view.findViewById(R.id.rvRecipes);
        searchview = (SearchView) view.findViewById(R.id.searchview);
        searchResultsTextView = (TextView) view.findViewById(R.id.search_results);
        noResultsTextView = (TextView) view.findViewById(R.id.no_results);

        prevButton = (Button) view.findViewById(R.id.prevButton);
        nextButton = (Button) view.findViewById(R.id.nextButton);

        attributionText = (TextView) view.findViewById(R.id.attribution_text);
        attributionURL = (TextView) view.findViewById(R.id.attribution_url);
        attributionImage = (ImageView) view.findViewById(R.id.attribution_logo);

        recipes = Recipe.createInitialRecipeList();
        RecipeListAdapter adapter = new RecipeListAdapter(getActivity(), recipes);
        rvRecipes.setAdapter(adapter);
        rvRecipes.setLayoutManager(new LinearLayoutManager(getActivity()));

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (start > 0 && !searchQuery.isEmpty()) {
                    start -= max;
                    getQuery(searchQuery, start);
                }
                else return;
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (start + max <= totalMatchCount && searchQuery != null && !searchQuery.isEmpty()) {
                    start += max;
                    getQuery(searchQuery, start);
                } else if (searchQuery != null && !searchQuery.isEmpty()){
                    Toast.makeText(getContext(), "No more results!", Toast.LENGTH_SHORT).show();
                }
                else return;
            }
        });

        final SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                searchQuery = query;
                start = 0;
                getQuery(query, start);
                return true;
            }
        };
        searchview.setOnQueryTextListener(queryTextListener);
        displayResults();
    }

    private void getQuery(String query, int startFromResult) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RecipeService service = retrofit.create(RecipeService.class);
        Call<SearchResults> call = service.searchQueryWithPagination(id, key, query, Integer.toString(max), Integer.toString(startFromResult));

        // handle response
        call.enqueue(new Callback<SearchResults>() {
            @Override
            public void onResponse(Call<SearchResults> call, Response<SearchResults> response) {
                //handle response here
                Gson gson = new Gson();
                SearchResults searchResults = response.body();
                results = searchResults;
                totalMatchCount = searchResults.totalMatchCount;
                displayResults();
                Log.d("Search", gson.toJson(searchResults));
            }

            @Override
            public void onFailure(Call<SearchResults> call, Throwable t) {
                Log.d("FindRecipesFragment", t.getMessage());
            }
        });
    }

    private void displayResults() {
        if (searchQuery != null) {
            // clear the previous recipes
            searchResultsTextView.setText("Search Results");
            recipes.clear();
            if (results.matches.size() == 0) {
                noResultsTextView.setVisibility(View.VISIBLE);
            } else {
                for (int i = 0; i < results.matches.size(); i++) {
                    // create a new recipe and add it to the results list
                    Match match = results.matches.get(i);
                    Recipe recipe = new Recipe(match.recipeName, match.id, match.rating, match.smallImageUrls.get(0));
                    recipes.add(recipe);
                }
                noResultsTextView.setVisibility(View.INVISIBLE);
            }

            setAttributions(results.getAttribution());
            updateButtonVisibility();
            rvRecipes.getAdapter().notifyDataSetChanged();
        }
        else return;
    }

    private void updateButtonVisibility() {
        // for previous button
        if (start != 0) {
            prevButton.setVisibility(View.VISIBLE);
        } else if (start == 0) {
            prevButton.setVisibility(View.INVISIBLE);
        }

        // for next button
        if (start + max < totalMatchCount) {
            nextButton.setVisibility(View.VISIBLE);
        } else if (start + max > totalMatchCount) {
            nextButton.setVisibility(View.INVISIBLE);
        }
    }

    private void setAttributions(Attribution attribution) {
        attributionText.setText(attribution.text);
        attributionURL.setText(attribution.url);
        Picasso.get().load(attribution.logo).resize(150,50).into(attributionImage);
    }


}
