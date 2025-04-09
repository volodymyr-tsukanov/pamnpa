package com.vt.pamnpa.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vt.pamnpa.R;
import com.vt.pamnpa.adapters.ElementRV;
import com.vt.pamnpa.adapters.ElementRV;
import com.vt.pamnpa.room.ElementViewModel;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class A3 extends BaseActivity {
    RecyclerView rv_phones;
    Button b_goBack;

    private ElementViewModel mPhoneViewModel;
    private ElementRV phonesRV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isMenuEnabled = false;
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_a3);
        if (getSupportActionBar() != null) getSupportActionBar().hide();

        b_goBack = findViewById(R.id.activity_a3_b_goBack);
        rv_phones = findViewById(R.id.activity_a3_rv_phones);

        b_goBack.setOnClickListener(view->{
            intent.setClass(this, MainActivity.class);
            arl.launch(intent);
            finish();
        });

        phonesRV = new ElementRV(this);
        rv_phones.setLayoutManager(new LinearLayoutManager(this));
        rv_phones.setAdapter(phonesRV);

        mPhoneViewModel = new ViewModelProvider(this).get(ElementViewModel.class);
        mPhoneViewModel.getAllElements().observe(this, elements -> {
                    phonesRV.setElementList(elements);
                });

        intent.setClass(this, MainActivity.class);
    }

    @Override
    protected void onActivityResult(ActivityResult result) {}
}