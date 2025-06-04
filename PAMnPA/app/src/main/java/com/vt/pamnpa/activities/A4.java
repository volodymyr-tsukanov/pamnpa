package com.vt.pamnpa.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.vt.pamnpa.R;
import com.vt.pamnpa.adapters.FileInfo;
import com.vt.pamnpa.async.ProgressEvent;
import com.vt.pamnpa.async.ShortTask;
import com.vt.pamnpa.services.ItsService;

public class A4 extends BaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    EditText et_url;
    TextView tv_size, tv_type, tv_downl;
    ProgressBar pb_downl;
    Button b_get, b_downl;

    boolean serviceBound = false;
    LiveData<ProgressEvent> progressEventLiveData;
    ItsService.ItsServiceBinder binder;
    A4ViewModel viewModel;

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

        viewModel = new ViewModelProvider(this).get(A4ViewModel.class);
        viewModel.progress.observe(this, this::updateProgressRedundant);

        b_get.setOnClickListener((l) -> {
            // Just get file info directly here (can be moved to service if desired)
            String url = getUrl();
            if (url.isEmpty()) {
                return;
            }
            // For now, call getFileInfo directly in ShortTask (as before)
            new Thread(() -> {
                ShortTask st = new ShortTask();
                st.getFileInfo(url, new ShortTask.ResultCallback<FileInfo>() {
                    @Override
                    public void onSuccess(FileInfo result) {
                        runOnUiThread(() -> {
                            tv_size.setText((result.getSize() / 1024 / 1024 + 1) + "MB");
                            tv_type.setText(result.getType());
                        });
                    }
                    @Override
                    public void onProgress(int progress) {}
                    @Override
                    public void onError(Throwable throwable) {
                        runOnUiThread(() -> toast("Error: " + throwable.getMessage()));
                    }
                });
            }).start();
            toast("get info");
        });
        b_downl.setOnClickListener((l) -> {
            String url = getUrl();
            if (url.isEmpty()) return;
            if (serviceBound && binder != null) {
                binder.startDownload(url);
                toast("Downloading...");
            } else {
                toast("Service not bound");
            }
        });

        // Bind service
        Intent myServiceIntent = new Intent(this, ItsService.class);
        bindService(myServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE);

        // Restore progress from notification if present
        ProgressEvent eventFromNotification = getIntent().getParcelableExtra("KEY_PROGRESS");
        if (eventFromNotification != null) {
            updateProgressRedundant(eventFromNotification);
        }
    }

    @Override
    protected void onActivityResult(ActivityResult result) {
    }

    @Override
    protected void onDestroy() {
        if (serviceBound && progressEventLiveData != null) {
            progressEventLiveData.removeObservers(this);
        }
        if (serviceBound) {
            unbindService(serviceConnection);
            serviceBound = false;
        }
        super.onDestroy();
    }

    private String getUrl() {
        String url = et_url.getText().toString().trim();
        if (url.isBlank() || !url.startsWith("http")) return "";
        return url;
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            serviceBound = true;
            binder = (ItsService.ItsServiceBinder) iBinder;
            progressEventLiveData = binder.getProgressEvent();
            progressEventLiveData.observe(A4.this, event -> {
                if (event != null) {
                    viewModel.progress.setValue(event);
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            if (progressEventLiveData != null) {
                progressEventLiveData.removeObservers(A4.this);
            }
            serviceBound = false;
            binder = null;
        }
    };

    private void updateProgressRedundant(ProgressEvent event) {
        if (event == null) return;
        tv_downl.setText(String.format("%d / %d bytes", event.progress, event.total));
        pb_downl.setMax(event.total);
        pb_downl.setProgress(event.progress);
    }

    public static class A4ViewModel extends ViewModel {
        public final MutableLiveData<ProgressEvent> progress = new MutableLiveData<>();
    }
}
