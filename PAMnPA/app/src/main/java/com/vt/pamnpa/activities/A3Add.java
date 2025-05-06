package com.vt.pamnpa.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.view.Menu;
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
import com.vt.pamnpa.room.Element;
import com.vt.pamnpa.room.ElementViewModel;

public class A3Add extends BaseActivity {
    private ElementViewModel mPhoneViewModel;
    EditText edit_mr, edit_ml, edit_vn, edit_se;
    Button b_se, b_cancel, b_save;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isMenuEnabled = false;
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_a3_add);

        edit_mr = findViewById(R.id.activity_a3_add_mr_edit);
        edit_ml = findViewById(R.id.activity_a3_add_ml_edit);
        edit_vn = findViewById(R.id.activity_a3_add_vn_edit);
        edit_se = findViewById(R.id.activity_a3_add_se_edit);
        b_se = findViewById(R.id.activity_a3_add_b_se);
        b_cancel = findViewById(R.id.activity_a3_add_b_cancel);
        b_save = findViewById(R.id.activity_a3_add_b_save);

        mPhoneViewModel = new ViewModelProvider(this).get(ElementViewModel.class);

        b_se.setOnClickListener((view)->{
            String site = edit_se.getText().toString();
            if(checkWebAddress(site)){
                Intent intentWeb = new Intent(Intent.ACTION_VIEW, Uri.parse(site));
                startActivity(intentWeb);
            } else {
                toast("ignoring me? try to enter web address properly");
            }
        });

        b_cancel.setOnClickListener((view)->{
            setResult(0,intent);
            finish();
        });

        b_save.setOnClickListener((view)->{
            String manufacturer = edit_mr.getText().toString(), model = edit_ml.getText().toString(), site = edit_se.getText().toString();
            int version = Integer.parseInt(edit_vn.getText().toString());   //!dangerous lazy
            if(manufacturer.isBlank()){
                edit_mr.setError("Fill me");
                return;
            }
            if(model.isBlank()){
                edit_ml.setError("Fill me, pls");
                return;
            }
            if(!checkWebAddress(site)){
                edit_se.setError("!?");
                return;
            }
            if(version>15) {
                edit_vn.setError("time traveller? tell me stock market insides pls");
                return;
            }
            Element el = new Element(manufacturer,model,version,site);
            mPhoneViewModel.insert(el);
            setResult(1,intent);
            finish();
        });

        intent.setClass(this, A3.class);
    }

    @Override
    protected void onActivityResult(ActivityResult result) {}

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle("Add new phone");
    }

    private  boolean checkWebAddress(String inputText) {
        return !inputText.isBlank() && inputText.startsWith("http");
    }
}