package com.vt.pamnpa.async;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.*;


enum ShortTaskResult {
    ok
}

public class ShortTask {
    private static final String TAG = ShortTask.class.getSimpleName();
    private final ExecutorService executorService;
    private final Handler mainThreadHandler;
    private Future<ResultType> future;

    public ShortTask() {
        // utworzenie puli 2 wątków
        executorService = Executors.newFixedThreadPool(2);
        // utworzenie Handlera do wysyłania zadań do wątku UI
        mainThreadHandler = new Handler(Looper.getMainLooper());
    }

    // wywołanie zwrotne do przekazywania wyników
    public interface ResultCallback {
        void onSuccess(ShortTaskResulth result);

        void onError(Throwable throwable);
        // można dodać więcej metod np. do przekazywania informacji o postępie
    }

    // do metody trzeba przekazać wywołanie zwrotne do odbierania wyników i wymagane
    // parametry
    public Future<ShortTaskResult> executeTask(ResultCallback callback, String param) {
        // anulowanie bieżącego zadania
        if (future != null && !future.isDone()) {
            future.cancel(true);
        }
        // tworzenie zadania, które odczyta wynik zadania wykonanego w tle i
        // przekaże go do głównego wątku
        Runnable completionTask = new Runnable() {
            @Override
            public void run() {
                try {
                    // czekanie na wynik (jeżeli wyniku nie ma blokuje wątek
                    // wywołujący)
                    ShortTaskResult result = future.get();
                    // przekazanie wyniku
                    callback.onSuccess(result);
                } catch (CancellationException e) {
                    // ewentualna reakcja na anulowanie
                } catch (Exception e) {
                    // przekazanie informacji o błędach
                    callback.onError(e);
                }
            }
        };
        // tworzenie zadania wykonywanego w tle (zadanie zwraca wynik)
        Callable<ResultType> asyncTask = new Callable<ResultType>() {
            @Override // wykonywane zadania mogą powodować wyjątki
            public ResultType call() throws Exception {
                // tutaj umieścić zadanie do wykonania w tle
                // ...
                // wynik jest gotowy – wysyłamy do wykonania zadanie przekazujące wynik
                mainThreadHandler.post(completionTask);
                // zakończenie przyszłości (poprzez ustawienie wyniku)
                return result;
            }
        };
        // wysłanie zadania do wykonania przez pulę wątków
        future = executorService.submit(asyncTask);
        // zwrócenie przyszłości (może być użyta do anulowania zadania)
        return future;
    }

    // tą metodę wywołać gdy aktywność lub fragment są niszczone
    public void shutdown() {
        // anulowanie wykonywanego zadania
        if (future != null && !future.isDone()) {
            future.cancel(true); // true – przerwanie zadania
        }
        // zamknięcie puli wątków
        executorService.shutdown();
    }
}
