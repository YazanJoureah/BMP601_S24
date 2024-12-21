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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import controller.RepresentativeController;
import model.Region;
import model.SalesRepresentative;
import controller.RegionController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.Manifest;

public class RepresentativeView extends AppCompatActivity {
    // Constants
    private static final int PICK_IMAGE = 1;
    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 100;

    // Controllers
    private RepresentativeController representativeController;
    private RegionController regionController;

    // UI Elements
    private Button buttonAddRepresentative;
    private Button buttonSelectImage;
    private EditText editTextEmail;
    private EditText editTextName;
    private EditText editTextPhoneNumber;
    private ImageView imageViewRepresentative;
    private Spinner spinnerRegion;
    private TableLayout tableLayout;

    // Data
    private Bitmap representativeImage;
    private List<SalesRepresentative> allRepresentatives = new ArrayList<>();
    private SalesRepresentative currentRep = new SalesRepresentative();
    private boolean isAddMode = true; // True for add, false for update

    // Uri for image
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_representative_view);
        checkPermissions();
        initializeViews();
        setupControllers();
        setupSpinner();
        setupButtonListeners();
        applyWindowInsets();
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_READ_EXTERNAL_STORAGE);
        }
    }

    private void initializeViews() {
        editTextName = findViewById(R.id.editTextName);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        editTextEmail = findViewById(R.id.editTextEmail);
        spinnerRegion = findViewById(R.id.spinnerRegion);
        tableLayout = findViewById(R.id.tableLayout);
        buttonAddRepresentative = findViewById(R.id.buttonAddRepresentative);
        imageViewRepresentative = findViewById(R.id.imageViewRepresentative);
        buttonSelectImage = findViewById(R.id.buttonSelectImage);
    }

    private void setupControllers() {
        representativeController = new RepresentativeController(this);
        regionController = new RegionController(this);
        allRepresentatives = representativeController.getRepresentatives();
    }

    private void setupSpinner() {
        List<Region> regions = regionController.getRegions();
        ArrayAdapter<Region> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, regions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRegion.setAdapter(adapter);
        updateTable();
    }

    private void setupButtonListeners() {
        buttonSelectImage.setOnClickListener(v -> openGallery());
        buttonAddRepresentative.setOnClickListener(v -> {
            if (isAddMode) {
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
            loadRepresentativeImage();
        }
    }

    private void loadRepresentativeImage() {
        try {
            representativeImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            imageViewRepresentative.setImageBitmap(representativeImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addRepresentative() {
        SalesRepresentative representative = createRepresentativeFromInput();
        representativeController.addRepresentative(representative);
        updateAllRepresentatives();
    }

    private void updateRepresentative(SalesRepresentative representative) {
        SalesRepresentative updatedRepresentative = createRepresentativeFromInput();
        representative.setRepresentativeID(updatedRepresentative.getRepresentativeID()); // Preserve ID
        representativeController.updateRepresentative(representative);
        updateAllRepresentatives();
    }

    private void deleteRepresentative(SalesRepresentative currentRepresentative) {
        if (currentRepresentative.getRepresentativeID() == -1)
            return; // No representative to delete
        representativeController.deleteRepresentative(currentRepresentative.getRepresentativeID());
        updateAllRepresentatives();
    }

    private SalesRepresentative createRepresentativeFromInput() {
        String name = editTextName.getText().toString();
        String phoneNumber = editTextPhoneNumber.getText().toString();
        String email = editTextEmail.getText().toString();
        int regionID = spinnerRegion.getSelectedItemPosition() + 1;

        SalesRepresentative representative = new SalesRepresentative();
        representative.setName(name);
        representative.setPhoneNumber(phoneNumber);
        representative.setEmail(email);
        representative.setImageUri(imageUri); // Set image URI directly
        representative.setRegionID(regionID);

        return representative;
    }

    private void updateAllRepresentatives() {
        allRepresentatives = representativeController.getRepresentatives();
        updateTable();
    }

    private void updateTable() {
        tableLayout.removeViewsInLayout(0, tableLayout.getChildCount());
        for (SalesRepresentative rep : allRepresentatives) {
            TableRow row = createTableRow(rep);
            tableLayout.addView(row);
        }
    }

    private TableRow createTableRow(SalesRepresentative rep) {
        TableRow row = new TableRow(this);

        TextView textNameView = createTextView(rep.getName());
        TextView textRegionView = createTextView(String.valueOf(regionController.getRegionById(rep.getRegionID())));

        Button buttonActionUpdate = getButton(rep);
        Button buttonActionDelete = createDeleteButton(rep);

        row.addView(textNameView);
        row.addView(textRegionView);
        row.addView(buttonActionUpdate);
        row.addView(buttonActionDelete);

        return row;
    }

    private TextView createTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextColor(Color.BLACK);
        return textView;
    }

    private Button createDeleteButton(SalesRepresentative rep) {
        Button buttonActionDelete = new Button(this);
        buttonActionDelete.setText("Delete");
        buttonActionDelete.setOnClickListener(v -> deleteRepresentative(rep));
        return buttonActionDelete;
    }

    @NonNull
    private Button getButton(SalesRepresentative rep) {
        Button buttonActionUpdate = new Button(this);
        buttonActionUpdate.setText("Update");
        buttonActionUpdate.setOnClickListener(v -> populateFieldsForUpdate(rep));
        return buttonActionUpdate;
    }

    private void populateFieldsForUpdate(SalesRepresentative rep) {
        isAddMode = false;
        currentRep = rep;
        buttonAddRepresentative.setText("Update Representative");

        editTextName.setText(rep.getName());
        editTextEmail.setText(rep.getEmail());
        editTextPhoneNumber.setText(rep.getPhoneNumber());
        spinnerRegion.setSelection(rep.getRegionID() - 1);

        setRepresentativeImage(rep.getImageUri());
    }

    private void setRepresentativeImage(Uri imageUri) {
        if (imageUri != null) {
            try {
                representativeImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                imageViewRepresentative.setImageBitmap(representativeImage);
            } catch (Exception e) {
                e.printStackTrace();
                setDefaultImage();
            }
        } else {
            setDefaultImage();
        }
    }

    private void setDefaultImage() {
        imageViewRepresentative.setImageResource(R.drawable.default_image);
    }

    private void applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.representative_View), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
