package a4720.virginia.cs.uva.sous_chef;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class EditRecipeActivity extends AppCompatActivity {
    private Button btn;
    public Integer primaryKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editrecipe);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        getIncomingIntent();
    }


    private void getIncomingIntent(){
        primaryKey=Integer.parseInt(getIntent().getStringExtra("primaryKey"));
        Recipe eRecipe = MainActivity.getDb().recipeDao().getRecipe(primaryKey);
        setRecipe(eRecipe.name, eRecipe.ingredientLines, eRecipe.totalTime, eRecipe.numberOfServings, eRecipe.notes);
    }
    private void setRecipe(String name, ArrayList<String> ingredientLines, String totalTime, int numberOfServings, String notes) {
        EditText recipeTitle = findViewById(R.id.title);
        EditText recipeIngredients = findViewById(R.id.ingredients);
        EditText recipeTotalTime = findViewById(R.id.totalTime);
        EditText recipeServings =findViewById(R.id.numServings);
        EditText recipeNotes = findViewById(R.id.notes);

        recipeTitle.setText(name);
        for (int i = 0; i < ingredientLines.size(); i++) {
            recipeIngredients.append("\n" + ingredientLines.get(i));
        }
        recipeTotalTime.setText(totalTime);
        recipeServings.setText(Integer.toString(numberOfServings));
        recipeNotes.setText(notes);
    }

    public void editRecipe(View view) {
        EditText recipeTitle = (EditText) findViewById(R.id.title);
        EditText recipeIngredients = (EditText) findViewById(R.id.ingredients);
        EditText recipeTime = (EditText) findViewById(R.id.totalTime);
        EditText recipeServings = (EditText) findViewById(R.id.numServings);
        EditText recipeNotes = (EditText) findViewById(R.id.notes);

        if (recipeTitle.getText().toString().equals("")){
            recipeTitle.setError("Task title is required!");
        } else {
            String title = recipeTitle.getText().toString();
            String ingredients=recipeIngredients.getText().toString();
            ArrayList<String> ingredientsList= new ArrayList<String>(Arrays.asList(ingredients.split("\\n")));
            String totalTime = recipeTime.getText().toString();
            String servingsString=recipeServings.getText().toString();
            Integer servings=0;
            if(servingsString != null && !servingsString.isEmpty()){
                servings=Integer.parseInt(servingsString);
            }
            String notes = recipeNotes.getText().toString();
            MainActivity.getDb().recipeDao().updateRecipe(primaryKey, title, ingredientsList, totalTime, servings, notes);

            finish();
            Intent intent = new Intent(EditRecipeActivity.this,MainActivity.class);
            startActivity(intent);
        }
    }
}
