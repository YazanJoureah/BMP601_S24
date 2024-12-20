package com.example.helloworld;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import controller.CommissionController;
import controller.RegionController;
import controller.RepresentativeController;
import controller.SalesController;
import model.Commission;
import model.Region;
import model.Sales;
import model.SalesRepresentative;

public class CommissionReport_View extends AppCompatActivity {

    private RepresentativeController representativeController;
    private CommissionController commissionController;

    private TableLayout commissionTable;
    private Spinner spinnerRep;
    private Spinner monthSpinner;
    private Spinner yearSpinner;
    private int currentRepID;
    private int month;
    private int year;
    // Create an array of months
    String[] MONTHS = new String[] {
            "Select Month", "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
    };

    // Create an array of years (e.g., from 2020 to 2030)
    String[] YEARS = new String[] {
            "Select Year", "2020", "2021", "2022", "2023", "2024", "2025",
            "2026", "2027", "2028", "2029", "2030"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_commission_report_view);

        initializeViews();
        setupControllers();
        setupSpinners();
        setupWindowInsets();
    }

    private void initializeViews() {
        spinnerRep = findViewById(R.id.representativeSpinner);
        commissionTable = findViewById(R.id.commissionTable);
        monthSpinner = findViewById(R.id.monthSpinner);
        yearSpinner = findViewById(R.id.yearSpinner);
    }

    private void setupControllers() {
        representativeController = new RepresentativeController(this);
        commissionController=new CommissionController(this);
    }

    private void setupSpinners() {
        setupRepresentativeSpinner();
        setupMonthSpinner();
        setupYearSpinner();
    }

    private void setupRepresentativeSpinner() {
        List<SalesRepresentative> allRepresentatives = representativeController.getRepresentatives();
        allRepresentatives.add(0, new SalesRepresentative("--Representative Name", -1));

        ArrayAdapter<SalesRepresentative> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, allRepresentatives);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRep.setAdapter(adapter);

        spinnerRep.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SalesRepresentative selectedRep = (SalesRepresentative) parent.getItemAtPosition(position);
                currentRepID = selectedRep.getRepresentativeID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                currentRepID = -1;
            }
        });
    }

    private void setupMonthSpinner() {
        setupSpinner(monthSpinner, MONTHS, new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                month = (position != 0) ? position : 0;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No action needed
            }
        });
    }

    private void setupYearSpinner() {
        setupSpinner(yearSpinner, YEARS, new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    year = Integer.parseInt((String) parent.getItemAtPosition(position));
                    getCommissions();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No action needed
            }
        });
    }

    private void setupSpinner(Spinner spinner, String[] items, AdapterView.OnItemSelectedListener listener) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(listener);
    }

    private void getCommissions(){
        List<Commission> commissionsList;
        commissionsList=commissionController.getCommissionsByRepresentativeAndDate(currentRepID,month,year);
        for (Commission commission:commissionsList
        ) {
            addToCommissionsTable(commission);
        }
    }

    private void addToCommissionsTable(Commission commission) {
        TableRow row = new TableRow(this);
        row.setBackgroundColor(Color.WHITE); // Set row background color
        row.setPadding(8, 8, 8, 8); // Set padding for the row

        TextView textMonthView = new TextView(this);
        textMonthView.setText(MONTHS[commission.getMonth()]);
        textMonthView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        TextView textYearView = new TextView(this);
        textYearView.setText(String.valueOf(commission.getYear()));
        textYearView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        TextView textAmountView = new TextView(this);
        textAmountView.setText(String.valueOf((long)commission.getAmount()));
        textAmountView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        // Add TextViews to the row
        row.addView(textMonthView);
        row.addView(textYearView);
        row.addView(textAmountView);

        // Add the row to the sales table
        commissionTable.addView(row);
    }


    private void setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.CommissionReport_View), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}