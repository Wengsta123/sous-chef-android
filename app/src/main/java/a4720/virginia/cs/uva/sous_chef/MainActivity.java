package a4720.virginia.cs.uva.sous_chef;

import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import a4720.virginia.cs.uva.sous_chef.Fragments.RecipeFragmentManager;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    static AppDatabase db;
    private ArrayList<Recipe> recipes;
    SensorManager sensorManager;
    private Sensor mAccelerometer;
    private static final float SHAKE_THRESHOLD_GRAVITY = 5F;
    private long lastUpdate;
    private float last_x=0;
    private float last_y=0;
    private float last_z=0;

    static final int ADD_ACTIVITY_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        RecipeFragmentManager manager = new RecipeFragmentManager(this, getSupportFragmentManager());
        viewPager.setAdapter(manager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "student").allowMainThreadQueries().build();
    }

    public static AppDatabase getDb() {
        return db;
    }

    private Recipe createRecipeFromBundle(Bundle extras) {
        String ingredients=extras.getString("ingredients");
        ArrayList<String> ingredientsList= new ArrayList<String>(Arrays.asList(ingredients.split("\\n")));
        String servingsString=extras.getString("servings");
        Integer servings=0;
        Boolean saved = true;
        if(servingsString != null && !servingsString.isEmpty()){
            servings=Integer.parseInt(servingsString);
        }
        else{
        }
        return new Recipe(extras.getString("title"),
                ingredientsList,
                extras.getString("time"),
                servings,
                extras.getString("notes"), saved);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_CANCELED) {
        }
        if (requestCode == ADD_ACTIVITY_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle extras = data.getExtras();
                addRecipeToDatabase(createRecipeFromBundle(extras));

            }
        }
    }

//    @Override
//    protected void onResume(){
//        super.onResume();
//    }

    public void addRecipeToDatabase(Recipe recipe) {
        db.recipeDao().insertAll(recipe);
        finish();
        startActivity(getIntent());
    }
    public void deleteFromDatabase(Recipe recipe){
        db.recipeDao().delete(recipe);
        finish();
        startActivity(getIntent());
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        lastUpdate=System.currentTimeMillis();
    }

    @Override
    public void onPause() {
        sensorManager.unregisterListener(this);
        super.onPause();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        long curTime = System.currentTimeMillis();
        if ((curTime - lastUpdate) > 200) {
            long diffTime = (curTime - lastUpdate);
            lastUpdate = curTime;

            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            //speed is actually acceleration but easier to type speed
            //Calculate acceleration vector
            float speed = (float) Math.sqrt(Math.pow((x - last_x), 2) + Math.pow(y - last_y, 2) + Math.pow(z - last_z, 2)) / diffTime * 1000 / 9.81F; //speed in units of gravity
            speed = speed - 1;//subtract gravity force from speed
            if (speed > SHAKE_THRESHOLD_GRAVITY) {
                if(db.recipeDao().getAll().size()>1) {
                    Log.e("shake", "shake it off, shake it off...");
                    Toast.makeText(MainActivity.this, "Shake detected! Randomizing...", Toast.LENGTH_SHORT).show();
                    Random random = new Random();
                    int randomNum = random.nextInt(db.recipeDao().countRecipes() - 1);
                    List<Recipe> recipes = db.recipeDao().getAll();
                    Recipe randomRecipe = recipes.get(randomNum);
                    Intent intent = new Intent(MainActivity.this, RecipeDetailsActivity.class);
                    intent.putExtra("recipe", Parcels.wrap(randomRecipe));
                    intent.putExtra("primarykey", Integer.toString(randomRecipe.getUid()));
                    try {
                        intent.putExtra("saved", randomRecipe.getSaved());
                    } catch (NullPointerException e) {

                    }
                    try {
                        intent.putExtra("id", randomRecipe.getId());
                    } catch (NullPointerException e) {

                    }
                    startActivity(intent);
                }
                else{
                    Toast.makeText(MainActivity.this, "Can't randomize with less than 2 items in cookbook.", Toast.LENGTH_SHORT).show();
                }
            }
            //Keep track of last_x/y/z to detect changes of direction
            last_x = x;
            last_y = y;
            last_z = z;
        }
    }

}
