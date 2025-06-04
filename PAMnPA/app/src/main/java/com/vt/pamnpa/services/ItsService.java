package com.vt.pamnpa.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vt.pamnpa.R;
import com.vt.pamnpa.activities.A4;
import com.vt.pamnpa.async.ProgressEvent;


public class ItsService extends Service {
    private static final String CHANNEL_ID = "download_channel";
    private static final int NOTIFICATION_ID = 1001;

    public final static String TAG = ItsService.class.getSimpleName();
    //obiekt LiveData do przekazywana informacji o postępie do aktywności
    private MutableLiveData<ProgressEvent> progressLiveData =
            new MutableLiveData<>(null);
    //Binder definiuje metody, które można wywoływać z zewnątrz np. z aktywności
    public class ItsServiceBinder extends Binder {
        public LiveData<ProgressEvent> getProgressEvent() {
            return progressLiveData;
        }
    }
    private final IBinder binder = new ItsServiceBinder();
    //metoda odpowiedzialna za zwrócenie Bindera, w przypadku usług niezwiązanych
    //(unbounded) musi zwracać null
    @Override
    public IBinder onBind(Intent intent) {
        createNotificationChannel();
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "File Download Channel",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
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
                .setSmallIcon(R.drawable.ic_launcher_foreground) // Your icon
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


    // This should be called from the download logic
    public void updateProgress(int downloadedBytes, int totalBytes, int status) {
        ProgressEvent event = new ProgressEvent(downloadedBytes, totalBytes, status);
        progressLiveData.postValue(event);
        showNotification(event);
    }
}
