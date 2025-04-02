package com.vt.pamnpa.room;
import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;

public class ElementRepository {
    private ElementDao mElementDao;
    private LiveData<List<Element>> mAllElements;
    ElementRepository(Application application) {
        ElementRoomDatabase elementRoomDatabase =
                ElementRoomDatabase.getDatabase(application);
        //repozytorium korzysta z obiektu DAO do odwołań do bazy
        mElementDao = elementRoomDatabase.elementDao();
        mAllElements = mElementDao.getAlphabetizedElements();//… odczytanie wszystkich elementów z DAO
    }
    LiveData<List<Element>> getAllElements() {
        //… metdoda zwraca wszystkie elementy
        return mAllElements;
    }
    void deleteAll() {
        ElementRoomDatabase.databaseWriteExecutor.execute(() -> {
            //… skasowanie wszystkich elementów za pomocą DAO
        });
    }
}
