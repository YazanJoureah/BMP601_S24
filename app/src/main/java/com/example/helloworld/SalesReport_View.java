package com.example.helloworld;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

import controller.RegionController;
import controller.RepresentativeController;
import controller.SalesController;
import model.Region;
import model.Sales;
import model.SalesRepresentative;

public class SalesReport_View extends AppCompatActivity {


    private Spinner spinnerRep;
    private RepresentativeController representativeController;
    private SalesRepresentative currentRep;
    private int currentRepID;
    private int currentRepRegionID;
    private RegionController regionController;
    private SalesController salesController;
    private List<SalesRepresentative> AllRepresentative = new ArrayList<>();
    private TableLayout salesTable;
    private Spinner monthSpinner;
    private Spinner yearSpinner;
    private int month;
    private int year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sales_report_view);

        initializeViews();
        setupControllers();
        setupSpinner();
        getSales();
        setupWindowInsets();


    }

    private void initializeViews() {
        spinnerRep = findViewById(R.id.representativeSpinner);
        salesTable = findViewById(R.id.salesTable);
        monthSpinner = findViewById(R.id.monthSpinner);
        yearSpinner = findViewById(R.id.yearSpinner);
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
                currentRepID=selectedRep.getRepresentativeID();
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

    private void getSales(){
         List<Sales> salesList=new ArrayList<>();
         salesList=salesController.getSalesByRepresentativeAndDate(currentRepID,month,year);
        for (Sales sale:salesList
             ) {
            addToSalesTable(sale);
        }
    }

    private void addToSalesTable(Sales sale) {
        TableRow row = new TableRow(this);
        TextView textMonthView = new TextView(this);
        textMonthView.setText(sale.getMonth()+"");
        TextView textYearView = new TextView(this);
        textYearView.setText(sale.getYear()+"");
      /*  TextView textAreaView = new TextView(this);
        textMonthView.setText(regionController.getRegionById(sale.getArea()));*/
        TextView textAmountView = new TextView(this);
        textAmountView.setText(sale.getAmount()+"");
        row.addView(textMonthView);
        row.addView(textYearView);
        row.addView(textAmountView);
        salesTable.addView(row);
    }


    private void setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.SalesReport_View), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

}