package com.hfad.currencyconveter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.gson.JsonObject;
import com.hfad.currencyconveter.retrofit.RetrofitBuilder;
import com.hfad.currencyconveter.retrofit.RetrofitInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    Button button;
    EditText currencyToBeConverted, currencyConverted;
    Spinner convertTo, convertFrom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialization
        currencyConverted = findViewById(R.id.currency_converted);
        currencyToBeConverted = findViewById(R.id.currency_to_be_converted);
        convertTo = findViewById(R.id.convertTo);
        convertFrom = findViewById(R.id.convertFrom);
        button = findViewById(R.id.button);

        //adding functionality
        String [] dropDownList = {"USD", "INR", "EUR", "NZD"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item,dropDownList);
        convertFrom.setAdapter(adapter);
        convertTo.setAdapter(adapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RetrofitInterface retrofitInterface = RetrofitBuilder.getRetrofitInstance().create(RetrofitInterface.class);

                Call<JsonObject> call = retrofitInterface.getExchangeCurrency(convertFrom.getSelectedItem().toString());
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                      JsonObject res = response.body();
                      JsonObject rates = res.getAsJsonObject("rates");
                        Double currency = Double.valueOf(currencyToBeConverted.getText().toString());
                        Double multiplier = Double.valueOf(rates.get(convertTo.getSelectedItem().toString()).toString());
                        Double results = currency * multiplier;
                        currencyConverted.setText(String.valueOf(results));

                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                });
            }
        });
    }
}