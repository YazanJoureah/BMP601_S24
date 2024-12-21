package com.example.helloworld;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import model.DatabaseHelper;


public class MainActivity extends AppCompatActivity {

    private TextView addSalesButton;
    private TextView repButton;
    private TextView salesReport;
    private TextView commissionReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        initializeViews();
        setOnClickListeners();
        applyWindowInsets();
    }

    private void initializeViews() {
        addSalesButton = findViewById(R.id.addSalesButton);
        repButton = findViewById(R.id.representativeButton);
        salesReport = findViewById(R.id.salesReport);
        commissionReport = findViewById(R.id.commissionReport);
        ImageView homeButton = findViewById(R.id.home_btn);

        homeButton.setOnClickListener(v -> navigateTo(RepresentativeView.class));
    }

    private void setOnClickListeners() {
        repButton.setOnClickListener(v -> navigateTo(RepresentativeView.class));
        addSalesButton.setOnClickListener(v -> navigateTo(Sales_View.class));
        salesReport.setOnClickListener(v -> navigateTo(SalesReport_View.class));
        commissionReport.setOnClickListener(v -> navigateTo(CommissionReport_View.class));
    }

    private void navigateTo(Class<?> destination) {
        Intent intent = new Intent(MainActivity.this, destination);
        startActivity(intent);
    }

    private void applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
