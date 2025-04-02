package com.vt.pamnpa.room;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;

@Dao
public interface ElementDao {
    //adnotacja określająca, że metoda wstawia element do bazy
    //w przypadku konfliktu operacja będzie przerwana
    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert(Element element);
    //adnotacja pozwalająca na wykonanie dowolnego polecenia np. skasowania wszystkich elementów
    @Query("DELETE FROM nazwa_tabeli")
    void deleteAll();
    //metoda zwraca listę elementów opakowaną w pojemnik live data pozwalający na odbieranie
    //powiadomień o zmianie danych. Room wykonuje zapytanie w innym wątku
    //live data powiadamia obserwatora w głównym wątku aplikacji
    @Query("SELECT * FROM nazwa_tabeli ORDER BY nazwa_kolumny_2_w_tabeli ASC")
    LiveData<List<Element>> getAlphabetizedElements();
}