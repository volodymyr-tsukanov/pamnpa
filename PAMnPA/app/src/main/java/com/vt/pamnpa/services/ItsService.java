package com.vt.pamnpa.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.lifecycle.MutableLiveData;


public class ItsService extends Service {
    public final static String TAG = ItsService.class.getSimpleName();
    //obiekt LiveData do przekazywana informacji o postępie do aktywności
    private MutableLiveData<ProgressEvent> progressLiveData =
            new MutableLiveData<>(null);
    //Binder definiuje metody, które można wywoływać z zewnątrz np. z aktywności
    public class ItsServiceBinder extends Binder {
        LiveData<ProgressEvent> getProgressEvent() {
            return progressLiveData;
        }
    }
    private final IBinder binder = new ItsServiceBinder();
    //metoda odpowiedzialna za zwrócenie Bindera, w przypadku usług niezwiązanych
    //(unbounded) musi zwracać null
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    //aktualizacja powiadomień i statusu postępu
    void sendMessagesAndUpdateNotification() {
        //sprawdzić jaki jest stan pobierania
        //...
        //i zaktualizwać stan pobierania (postValue a nie setValue bo ten kod
        //wykonuje się w dodatkowym wątku)
        progressLiveData.postValue(new ProgressEvent(progress, total, result));
        //...
    }
}
