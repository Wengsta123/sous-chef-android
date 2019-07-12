package a4720.virginia.cs.uva.sous_chef;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import a4720.virginia.cs.uva.sous_chef.APIConnect.RecipeService;
import a4720.virginia.cs.uva.sous_chef.Objects.InnerClasses.*;

import a4720.virginia.cs.uva.sous_chef.APIConnect.RecipeService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.HEAD;

public class RecipeDetailsActivity extends AppCompatActivity{
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.recipe_details, container, false);}

    // fields
    TextView recipeName;
    TextView totalTime;
    TextView sourceDisplayName;
    TextView numberOfServings;
    TextView ingredients;
    ImageView imageView;
    TextView notes;
    Button directionsButton;
    Button addToCookbookButton;
  
    public Integer primaryKey;
    public Recipe currentAPIRecipe;

    //attribution things
    ImageView attributionImage;
    TextView attributionURL;
    TextView attributionText;

    // buttons
    private ImageButton editButton;
    private ImageButton deleteButton;
    private ImageButton shareRecipeButton;
    private ImageButton takePhotoButton;
    Uri file;
    String currentPhotoPath;

    // permissions
    static final int TAKE_PHOTO_PERMISSION = 1;
    static final int REQUEST_TAKE_PHOTO = 2;
    static final int PICK_IMAGE_REQUEST = 3;

    // api connection
    private static final String id = "cb47c5e6";
    private static final String key = "134207b176bbda4b3dafd4c9378bd3f2";
    private static final String baseURL = "https://api.yummly.com/v1/api/";

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_details);

        // fields
        recipeName = (TextView) findViewById(R.id.recipeName);
        sourceDisplayName = (TextView) findViewById(R.id.sourceDisplayName);
        imageView = (ImageView) findViewById(R.id.recipeImage);
        totalTime = (TextView) findViewById(R.id.totalTime);
        numberOfServings = (TextView) findViewById(R.id.servings);
        ingredients = (TextView) findViewById(R.id.ingredients);
        notes = (TextView) findViewById(R.id.notes);
        directionsButton = (Button) findViewById(R.id.directions);
        editButton = (ImageButton) findViewById(R.id.edit_button);
        deleteButton = (ImageButton) findViewById(R.id.delete_button);
        shareRecipeButton = (ImageButton) findViewById(R.id.shareRecipe);
        takePhotoButton = (ImageButton) findViewById(R.id.takePhoto);
        addToCookbookButton = (Button) findViewById(R.id.add_to_cookbook);


        // attribution
        attributionImage = findViewById(R.id.attribution_logo);
        attributionText = findViewById(R.id.attribution_text);
        attributionURL = findViewById(R.id.attribution_url);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
//        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
//        toolbar.setTitle("Add Item");

//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        shareRecipeButton = findViewById(R.id.shareRecipe);
        takePhotoButton = findViewById(R.id.takePhoto);
        shareRecipeButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
