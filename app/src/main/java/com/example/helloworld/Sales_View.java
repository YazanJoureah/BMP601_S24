package com.example.helloworld;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

import controller.CommissionController;
import controller.SalesController;
import model.Commission;
import model.Region;
import controller.RegionController;
import model.Sales;
import model.SalesRepresentative;
import controller.RepresentativeController;

public class Sales_View extends AppCompatActivity {
    // UI Components
private Spinner representativeSpinner;
private Spinner monthSpinner;
private Spinner yearSpinner;
private TextView representativeNameTextView;
private TextView representativeEmailTextView;
private TextView representativePhoneTextView;
private TextView representativeAreaTextView;
private TableLayout salesTableLayout;
private TextView commissionTextView;

// Controllers
private RepresentativeController representativeController;
private RegionController regionController;
private SalesController salesController;
private CommissionController commissionController;

// Current State
private int currentRepresentativeId;
private SalesRepresentative currentRepresentative;
private int currentRepresentativeRegionId;
private long totalCommissionAmount = 0;

// Data Lists
private List<SalesRepresentative> allRepresentatives = new ArrayList<>();
private List<Region> allRegions = new ArrayList<>();

// Date Arrays
private String[] months = new String[] {
                "Select Month", "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        };

// Create an array of years (e.g., from 2020 to 2030)
private String[] years = new String[] {
                "Select Year", "2020", "2021", "2022", "2023", "2024", "2025",
                "2026", "2027", "2028", "2029", "2030"
        };
        
// Date Variables
private int selectedMonth;
private int selectedYear;

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
    setupCalculateButton();
    setupSubmitButton();
}

private void initializeViews() {
    representativeNameTextView = findViewById(R.id.repName);
    representativeEmailTextView = findViewById(R.id.repEmail);
    representativePhoneTextView = findViewById(R.id.repPhone);
    representativeAreaTextView = findViewById(R.id.repArea);
    representativeSpinner = findViewById(R.id.representativeSpinner);
    salesTableLayout = findViewById(R.id.salesTable);
    monthSpinner = findViewById(R.id.monthSpinner);
    yearSpinner = findViewById(R.id.yearSpinner);
    commissionTextView =findViewById(R.id.commissionView);
    }

    private void setupControllers() {
        representativeController = new RepresentativeController(this);
        regionController = new RegionController(this);
        salesController=new SalesController(this);
        commissionController=new CommissionController(this);
    }

    private void setupSpinner() {

// Add a default item
allRepresentatives.add(0, new SalesRepresentative("--Representative Name", -1)); // Assuming constructor takes name and ID
allRepresentatives.addAll(1,representativeController.getRepresentatives());

ArrayAdapter<SalesRepresentative> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, allRepresentatives);
adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
representativeSpinner.setAdapter(adapter);

// Set the default selection to the first item
representativeSpinner.setSelection(0); // Set the default selected item to "--Representative Name"

representativeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        currentRepresentativeId = -1;
    }
});



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
                    selectedMonth = parent.getSelectedItemPosition();
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedMonth = 0;
            }
        });

        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getSelectedItemPosition()!=0){
                    selectedYear = Integer.parseInt((String) parent.getItemAtPosition(position));

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedYear = 0;
            }
        });
    }


    
    private void setupCalculateButton() {
        Button calculateButton = findViewById(R.id.calculateButton);
        calculateButton.setOnClickListener(v -> calculateCommission());
    }
    private void setupSubmitButton() {
        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(v -> submitSales());
    }
    
    private void clearRepresentativeDetails() {

        representativeNameTextView.setText("");
        representativeEmailTextView.setText("");
        representativePhoneTextView.setText("");
        representativeAreaTextView.setText("");
    }


    private void updateRepresentativeDetails(SalesRepresentative selectedRep) {
        currentRepresentativeId = selectedRep.getRepresentativeID();
        currentRepresentative = representativeController.getRepresentativeById(currentRepresentativeId);
        Region selectedRepRegion = regionController.getRegionById(selectedRep.getRegionID());
        currentRepresentativeRegionId = selectedRep.getRegionID();
        representativeNameTextView.setText(currentRepresentative.getName());
        representativeEmailTextView.setText(currentRepresentative.getEmail());
        representativePhoneTextView.setText(currentRepresentative.getPhoneNumber());
        representativeAreaTextView.setText(selectedRepRegion.getRegionName());
    }


    private void populateSalesTable() {
        allRegions = regionController.getRegions();
        for (Region region : allRegions) {
            addRegionToSalesTable(region);
        }
    }

    private void addRegionToSalesTable(Region region) {
    TableRow row = new TableRow(this);

    TextView textNameView = new TextView(this);
    textNameView.setText(region.getRegionName());
    textNameView.setTextColor(Color.BLACK); // Set text color to black

    EditText editSales = new EditText(this);
    editSales.setTag(region.getRegionID()); // Store the RegionID as a tag in the EditText

    // Restrict input to numbers only
    editSales.setInputType(InputType.TYPE_CLASS_NUMBER); // Allow only numeric input
    editSales.setTextColor(Color.BLACK); // Set text color to black

    // Optional: Set a filter to restrict input further (if needed)
    editSales.setFilters(new InputFilter[] { new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; i++) {
                if (!Character.isDigit(source.charAt(i))) {
                    return ""; // Reject non-numeric input
                }
            }
            return null; // Accept input
        }
    }});

    row.addView(textNameView);
    row.addView(editSales);
    salesTableLayout.addView(row);
}


    
    private void calculateCommission() {


        for (int i = 0; i < salesTableLayout.getChildCount(); i++) {
            TableRow row = (TableRow) salesTableLayout.getChildAt(i);
            EditText editSales = (EditText) row.getChildAt(1); // Assuming sales input is the second child
            String salesInput = editSales.getText().toString();

            if (!salesInput.isEmpty()) {
                long sales = Long.parseLong(salesInput);
                totalCommissionAmount += (long) calculateIndividualCommission(sales,i+1);
            }
        }

        commissionTextView.setText(String.valueOf(totalCommissionAmount));
    }

    private double calculateIndividualCommission(long sales,int regionID) {
        if(currentRepresentativeRegionId==regionID){
            if (sales > 100000000) {
                return (100000000 * 0.05) + ((sales - 100000000) * 0.07);
            } else {
                return sales * 0.05;
            }
        }else {
            return sales * 0.03;
        }
    }

