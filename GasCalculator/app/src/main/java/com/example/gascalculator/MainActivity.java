package com.example.gascalculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText etPrice, etDistance, etMpg;
    private RadioGroup rgAC;
    private Spinner spRoadType;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etPrice = findViewById(R.id.etPrice);
        etDistance = findViewById(R.id.etDistance);
        etMpg = findViewById(R.id.etMpg);
        rgAC = findViewById(R.id.rgAC);
        spRoadType = findViewById(R.id.spRoadType);
        tvResult = findViewById(R.id.tvResult);
        Button btnCalculate = findViewById(R.id.btnCalculate);

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculate();
            }
        });
    }

    private void calculate() {
        double price;
        int distance;
        int mpg;

        try {
            price = Double.parseDouble(etPrice.getText().toString().trim());
            distance = Integer.parseInt(etDistance.getText().toString().trim());
            mpg = Integer.parseInt(etMpg.getText().toString().trim());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter data for all fields.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (mpg <= 0) {
            Toast.makeText(this, "Please enter data for all fields.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        int checkedId = rgAC.getCheckedRadioButtonId();
        if (checkedId == -1) {
            Toast.makeText(this, "Please choose if you need AC or not",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        double modifier = 0.0;

        if (checkedId == R.id.rbYes) {
            modifier += 0.15;
        }

        String roadType = spRoadType.getSelectedItem().toString();
        if (roadType.equals("City")) {
            modifier += 0.15;
        } else if (roadType.equals("Mixed")) {
            modifier += 0.10;
        }

        double gallonsNeeded = ((double) distance / mpg) * (1 + modifier);
        double totalCost = (gallonsNeeded * price) * 2;

        tvResult.setText(String.format("The cost of a round trip will be approximately $%.2f",
                totalCost));
    }
}