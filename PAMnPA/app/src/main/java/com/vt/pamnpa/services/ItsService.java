package com.vt.pamnpa.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vt.pamnpa.R;
import com.vt.pamnpa.activities.A4;
import com.vt.pamnpa.adapters.FileInfo;
import com.vt.pamnpa.async.ProgressEvent;
import com.vt.pamnpa.async.ShortTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class ItsService extends Service {
    private static final String CHANNEL_ID = "download_channel";
    private static final int NOTIFICATION_ID = 1001;

    private MutableLiveData<ProgressEvent> progressLiveData = new MutableLiveData<>(null);


    public class ItsServiceBinder extends Binder {
        public LiveData<ProgressEvent> getProgressEvent() {
            return progressLiveData;
        }
        // Expose a method to start download from the activity
        public void startDownload(String url) {
            ItsService.this.startDownload(url);
        }
    }
    private final IBinder binder = new ItsServiceBinder();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        createNotificationChannel();
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    private void createNotificationChannel() {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "File Download Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
    }

    void showNotification(ProgressEvent event) {
        Intent intent = new Intent(this, A4.class);
        intent.putExtra("KEY_PROGRESS", event);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(A4.class);
        stackBuilder.addNextIntent(intent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Downloading File")
                .setContentIntent(pendingIntent)
                .setOnlyAlertOnce(true)
                .setOngoing(event.result == ProgressEvent.IN_PROGRESS);

        if (event.result == ProgressEvent.IN_PROGRESS) {
            builder.setContentText("Downloaded " + event.progress + " / " + event.total + " bytes")
                    .setProgress(event.total, event.progress, false);
        } else if (event.result == ProgressEvent.OK) {
            builder.setContentText("Download complete")
                    .setProgress(0, 0, false)
                    .setOngoing(false);
        } else if (event.result == ProgressEvent.ERROR) {
            builder.setContentText("Download failed")
                    .setProgress(0, 0, false)
                    .setOngoing(false);
        }

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID, builder.build());
    }

    public void updateProgress(ProgressEvent event) {
        progressLiveData.postValue(event);
        showNotification(event);
    }


    private void startDownload(String fileUrl) {
        new Thread(() -> {
            final int bufferLength = 12288;
            HttpsURLConnection connection = null;
            try {
                URL url = new URL(fileUrl);
                connection = (HttpsURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                int length = connection.getContentLength();
                String type = connection.getContentType();

                InputStream is = connection.getInputStream();
                File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                if (!downloadsDir.exists()) {
                    downloadsDir.mkdirs();
                }

                String fileName = new File(url.getPath()).getName();
                if (fileName.isEmpty()) {
                    fileName = "downloaded_file";
                }

                File outputFile = new File(downloadsDir, fileName);
                FileOutputStream os = new FileOutputStream(outputFile);

                byte[] buffer = new byte[bufferLength];
                int bytesRead, totalBytesRead = 0;

                while ((bytesRead = is.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                    totalBytesRead += bytesRead;

                    int progress = (int) ((totalBytesRead / (float) length) * 100);
                    updateProgress(new ProgressEvent(progress, totalBytesRead, length));
                }

                os.flush();
                os.close();
                is.close();

                updateProgress(new ProgressEvent(bufferLength, totalBytesRead, 0));
            } catch (Exception e) {
                updateProgress(new ProgressEvent(ProgressEvent.ERROR, 0, 0));
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }).start();
    }

}
