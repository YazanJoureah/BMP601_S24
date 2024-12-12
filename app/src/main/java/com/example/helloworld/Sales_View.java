package com.example.helloworld;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

import controller.SalesController;
import model.Region;
import controller.RegionController;
import model.Sales;
import model.SalesRepresentative;
import controller.RepresentativeController;

public class Sales_View extends AppCompatActivity {

    private Spinner spinnerRep;
    private RepresentativeController representativeController;
    private int currentRepID;
    private SalesRepresentative currentRep;
    private int currentRepRegionID;
    private RegionController regionController;
    private SalesController salesController;
    private List<SalesRepresentative> AllRepresentative=new ArrayList<>();
    private List<Region> AllRegions=new ArrayList<>();
    private TextView repName,repEmail,repPhone,repArea;
    private TableLayout salesTable;
    private Spinner monthSpinner;
    private Spinner yearSpinner;
    private int month;
    private int year;
    private long totalCommission = 0;
    private TextView commissionView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sales_view);

        initializeViews();
        setupControllers();
        setupSpinner();
        populateSalesTable();
        setupWindowInsets();
        calculateCommission();
        setupSubmitButton();
    }

    private void initializeViews() {
        repName = findViewById(R.id.repName);
        repEmail = findViewById(R.id.repEmail);
        repPhone = findViewById(R.id.repPhone);
        repArea = findViewById(R.id.repArea);
        spinnerRep = findViewById(R.id.representativeSpinner);
        salesTable = findViewById(R.id.salesTable);
        monthSpinner = findViewById(R.id.monthSpinner);
        yearSpinner = findViewById(R.id.yearSpinner);
        commissionView=findViewById(R.id.commissionView);
    }

    private void setupControllers() {
        representativeController = new RepresentativeController(this);
        regionController = new RegionController(this);
        salesController=new SalesController(this);
    }

    private void setupSpinner() {
        AllRepresentative = representativeController.getRepresentatives();

        // Add a default item
        AllRepresentative.add(0, new SalesRepresentative("--Representative Name", -1)); // Assuming constructor takes name and ID

        ArrayAdapter<SalesRepresentative> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, AllRepresentative);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRep.setAdapter(adapter);

        spinnerRep.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SalesRepresentative selectedRep = (SalesRepresentative) parent.getItemAtPosition(position);
                if (selectedRep.getRepresentativeID() != -1) { // Check if it's not the default item
                    updateRepresentativeDetails(selectedRep);
                } else {
                    clearRepresentativeDetails(); // Clear details if default is selected
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                currentRepID = -1;
            }
        });



// Create an array of months
        String[] months = new String[] {
                "Select Month", "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        };

// Create an array of years (e.g., from 2020 to 2030)
        String[] years = new String[] {
                "Select Year", "2020", "2021", "2022", "2023", "2024", "2025",
                "2026", "2027", "2028", "2029", "2030"
        };

// Create ArrayAdapter for months
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, months);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthAdapter);

// Create ArrayAdapter for years
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, years);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);

        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getSelectedItemPosition()!=0){
                    month = parent.getSelectedItemPosition();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                month = 0;
            }
        });

        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getSelectedItemPosition()!=0){
                year = Integer.parseInt((String) parent.getItemAtPosition(position));

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                year = 0;
            }
        });
    }


    private void clearRepresentativeDetails() {
        repName.setText("");
        repEmail.setText("");
        repPhone.setText("");
        repArea.setText("");
    }


    private void updateRepresentativeDetails(SalesRepresentative selectedRep) {
        currentRepID = selectedRep.getRepresentativeID();
        currentRep = representativeController.getRepresentativeById(currentRepID);
        Region selectedRepRegion = regionController.getRegionById(selectedRep.getRegionID());
        currentRepRegionID = selectedRep.getRegionID();
        repName.setText(currentRep.getName());
        repEmail.setText(currentRep.getEmail());
        repPhone.setText(currentRep.getPhoneNumber());
        repArea.setText(selectedRepRegion.getRegionName());
    }

    private void setupSubmitButton() {
        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(v -> submitSales());
    }

    private void populateSalesTable() {
        AllRegions = regionController.getRegions();
        for (Region region : AllRegions) {
            addRegionToSalesTable(region);
        }
    }

    private void addRegionToSalesTable(Region region) {
        TableRow row = new TableRow(this);

        TextView textNameView = new TextView(this);
        textNameView.setText(region.getRegionName());

        EditText editSales = new EditText(this);
        editSales.setOnFocusChangeListener((v, hasFocus) -> calculateCommission());

        // Store the RegionID as a tag in the EditText
        editSales.setTag(region.getRegionID());

        row.addView(textNameView);
        row.addView(editSales);
        salesTable.addView(row);
    }


    private void setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.Sales_View), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void calculateCommission() {


        for (int i = 0; i < salesTable.getChildCount(); i++) {
            TableRow row = (TableRow) salesTable.getChildAt(i);
            EditText editSales = (EditText) row.getChildAt(1); // Assuming sales input is the second child
            String salesInput = editSales.getText().toString();

            if (!salesInput.isEmpty()) {
                long sales = Long.parseLong(salesInput);
                totalCommission += (long) calculateIndividualCommission(sales,i+1);
            }
        }

        commissionView.setText(""+totalCommission);
        displayTotalCommission(totalCommission);
    }

    private double calculateIndividualCommission(long sales,int regionID) {
        if(currentRepRegionID==regionID){
            if (sales > 100000000) {
                return (100000000 * 0.05) + ((sales - 100000000) * 0.07);
            } else {
                return sales * 0.05;
            }
        }else {
            return sales * 0.03;
        }
    }

    private void displayTotalCommission(long totalCommission) {
        Toast.makeText(this, "Total Commission: " + totalCommission, Toast.LENGTH_LONG).show();
    }

    private void submitSales() {
        for (int i = 0; i < salesTable.getChildCount(); i++) {
            TableRow row = (TableRow) salesTable.getChildAt(i);
            EditText editSales = (EditText) row.getChildAt(1); // Assuming sales input is the second child

            if (!editSales.getText().toString().isEmpty()) {
                Sales sale = new Sales();
                sale.setYear(year);
                sale.setMonth(month);
                sale.setRepresentativeID(currentRepID);
                sale.setAmount(Float.parseFloat(editSales.getText().toString()));
                sale.setRegionID((int) editSales.getTag()); // Get the RegionID from the tag

                if (!salesController.addSale(sale)) {
                    editSales.setBackgroundColor(Color.RED); // Use Color.RED for error indication
                }
            }
        }
    }
}

