package com.vt.pamnpa.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
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
import com.vt.pamnpa.room.Element;
import com.vt.pamnpa.room.ElementViewModel;

public class A4 extends BaseActivity {
    EditText et_url;
    TextView tv_size, tv_type, tv_downl;
    ProgressBar pb_downl;
    Button b_get, b_downl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isMenuEnabled = false;
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_a4);
        setTitle("Threading");

        et_url = findViewById(R.id.activity_a4_url_edit);
        tv_size = findViewById(R.id.activity_a4_get_size);
        tv_type = findViewById(R.id.activity_a4_get_type);
        tv_downl = findViewById(R.id.activity_a4_downl);
        pb_downl = findViewById(R.id.activity_a4_downl_progres);
        b_get = findViewById(R.id.activity_a4_get_button);
        b_downl = findViewById(R.id.activity_a4_downl_button);

        b_get.setOnClickListener((l)->{
            toast("g");
        });
        b_downl.setOnClickListener((l)->{
            toast("d");
        });

        intent.setClass(this, MainActivity.class);
    }

    @Override
    protected void onActivityResult(ActivityResult result) {
        switch(result.getResultCode()) {
            default:
                toast("Wrong result code");
        }
    }
}