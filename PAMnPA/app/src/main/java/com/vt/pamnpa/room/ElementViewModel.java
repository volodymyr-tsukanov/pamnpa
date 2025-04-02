package com.vt.pamnpa.room;
import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

public class ElementViewModel extends AndroidViewModel {
    private final ElementRepository mRepository;
    private final LiveData<List<Element>> mAllElements;
    public ElementViewModel(Application application) {
        super(application);
        mRepository = new ElementRepository(application);
        mAllElements = mRepository.getAllElements();//… pobranie wszystkich metod z repozytorium
    }
    public LiveData<List<Element>> getAllElements() {
        return mAllElements;
    }
    public void deleteAll() {
        mRepository.deleteAll();
    }
}