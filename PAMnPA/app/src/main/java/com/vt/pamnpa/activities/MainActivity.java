package com.vt.pamnpa.activities;

import android.os.Bundle;

import android.view.MenuItem;
import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.vt.pamnpa.R;

public class MainActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isMenuEnabled = true;
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menu_main_a1){
            toast("Lab1");
            intent.setClass(this,A1.class);
            arl.launch(intent);
            finish();
        }
        if(id == R.id.menu_main_a3){
            toast("Lab3");
            intent.setClass(this,A3.class);
            arl.launch(intent);
            finish();
        }
        if(id == R.id.menu_main_a4){
            toast("Lab4");
            intent.setClass(this,A4.class);
            arl.launch(intent);
            finish();
        }
        if(id == R.id.menu_main_main) {
            toast("MAIN");
            intent.setClass(this, MainActivity.class);
            arl.launch(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(ActivityResult result) {
        toast("Main: "+result.getResultCode());
    }
}