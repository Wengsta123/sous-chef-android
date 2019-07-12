package a4720.virginia.cs.uva.sous_chef;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

import a4720.virginia.cs.uva.sous_chef.R;

public class AddRecipeActivity extends AppCompatActivity {
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addrecipe);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    public void createNewRecipe(View view) {
        Intent returnIntent = new Intent();
        Bundle extras = new Bundle();
        EditText itemTitle = (EditText) findViewById(R.id.title);
        EditText itemIngredients = (EditText) findViewById(R.id.ingredients);
        EditText itemTime = (EditText) findViewById(R.id.totalTime);
        EditText itemServings = (EditText) findViewById(R.id.numServings);
        EditText itemNotes = (EditText) findViewById(R.id.notes);

        if (itemTitle.getText().toString().equals("")){
            itemTitle.setError("Task title is required!");
        } else {
            extras.putString("title", itemTitle.getText().toString());
            extras.putString("ingredients", itemIngredients.getText().toString());
            extras.putString("time", itemTime.getText().toString());
            extras.putString("servings", itemServings.getText().toString());
            extras.putString("notes", itemNotes.getText().toString());

            Log.e("Finishing up", "Return dat intent");
            returnIntent.putExtras(extras);

            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
    }
}
