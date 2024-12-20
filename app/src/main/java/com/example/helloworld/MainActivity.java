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


        ImageView homeButton = findViewById(R.id.home_btn);
        addSalesButton=findViewById(R.id.addSalesButton);
        repButton=findViewById(R.id.representativeButton);
        salesReport=findViewById(R.id.salesReport);
        commissionReport=findViewById(R.id.commissionReport);
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RepresentativeView.class);
            startActivity(intent);
        });

        repButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RepresentativeView.class);
            startActivity(intent);
        });

        addSalesButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Sales_View.class);
            startActivity(intent);
        });

        salesReport.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SalesReport_View.class);
            startActivity(intent);
        });

        commissionReport.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CommissionReport_View.class);
            startActivity(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


    }


}