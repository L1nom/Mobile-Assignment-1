package com.example.mob1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        create spinner objects, set spinner values to our strings array created in stings.xml
        Spinner interest_spinner = findViewById(R.id.interest_spinner);
        ArrayAdapter<CharSequence> interest_adapter = ArrayAdapter.createFromResource(this, R.array.rate_values, android.R.layout.simple_spinner_item);
        interest_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        interest_spinner.setAdapter(interest_adapter);

        Spinner time_spinner = findViewById(R.id.term_spinner);
        ArrayAdapter<CharSequence> time_adapter = ArrayAdapter.createFromResource(this, R.array.period_values, android.R.layout.simple_spinner_item);
        time_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        time_spinner.setAdapter(time_adapter);

        EditText principal = (EditText) findViewById(R.id.principal_value);

//        use button to fetch input values
        Button calculationBtn;
        calculationBtn = (Button) findViewById(R.id.calc_button);
        calculationBtn.setOnClickListener(new View.OnClickListener() {
//            set button listener. Grab entered value and spinner values
            @Override
            public void onClick(View view) {
                String prpl = principal.getText().toString();
//                extract only decimal value with regex
                String interest_rate = interest_spinner.getSelectedItem().toString().replaceAll("[ a-zA-Z%^]","");
                String term_rate = time_spinner.getSelectedItem().toString();

//              make sure principal is populated with a value
                if (TextUtils.isEmpty(prpl)){
                    principal.setError("Enter Principal Amount");
                    principal.requestFocus();
                    return;
                }
//                Here we can set the actual calculation values, convert from string to flot
                float input_principal = Float.parseFloat(prpl);
                float input_interest = Float.parseFloat(interest_rate);
                float input_term = Float.parseFloat(term_rate);

//                Call math functions for calculations
                float P = princ(input_principal);
                float I = monthlyRate(input_interest);
                float N = months(input_term);
                float emi = divdnt(P, I, N);
                float int_payed = totalInterest(I, N, emi, P);

//                Set messages for our pop up message to display
                String message1 = "Your monthly payments equates to:\n$" + emi;
                String message2 = "Your interest rate is: " + input_interest + "%, calculated over a duration of: " + input_term + " years.";
                String message3 = "Your total interest paid over the term duration is: $" + int_payed;
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Your EMI Summary");
                builder.setMessage(message1 + "\n" + "\n" + message2 + "\n" + "\n" + message3);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
//                    allow user to exit pop up menu
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();
            }
        });

    }
//    write math functions here

//    return float value of principal
    public float princ(float p){
        return (float)(p);
    }
//    return our interest
    public float monthlyRate(float i){
        return (float)((i / 12 / 100));
    }
//    return our time in months
    public float months(float y){
        return (float)(y * 12);
    }
//    formula for calculating EMI, using principal investment, interest rate, and period of time
    public float divdnt(float p, float rate, float month){
        return (float) (Math.round(p * rate * Math.pow((1 + rate), month)/(Math.pow((1 + rate), month) - 1)* 100 ) / 100);
    }
//    for loop to find out how much interest the user paid
//    need to use amorization formula to figure this out
    public float totalInterest(float rate, float term, float emi, float principal){
        float totalPay = 0;
        float totalInt = 0;
        float balance = principal;
        for(int i = 0; i < Math.ceil(term); i++) {
            totalPay += emi - balance * rate;
            totalInt += balance * rate;
            balance -= emi - balance * rate;


        }
        return (float) (Math.round(totalInt * 100) / 100);
    }

}