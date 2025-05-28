package com.vt.pamnpa.async;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;

import com.vt.pamnpa.adapters.FileInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.*;

import javax.net.ssl.HttpsURLConnection;


public class ShortTask {
    private static final String TAG = ShortTask.class.getSimpleName();
    private final ExecutorService executorService;
    private final Handler mainThreadHandler;
    private Future<?> future;

    public ShortTask() {
        // utworzenie puli 2 wątków
        executorService = Executors.newFixedThreadPool(2);
        // utworzenie Handlera do wysyłania zadań do wątku UI
        mainThreadHandler = new Handler(Looper.getMainLooper());
    }

    // wywołanie zwrotne do przekazywania wyników
    public interface ResultCallback<T> {
        void onSuccess(T result);

        void onProgress(int progress);

        void onError(Throwable throwable);
        // można dodać więcej metod np. do przekazywania informacji o postępie
    }

    /**
     * Get file info (like content length and content type)
     */
    public Future<?> getFileInfo(String fileUrl, ResultCallback<FileInfo> callback) {
        cancelPreviousTask();

        Callable<FileInfo> task = () -> {
            HttpsURLConnection connection = null;
            try {
                URL url = new URL(fileUrl);
                connection = (HttpsURLConnection) url.openConnection();
                connection.setRequestMethod("HEAD");
                connection.connect();

                int length = connection.getContentLength();
                String type = connection.getContentType();
                return new FileInfo(length, type);
            } catch (Exception e) {
                throw e;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        };

        return submitTask(task, callback);
    }

    /**
     * Download the file and return FileInfo (you can extend to save file too)
     */
    public Future<?> downloadFile(String fileUrl, ResultCallback<FileInfo> callback) {
        cancelPreviousTask();
        Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        Callable<FileInfo> task = () -> {
            final int bufferLength = 12288;
            HttpsURLConnection connection = null;
            try {
                URL url = new URL(fileUrl);
                connection = (HttpsURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                int length = connection.getContentLength();
                String type = connection.getContentType();

                // For example, just reading the input stream and ignoring it
                try (InputStream is = connection.getInputStream()) {
                    File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    if (!downloadsDir.exists()) {
                        downloadsDir.mkdirs(); // create folder if not exists
                    }

                    String fileName = new File(url.getPath()).getName();
                    if (fileName.isEmpty()) {
                        fileName = "downloaded_file";
                    }

                    File outputFile = new File(downloadsDir, fileName);
                    try (FileOutputStream os = new FileOutputStream(outputFile)) {
                        byte[] buffer = new byte[bufferLength];
                        int bytesRead, totalBytesRead=0;
                        while ((bytesRead = is.read(buffer)) != -1) {
                            os.write(buffer, 0, bytesRead);
                            totalBytesRead += bytesRead;
                            final int progress = (int) ((totalBytesRead / (float) length) * 100);
                            mainThreadHandler.post(() -> callback.onProgress(progress));
                        }
                        os.flush();
                    }
                }

                return new FileInfo(length, type);
            } catch (Exception e) {
                throw e;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        };

        return submitTask(task, callback);
    }

    private <T> Future<?> submitTask(Callable<T> task, ResultCallback<T> callback) {
        future = executorService.submit(() -> {
            try {
                T result = task.call();
                mainThreadHandler.post(() -> callback.onSuccess(result));
            } catch (Exception e) {
                mainThreadHandler.post(() -> callback.onError(e));
            }
        });
        return future;
    }

    private void cancelPreviousTask() {
        if (future != null && !future.isDone()) {
            future.cancel(true);
        }
    }

    public void shutdown() {
        cancelPreviousTask();
        executorService.shutdown();
    }
}
