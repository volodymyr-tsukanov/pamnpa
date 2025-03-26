package com.vt.pamnpa.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.MenuRes;
import androidx.appcompat.app.AppCompatActivity;
import com.vt.pamnpa.R;


public abstract class BaseActivity  extends AppCompatActivity {
    protected boolean isMenuEnabled = true;
    protected String sharedPreferencesName = "dt";
    Intent intent;
    ActivityResultLauncher<Intent> arl;
    SharedPreferences sp;

    // ACTIVITY
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        intent = new Intent(this, A1.class);
        intent.setAction(Intent.ACTION_VIEW);

        arl = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::onActivityResult);

        sp = this.getSharedPreferences(sharedPreferencesName,MODE_PRIVATE);

        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/
    }

    // MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(isMenuEnabled) drawMenu(menu,-1);
        return isMenuEnabled;
    }
    protected void drawMenu(Menu menu, @MenuRes int menuRes){
        if(menuRes == -1) menuRes = R.menu.main;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(menuRes, menu);
    }


    // INTENT
    protected abstract void onActivityResult(ActivityResult result);


    // UTILS
    protected void toast(String msg){
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.show();
    }
}
