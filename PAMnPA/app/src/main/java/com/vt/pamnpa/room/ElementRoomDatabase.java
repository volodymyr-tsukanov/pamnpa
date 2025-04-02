package com.vt.pamnpa.room;
import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Element.class}, version = 1, exportSchema = false)
public abstract class ElementRoomDatabase extends RoomDatabase {
    //abstrakcyjna metoda zwracająca DAO
    public abstract ElementDao elementDao();

    //implementacja singletona
    private static volatile ElementRoomDatabase INSTANCE;

    static ElementRoomDatabase getDatabase(final Context context) {
        //tworzymy nowy obiekt tylko gdy żaden nie istnieje
        if (INSTANCE == null) {
            synchronized (ElementRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    ElementRoomDatabase.class, "nazwa_bazy")
//ustawienie obiektu obsługującego zdarzenia związane z bazą
                            //(kod poniżej)
                            .addCallback(sRoomDatabaseCallback)
                            //najprostsza migracja – skasowanie i utworzenie bazy od nowa
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    //usługa wykonawcza do wykonywania zadań w osobnym wątku
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    //obiekt obsługujący wywołania zwrotne (call backs) związane ze zdarzeniami bazy danych
    //np. onCreate, onOpen
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        //uruchamiane przy tworzeniu bazy (pierwsze uruchomienie aplikacji, gdy baza nie
        //istnieje)
        @Override
        public void onCreate(SupportSQLiteDatabase db) {
            super.onCreate(db);
            //wykonanie operacji w osobnym wątku. Parametrem metody execute() jest obiekt
            //implementujący interfejs Runnable, może być zastąpiony wyrażeniem lambda
            databaseWriteExecutor.execute(() -> {
                ElementDao dao = INSTANCE.elementDao();
                //tworzenie elementów (obiektów klasy Element) i dodawanie ich do bazy
                //za pomocą metody insert() z obiektu dao
                //tutaj możemy określić początkową zawartość bazy danych
                //...
            });
        }
    };
}