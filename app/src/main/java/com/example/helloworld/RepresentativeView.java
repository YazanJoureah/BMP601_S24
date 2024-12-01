package com.example.helloworld;

      import android.annotation.SuppressLint;
      import android.os.Bundle;
      import android.widget.ArrayAdapter;
      import android.widget.Button;
      import android.widget.EditText;
      import android.widget.Spinner;
      import android.widget.TableLayout;
      import android.widget.TableRow;
      import android.widget.TextView;

      import androidx.annotation.NonNull;
      import androidx.appcompat.app.AppCompatActivity;

      import controller.RepresentativeController;
      import model.Region;
      import model.SalesRepresentative;
      import controller.RegionController;

      import java.util.ArrayList;
      import java.util.List;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_representative_view);
        editTextName = findViewById(R.id.editTextName);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        editTextEmail = findViewById(R.id.editTextEmail);
        spinnerRegion = findViewById(R.id.spinnerRegion);
        tableLayout=findViewById(R.id.tableLayout);
        buttonAddRepresentative = findViewById(R.id.buttonAddRepresentative);
        representativeController = new RepresentativeController(this);
        regionController=new RegionController(this);
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

    @SuppressLint("SetTextI18n")
    private void updateTable(){
        tableLayout.removeViewsInLayout(1,tableLayout.getChildCount()-1);
        for (SalesRepresentative rep:AllRepresentative) {
            TableRow row =new TableRow(this);
            TextView textNameView =new TextView(this);
            textNameView.setText(rep.getName());
            TextView textRegionView =new TextView(this);
            textRegionView.setText(""+rep.getRegionID());
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
        Button buttonActionUpdate =new Button(this);
        buttonActionUpdate.setText("Update");
        buttonActionUpdate.setOnClickListener(v->{
            add_update=false;
            currentRep= rep;
            buttonAddRepresentative.setText("Update Representative");
            editTextName.setText(rep.getName());
            editTextEmail.setText(rep.getEmail());
            editTextPhoneNumber.setText(rep.getPhoneNumber());
            spinnerRegion.setSelection(rep.getRegionID()-1);
        });
        return buttonActionUpdate;
    }
}
