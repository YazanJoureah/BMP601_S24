package com.example.helloworld;

      import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;

      import android.annotation.SuppressLint;
      import android.content.Intent;
      import android.content.pm.PackageManager;
      import android.graphics.Bitmap;
      import android.graphics.Color;
      import android.net.Uri;
      import android.os.Bundle;
      import android.provider.MediaStore;
      import android.widget.ArrayAdapter;
      import android.widget.Button;
      import android.widget.EditText;
      import android.widget.ImageView;
      import android.widget.Spinner;
      import android.widget.TableLayout;
      import android.widget.TableRow;
      import android.widget.TextView;

      import androidx.annotation.NonNull;
      import androidx.appcompat.app.AppCompatActivity;
      import androidx.core.app.ActivityCompat;
      import androidx.core.content.ContextCompat;

      import controller.RepresentativeController;
      import model.Region;
      import model.SalesRepresentative;
      import controller.RegionController;

      import java.io.IOException;
      import java.util.ArrayList;
      import java.util.List;
      import android.Manifest;

public class RepresentativeView extends AppCompatActivity {
    private EditText editTextName, editTextPhoneNumber, editTextEmail;
    private Spinner spinnerRegion;
    private TableLayout tableLayout;
    private Button buttonAddRepresentative;
    private RepresentativeController representativeController;
    private RegionController regionController;
    private List<SalesRepresentative> AllRepresentative=new ArrayList<>();
    private SalesRepresentative currentRep= new SalesRepresentative();
    private boolean add_update=true;//True is add , false is update
    private static final int PICK_IMAGE = 1;
    private ImageView imageViewRepresentative; // Add this line
    private Button buttonSelectImage; // Add this line
    private Bitmap representativeImage; // Add this line
    private Uri imageUri;
    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_representative_view);
        // Check for permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_READ_EXTERNAL_STORAGE);
        }

        editTextName = findViewById(R.id.editTextName);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        editTextEmail = findViewById(R.id.editTextEmail);
        spinnerRegion = findViewById(R.id.spinnerRegion);
        tableLayout=findViewById(R.id.tableLayout);
        buttonAddRepresentative = findViewById(R.id.buttonAddRepresentative);
        representativeController = new RepresentativeController(this);
        regionController=new RegionController(this);
        imageViewRepresentative = findViewById(R.id.imageViewRepresentative); // Initialize ImageView
        buttonSelectImage = findViewById(R.id.buttonSelectImage); // Initialize Button

        buttonSelectImage.setOnClickListener(v -> openGallery()); // Set click listener for image selection
        // Populate the spinner with region data
        List<Region> regions = regionController.getRegions();
        AllRepresentative=representativeController.getRepresentatives();
        ArrayAdapter<Region> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, regions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRegion.setAdapter(adapter);
        updateTable();
        buttonAddRepresentative.setOnClickListener(v -> {
            if (add_update) {
                addRepresentative();
            } else {
                updateRepresentative(currentRep);
            }

        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, you can access the external storage
            } else {
                // Permission denied, show a message to the user
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            try {
                representativeImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                imageViewRepresentative.setImageBitmap(representativeImage); // Set the selected image to ImageView
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void addRepresentative() {

        String name = editTextName.getText().toString();
        String phoneNumber = editTextPhoneNumber.getText().toString();
        String email = editTextEmail.getText().toString();
        // Get the selected region
        int regionID = spinnerRegion.getSelectedItemPosition()+1;
        SalesRepresentative representative = new SalesRepresentative();
        representative.setName(name);
        representative.setPhoneNumber(phoneNumber);
        representative.setEmail(email);
        // Check if imageUri is not null before setting it
        if (imageUri != null) {
            representative.setImageUri(imageUri); // Store the image URI as a string
        } else {
            representative.setImageUri(null); // Handle case where no image is selected
        }
        representative.setRegionID(regionID);
        representativeController.addRepresentative(representative);
        AllRepresentative=representativeController.getRepresentatives();
        updateTable();
    }

    private void updateRepresentative(SalesRepresentative representative) {

        String name = editTextName.getText().toString();
        String phoneNumber = editTextPhoneNumber.getText().toString();
        String email = editTextEmail.getText().toString();
// Get the selected region
        int regionID = spinnerRegion.getSelectedItemPosition()+1;        representative.setName(name);
        representative.setPhoneNumber(phoneNumber);
        representative.setEmail(email);
        // Check if imageUri is not null before setting it
        if (imageUri != null) {
            representative.setImageUri(imageUri); // Store the image URI as a string
        } else {
            representative.setImageUri(null); // Handle case where no image is selected
        }
        representative.setRegionID(regionID);
        representativeController.updateRepresentative(representative);
        AllRepresentative=representativeController.getRepresentatives();
        updateTable();
    }

    private void deleteRepresentative(SalesRepresentative currentRepresentative) {
        if (currentRepresentative.getRepresentativeID() == -1) return; // No representative to delete
        representativeController.deleteRepresentative(currentRepresentative.getRepresentativeID());
        AllRepresentative=representativeController.getRepresentatives();
        updateTable();
    }

    private void updateTable(){
        tableLayout.removeViewsInLayout(0,tableLayout.getChildCount());
        for (SalesRepresentative rep:AllRepresentative) {
            TableRow row =new TableRow(this);
            TextView textNameView =new TextView(this);
            textNameView.setText(rep.getName());
            textNameView.setTextColor(Color.BLACK);
            TextView textRegionView =new TextView(this);
            textRegionView.setText(String.valueOf(regionController.getRegionById(rep.getRegionID())));
            textRegionView.setTextColor(Color.BLACK);
            Button buttonActionUpdate = getButton(rep);
            Button buttonActionDelete =new Button(this);
            buttonActionDelete.setText("Delete");
            buttonActionDelete.setOnClickListener(V->deleteRepresentative(rep));
            row.addView(textNameView);
            row.addView(textRegionView);
            row.addView(buttonActionUpdate);
            row.addView(buttonActionDelete);
            tableLayout.addView(row);
        }
    }

    @NonNull
    private Button getButton(SalesRepresentative rep) {
        Button buttonActionUpdate = new Button(this);
        buttonActionUpdate.setText("Update");
        buttonActionUpdate.setOnClickListener(v -> {
            add_update = false;
            currentRep = rep;
            buttonAddRepresentative.setText("Update Representative");
            editTextName.setText(rep.getName());
            editTextEmail.setText(rep.getEmail());
            editTextPhoneNumber.setText(rep.getPhoneNumber());
            spinnerRegion.setSelection(rep.getRegionID() - 1);

            // Set the representative's image in the ImageView
            Uri imageUri = rep.getImageUri();
            if (imageUri != null) {
                try {
                    representativeImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    imageViewRepresentative.setImageBitmap(representativeImage); // Set the selected image to ImageView
                } catch (Exception e) {
                    e.printStackTrace();
                    imageViewRepresentative.setImageResource(R.drawable.default_image); // Set a default image if there's an error
                }
            } else {
                imageViewRepresentative.setImageResource(R.drawable.default_image); // Set a default image if no image is available
            }
        });
        return buttonActionUpdate;
    }
}
