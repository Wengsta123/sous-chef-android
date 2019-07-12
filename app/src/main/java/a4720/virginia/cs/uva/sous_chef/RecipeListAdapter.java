package a4720.virginia.cs.uva.sous_chef;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.List;

public class RecipeListAdapter extends
        RecyclerView.Adapter<RecipeListAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView ratingTextView;
        public ImageView imageView;

        LinearLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.card_recipe_name);
            ratingTextView = (TextView) itemView.findViewById(R.id.rating);
            imageView = (ImageView) itemView.findViewById(R.id.card_recipe_image);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }

    private List<Recipe> mRecipes;
    private Context mContext;

    public RecipeListAdapter(Context context, List<Recipe> recipes) {
        mRecipes = recipes;
        mContext = context;
    }

    private Context getContext() {
        return mContext;
    }

    @Override
    public RecipeListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View recipeView = inflater.inflate(R.layout.recipe_cardview, parent, false);

        ViewHolder viewHolder = new ViewHolder(recipeView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecipeListAdapter.ViewHolder viewHolder, final int position) {
        Recipe recipe = mRecipes.get(position);

        TextView nameTextView = viewHolder.nameTextView;
        nameTextView.setText(recipe.getName());
        if (recipe.rating!=0) {
            TextView ratingTextView = viewHolder.ratingTextView;
            ratingTextView.setText("Rating: " + Integer.toString(recipe.rating) + "/5");
            ratingTextView.setVisibility(View.VISIBLE);
        }
        ImageView imageView = viewHolder.imageView;

        if (recipe.cardImageUrl!=null && !recipe.cardImageUrl.isEmpty()) {
            Picasso.get().load(recipe.cardImageUrl).resize(350, 250).into(imageView);
        } else if (recipe.localImageBytes != null && recipe.localImageBytes.length >=0 ) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(recipe.localImageBytes, 0, recipe.localImageBytes.length);
            imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 350, 250, false));
        }

        // set onClickListener for card
        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Recipe recipe = mRecipes.get(position);
                Intent intent = new Intent(mContext, RecipeDetailsActivity.class);
//                intent.putExtra("recipe", Parcels.wrap(recipe));
                intent.putExtra("primarykey",Integer.toString(recipe.getUid()));
                try {
                    intent.putExtra("saved", recipe.getSaved());
                } catch (NullPointerException e){

                }
                try {
                    intent.putExtra("id", recipe.getId());
                } catch (NullPointerException e) {

                }
                mContext.startActivity(intent);
                Log.d("BucketListAdapter", "onClick: clicked on " + recipe);
            }
        });
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mRecipes.size();
    }
}