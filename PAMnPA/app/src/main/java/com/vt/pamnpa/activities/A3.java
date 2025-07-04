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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.vt.pamnpa.R;
import com.vt.pamnpa.adapters.ElementRV;
import com.vt.pamnpa.adapters.ElementRV;
import com.vt.pamnpa.room.Element;
import com.vt.pamnpa.room.ElementViewModel;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class A3 extends BaseActivity {
    RecyclerView rv_phones;
    FloatingActionButton fab;

    private ElementViewModel mPhoneViewModel;
    private ElementRV phonesRV;
    private Element el_selected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isMenuEnabled = true;
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_a3);
        setTitle("Phones");

        rv_phones = findViewById(R.id.activity_a3_rv_phones);
        fab = findViewById(R.id.activity_a3_fab);

        phonesRV = new ElementRV(this, element -> {
            el_selected = element;
            toast(element.getModel());
        });
        rv_phones.setLayoutManager(new LinearLayoutManager(this));
        rv_phones.setAdapter(phonesRV);

        mPhoneViewModel = new ViewModelProvider(this).get(ElementViewModel.class);
        mPhoneViewModel.getAllElements().observe(this, elements -> {
            toast(elements.size()+" db entries");
            phonesRV.setElementList(elements);
        });
        ItemTouchHelper ith = getTouchHelper();
        ith.attachToRecyclerView(rv_phones);

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
                phonesRV.notifyItemInserted(phonesRV.getItemCount()-1);
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


    private ItemTouchHelper getTouchHelper() {
        ItemTouchHelper.Callback rv_callback = new ItemTouchHelper.SimpleCallback(0,  /*ItemTouchHelper.UP | ItemTouchHelper.DOWN | */ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT){
            @Override
            public boolean onMove(RecyclerView recyclerView,
                                  RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                /*int fromPos = viewHolder.getAdapterPosition();
                int toPos = target.getAdapterPosition();

                // Swap in adapter
                phonesRV.swapElements(fromPos, toPos);*/
                return false;  // true if moved
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = viewHolder.getAdapterPosition();
                phonesRV.removeElement(pos);
                //?delete fr
            }
        };
        ItemTouchHelper ith = new ItemTouchHelper(rv_callback);
        return ith;
    }
}