//                emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                emailIntent.setType("message/rfc822");
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check out this recipe from Sous-Chef!");
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, currentAPIRecipe.shareToEmail());
                startActivity(emailIntent);
                }
        });

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            takePhotoButton.setEnabled(false);
            takePhotoButton.setClickable(false);
            ActivityCompat.requestPermissions(this, new String[] { android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE }, TAKE_PHOTO_PERMISSION);
        }

        getIncomingIntent();
    }

    // load from personal or online database
    private void getIncomingIntent() {
        Intent intent = getIntent();
        if (intent.getStringExtra("primarykey")!= null && intent.getStringExtra("primarykey")!=""){
            Log.e("Primary key", intent.getStringExtra("primarykey"));
            addToCookbookButton.setVisibility(View.INVISIBLE);
            takePhotoButton.setVisibility(View.VISIBLE);
            editButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.VISIBLE);
            shareRecipeButton.setVisibility(View.VISIBLE);

            primaryKey=Integer.parseInt(intent.getStringExtra("primarykey"));
        }
        if (Integer.parseInt(intent.getStringExtra("primarykey")) != 0){
            Log.e("Primary key", intent.getStringExtra("primarykey"));
            addToCookbookButton.setVisibility(View.INVISIBLE);
            takePhotoButton.setVisibility(View.VISIBLE);
            editButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.VISIBLE);
            shareRecipeButton.setVisibility(View.VISIBLE);
            primaryKey=Integer.parseInt(intent.getStringExtra("primarykey"));
        }
        else{
            addToCookbookButton.setVisibility(View.VISIBLE);
            takePhotoButton.setVisibility(View.INVISIBLE);
            editButton.setVisibility(View.INVISIBLE);
            deleteButton.setVisibility(View.INVISIBLE);
            shareRecipeButton.setVisibility(View.INVISIBLE);
        }
        //retrieved from Find New
        if (intent.getStringExtra("id")!=null && intent.getStringExtra("id")!="") {

            loadFromOnlineDatabase(intent.getStringExtra("id"));
        }
        //retrieved from cookbook
        else{
            Log.e("Recipe id", "Id is:" + intent.getStringExtra(("id")));
            Log.e("Recipe name", "Key is:"+intent.getStringExtra("primarykey"));


            currentAPIRecipe = MainActivity.getDb().recipeDao().getRecipe(primaryKey);
            updateRecipe();
        }
    }

    private void loadFromOnlineDatabase(String recipeId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RecipeService service = retrofit.create(RecipeService.class);
        Call<Recipe> call = service.getRecipe(recipeId, id, key);

        // handle response
        call.enqueue(new Callback<Recipe>() {
            @Override
            public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                Recipe recipe = response.body();
                currentAPIRecipe=recipe;
                Gson gson = new Gson();
                updateRecipe();
            }

            @Override
            public void onFailure(Call<Recipe> call, Throwable t) {
                Log.d("RecipeDetailsActivity", t.getMessage());
            }
        });
    }

    private void updateRecipe() {
        Recipe recipe = currentAPIRecipe;
        recipeName.setText(currentAPIRecipe.name);
        if (currentAPIRecipe.id != null && !currentAPIRecipe.id.isEmpty()) {
            sourceDisplayName.setText(currentAPIRecipe.source.sourceDisplayName);
            try {
                Bitmap bitmap = BitmapFactory.decodeByteArray(recipe.localImageBytes, 0, recipe.localImageBytes.length);
                imageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                Picasso.get().load(currentAPIRecipe.images.get(0).hostedLargeUrl).resize(600, 400).into(imageView);
            }
            setAttributions(currentAPIRecipe.attribution);
            directionsButton.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    String url = currentAPIRecipe.source.sourceRecipeUrl;
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(i);
                }
            });
        }
        else {
            Bitmap bitmap = BitmapFactory.decodeByteArray(recipe.localImageBytes, 0, recipe.localImageBytes.length);
            imageView.setImageBitmap(bitmap);
        }

        if (recipe.id == null || recipe.id.isEmpty()) {
            directionsButton.setVisibility(View.INVISIBLE);
        }
        if (recipe.notes != null && !recipe.notes.isEmpty())
            notes.setVisibility(View.VISIBLE);
            notes.setText("Notes:\n" + recipe.notes);
        if (recipe.totalTime != null && !recipe.totalTime.isEmpty()) totalTime.setText("Time: " + recipe.totalTime);
        if (recipe.numberOfServings != 0) numberOfServings.setText("Servings: " + recipe.numberOfServings);
        for (int i = 0; i < recipe.ingredientLines.size(); i++) {
            ingredients.append("\n" + recipe.ingredientLines.get(i));
        }
    }

    private void setAttributions(Attribution attribution) {
        attributionText.setText(attribution.text);
        attributionURL.setText(attribution.url);
        Picasso.get().load(attribution.logo).resize(150,50).into(attributionImage);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         // suffix
                storageDir      // directory
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    public void takePicture(View view) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
                Log.i("CameraActivity", "IOException");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(cameraIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Add code here to handle results from both taking a picture or pulling
        // from the image gallery.

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            try {
                Bitmap imageBitmap = (Bitmap) MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(currentPhotoPath));
                imageView.setImageBitmap(imageBitmap);

                // convert to byte array for storage
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
                currentAPIRecipe.setLocalImageBytes(byteArrayOutputStream.toByteArray());
                MainActivity.getDb().recipeDao().updateLocalImage(primaryKey, byteArrayOutputStream.toByteArray());
                finish();

                Intent intent = new Intent(RecipeDetailsActivity.this, MainActivity.class);
                startActivity(intent);

            } catch (IOException e){
                e.printStackTrace();
            }
        }
        else if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == TAKE_PHOTO_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhotoButton.setEnabled(true);
                takePhotoButton.setClickable(true);
            }
        }
    }
    public void deleteRecipe(View view){
        Log.e("Primary Key", Integer.toString(primaryKey));
        Recipe dRecipe= MainActivity.getDb().recipeDao().getRecipe(primaryKey);
        MainActivity.getDb().recipeDao().delete(dRecipe);
        finish();
        Intent intent = new Intent(RecipeDetailsActivity.this,MainActivity.class);
        startActivity(intent);
    }
    public void editRecipeActivity(View view){
        Intent intent = new Intent(RecipeDetailsActivity.this, EditRecipeActivity.class);
        intent.putExtra("primaryKey", Integer.toString(primaryKey));
        startActivity(intent);
    }
    public void addToCookbook(View view){
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        currentAPIRecipe.setLocalImageBytes(ImageConverter.getBytes(bitmap));
        MainActivity.getDb().recipeDao().insertAll(currentAPIRecipe);
        finish();
        Intent intent = new Intent(RecipeDetailsActivity.this,MainActivity.class);
        startActivity(intent);
    }
}
