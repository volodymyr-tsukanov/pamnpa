package com.vt.pamnpa.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.vt.pamnpa.R;
import com.vt.pamnpa.adapters.ElementRV;
import com.vt.pamnpa.adapters.ElementRV;
import com.vt.pamnpa.room.ElementViewModel;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class A3 extends BaseActivity {
    RecyclerView rv_phones;
    FloatingActionButton fab;

    private ElementViewModel mPhoneViewModel;
    private ElementRV phonesRV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isMenuEnabled = true;
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_a3);
        setTitle("Phones");

        rv_phones = findViewById(R.id.activity_a3_rv_phones);
        fab = findViewById(R.id.activity_a3_fab);

        phonesRV = new ElementRV(this);
        rv_phones.setLayoutManager(new LinearLayoutManager(this));
        rv_phones.setAdapter(phonesRV);

        mPhoneViewModel = new ViewModelProvider(this).get(ElementViewModel.class);
        mPhoneViewModel.getAllElements().observe(this, elements -> {
            toast(elements.size()+" db entries");
            phonesRV.setElementList(elements);
        });

        fab.setOnClickListener((view)->{
            intent.setClass(this, A3Add.class);
            arl.launch(intent);
        });

        intent.setClass(this, MainActivity.class);
    }

    @Override
    protected void onActivityResult(ActivityResult result) {
        switch(result.getResultCode()) {
            case 1: //added successfully
                phonesRV.notifyItemInserted(phonesRV.getItemCount());
                //phonesRV.setElementList(mPhoneViewModel.getAllElements().getValue());
                break;
            case 0: //canceled
                toast("No items added");
                break;
            default:
                toast("Wrong result code");
        }
    }

    @Override
    protected void drawMenu(Menu menu, int menuRes) {
        super.drawMenu(menu,R.menu.room);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_room_del) {
            mPhoneViewModel.deleteAll();
            phonesRV.notifyDataSetChanged();
        }
        return super.onOptionsItemSelected(item);
    }
}