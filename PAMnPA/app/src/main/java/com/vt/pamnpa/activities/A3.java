package com.vt.pamnpa.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
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

    private ElementViewModel mPhoneViewModel;
    private ElementRV phonesRV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isMenuEnabled = true;
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_a3);

        rv_phones = findViewById(R.id.activity_a3_rv_phones);

        phonesRV = new ElementRV(this);
        rv_phones.setLayoutManager(new LinearLayoutManager(this));
        rv_phones.setAdapter(phonesRV);

        mPhoneViewModel = new ViewModelProvider(this).get(ElementViewModel.class);
        mPhoneViewModel.getAllElements().observe(this, elements -> {
            toast(elements.size()+" db entries");
            phonesRV.setElementList(elements);
        });

        intent.setClass(this, MainActivity.class);
    }

    @Override
    protected void onActivityResult(ActivityResult result) {}

    @Override
    protected void drawMenu(Menu menu, int menuRes) {
        super.drawMenu(menu,R.menu.room);
    }
}