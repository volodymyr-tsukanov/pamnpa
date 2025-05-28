package com.vt.pamnpa.activities;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import com.vt.pamnpa.adapters.FileInfo;
import com.vt.pamnpa.async.ShortTask;
import com.vt.pamnpa.room.Element;
import com.vt.pamnpa.room.ElementViewModel;

import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class A4 extends BaseActivity {
    EditText et_url;
    TextView tv_size, tv_type, tv_downl;
    ProgressBar pb_downl;
    Button b_get, b_downl;

    ShortTask st;

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

        et_url.setText("https://cdn.kernel.org/pub/linux/kernel/v6.x/linux-6.12.24.tar.xz");

        st = new ShortTask();
        b_get.setOnClickListener((l)->{
            String url = getUrl();
            if(url.isEmpty()){
                return;
            }
            st.getFileInfo(url, new ShortTask.ResultCallback<FileInfo>() {
                @Override
                public void onSuccess(FileInfo result) {
                    tv_size.setText(result.getSize() + "b");
                    tv_type.setText(result.getType());
                }

                @Override
                public void onProgress(int progress) {}

                @Override
                public void onError(Throwable throwable) {
                    toast("Error: " + throwable.getMessage());
                }
            });
            toast("get info");
        });
        b_downl.setOnClickListener((l)->{
            String url = getUrl();
            if(url.isEmpty()){
                return;
            }

            st.downloadFile(url, new ShortTask.ResultCallback<FileInfo>() {
                @Override
                public void onSuccess(FileInfo result) {
                    toast("Download finished. Size: " + result.getSize() + " bytes");
                }

                @Override
                public void onProgress(int progress) {
                    pb_downl.setProgress(progress);
                }

                @Override
                public void onError(Throwable throwable) {
                    toast("Download error: " + throwable.getMessage());
                }
            });
            toast("dwnld");
        });

        intent.setClass(this, MainActivity.class);
    }

    @Override
    protected void onDestroy() {
        if(st!=null){
            st.shutdown();
        }
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(ActivityResult result) {
        switch(result.getResultCode()) {
            default:
                toast("Wrong result code");
        }
    }


    private String getUrl(){
        String url = et_url.getText().toString().trim();
        if(url.isBlank() || !url.startsWith("http")) return "";
        return url;
    }
}