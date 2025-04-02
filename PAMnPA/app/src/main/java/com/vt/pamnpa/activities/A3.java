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

import com.vt.pamnpa.R;
import com.vt.pamnpa.adapters.ElementListAdapter;
import com.vt.pamnpa.room.ElementViewModel;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class A3 extends BaseActivity {
    private ElementViewModel mElementViewModel;
    private ElementListAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isMenuEnabled = false;
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_a3);

        intent.setClass(this, MainActivity.class);
    }

    @Override
    protected void onActivityResult(ActivityResult result) {}
}