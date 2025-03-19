package com.vt.lab1;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class A1 extends MainActivity {
    boolean isName, isSurname, isPoints;
    EditText et_name, et_surname, et_points;
    Button b_points, b_action;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_a1);

        et_name = findViewById(R.id.activity_a1_ediText_name);
        et_surname = findViewById(R.id.activity_a1_ediText_surname);
        et_points = findViewById(R.id.activity_a1_ediText_points);
        b_points = findViewById(R.id.activity_a1_button_points);
        b_action = findViewById(R.id.activity_a1_b_action);

        et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                isName = false;
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() > 0) isName = true;
                else isName = false;
            }
        });
        et_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b) testB();
            }
        });
        et_surname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() > 0) isSurname = true;
                else isSurname = false;
            }
        });
        et_surname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b) testB();
            }
        });
        et_points.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                isPoints = false;
                if(editable.length() > 0) {
                    try {
                        int p = Integer.parseInt(editable.toString());
                        if(p>=5 && p<=15) isPoints = true;
                        testB();
                    } catch (NumberFormatException nfe){
                        toast("!NIE INT!");
                    }
                }
            }
        });
        et_points.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b) testB();
            }
        });

        b_points.setOnClickListener((view -> {
            intent.setClass(this, A2.class);
            intent.putExtra("points", Integer.parseInt(et_points.getText().toString()));
            startActivity(intent);
        }));

        intent.setClass(this, MainActivity.class);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            // Get the average grade from Activity A2
            float average = data.getFloatExtra("average", 0);

            // Show the average grade
            String resultMessage = "Średnia: " + average;
            toast(resultMessage);

            // Show the appropriate button based on the average
            if (average >= 3) {
                b_action.setText("Super");
                b_action.setOnClickListener();
                //TODO
            } else {
                b_action.setText(".");
                b_action.setOnClickListener();
                //TODO
            }
        }
    }


    private void testB(){
        if(isName && isSurname && isPoints){
            b_points.setVisibility(View.VISIBLE);
        } else {
            String outp = "Niepoprawne: ";
            if(!isName){
                outp += "imie ";
                et_name.setError("IMIIIEEE");
            }
            if(!isSurname){
                outp += "nazwisko ";
                et_surname.setError("NAZZZWISKOOO");
            }
            if(!isPoints){
                outp += "ocena ";
                et_points.setError("OOOOOOOCENAAAAA");
            }
            toast(outp);
            b_points.setVisibility(View.GONE);
        }
    }
}
