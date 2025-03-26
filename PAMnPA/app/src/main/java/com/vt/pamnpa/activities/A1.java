package com.vt.pamnpa.activities;

import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.vt.pamnpa.R;

public class A1 extends BaseActivity {
    boolean isName, isSurname, isPoints, isOwingMoney = true;
    EditText et_name, et_surname, et_points;
    Button b_points, b_action;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isMenuEnabled = false;
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_a1);
        if (getSupportActionBar() != null) getSupportActionBar().hide();

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
        et_name.setOnFocusChangeListener((view, b) -> {
            if(!b) testB();
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
        et_surname.setOnFocusChangeListener((view, b) -> {
            if(!b) testB();
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
        et_points.setOnFocusChangeListener((view, b) -> {
            if(!b) testB();
        });
        b_points.setOnClickListener(view -> {
            intent.setClass(this, A2.class);
            intent.putExtra("points", Integer.parseInt(et_points.getText().toString()));
            arl.launch(intent);
        });
        b_action.setOnClickListener(view->{
            if(isOwingMoney){
                toast("przygotuj 166.66 zl za warunek");
            } else {
                toast("Zaliczone");
            }
        });

        intent.setClass(this, MainActivity.class);
    }

    @Override
    protected void onActivityResult(ActivityResult result) {
        switch(result.getResultCode()){
            case 69:
            // Get the average grade from Activity A2
            float average = result.getData().getFloatExtra("average", 0);

            // Show the average grade
            String resultMessage = "Średnia: " + average;
            toast(resultMessage);

            b_action.setVisibility(View.VISIBLE);
            if (average > 3.5) {
                isOwingMoney = false;
                b_action.setText("Super");
            } else {
                isOwingMoney = true;
                b_action.setText("Super?\uD83D\uDE08");
            }
            break;
            case 6:
                toast("Nie wpisano ocen");
                break;
            default:
                toast("error code 0xc000019c: Buy new iPhone to solve this error");
        }
    }


    void testB(){
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