private Sales createSaleFromInput(EditText editSales) {
    Sales sale = new Sales();
    sale.setYear(selectedYear);
    sale.setMonth(selectedMonth);
    sale.setRepresentativeID(currentRepresentativeId);
    sale.setAmount(Long.parseLong(editSales.getText().toString()));
    sale.setRegionID((int) editSales.getTag()); // Get the RegionID from the tag
    return sale;
}

      private void processSalesInput() {
          for (int i = 0; i < salesTableLayout.getChildCount(); i++) {
              TableRow row = (TableRow) salesTableLayout.getChildAt(i);
              EditText editSales = (EditText) row.getChildAt(1); // Assuming sales input is the second child
      
              if (!editSales.getText().toString().isEmpty()) {
                  Sales sale = createSaleFromInput(editSales);
                  if (!salesController.addSale(sale)) {
                      editSales.setBackgroundColor(Color.RED); // Indicate error
                  } else {
                      editSales.setBackgroundColor(Color.GREEN); // Indicate success
                  }
              } else {
                  editSales.setBackgroundColor(Color.LTGRAY); // Indicate empty input
              }
          }
      }
    
    private Commission createCommission() {
    Commission commission = new Commission();
    commission.setYear(selectedYear);
    commission.setMonth(selectedMonth);
    commission.setAmount(totalCommissionAmount);
    commission.setRepresentativeID(currentRepresentativeId);
    return commission;
}  
      

private void addNewCommission() {
    Commission commission = createCommission();
    if (!commissionController.addCommission(commission)) {
        commissionTextView.setBackgroundColor(Color.GREEN); // Indicate success
        showToast("Success: Sales and Commission added successfully."); // Popup message for success
    } else {
        commissionTextView.setBackgroundColor(Color.RED); // Indicate error
        showToast("Error: Sales and Commission could not be added."); // Popup message for error

    }
}


private void submitSales() {
  
    if (commissionController.getCommissionsByRepresentativeAndDate(
            currentRepresentativeId,
            selectedMonth,
            selectedYear).isEmpty()) {
        processSalesInput();
        addNewCommission();
    } else {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.notification);
        mediaPlayer.start();
        ShowWarningDialog(totalCommissionAmount); // Show warning dialog if commission exists
    }
}    
    


private void ShowWarningDialog(long newCommission) {
    
    new AlertDialog.Builder(this)
        .setTitle("Commission Exists")
        .setMessage("A commission of value: " + newCommission +
                " already exists for " + selectedMonth + " " + selectedYear +
                ".\n\nWould you like to replace the existing commission with this new value?\n" +
                "Please note that replacing it will overwrite the previous commission.")
        .setPositiveButton("Yes", (dialog, which) -> {
            updateSalesAndCommission();
        })
        .setNegativeButton("No", (dialog, which) -> {
            dialog.dismiss();
        })
        .setIcon(android.R.drawable.ic_dialog_alert) // Optional: Add an icon for better visual indication
        .show();
}



private void updateSalesAndCommission() {
    for (int i = 0; i < salesTableLayout.getChildCount(); i++) {
        TableRow row = (TableRow) salesTableLayout.getChildAt(i);
        EditText editSales = (EditText) row.getChildAt(1); // Assuming sales input is the second child

        if (!editSales.getText().toString().isEmpty()) {
            Sales sale = createSaleFromInput(editSales);
            if (!salesController.updateSale(sale)) {
                editSales.setBackgroundColor(Color.GREEN); // Indicate success
            } else {
                editSales.setBackgroundColor(Color.RED); // Indicate error
            }
        }
    }

    Commission commission = createCommission();
    if (!commissionController.updateCommission(commission)) {
        commissionTextView.setBackgroundColor(Color.GREEN); // Indicate success

    } else {
        commissionTextView.setBackgroundColor(Color.RED); // Indicate error

    }
}

// Helper method to show a Toast message
      private void showToast(String message) {
          Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
      }

private void setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.Sales_View), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    
    

}

