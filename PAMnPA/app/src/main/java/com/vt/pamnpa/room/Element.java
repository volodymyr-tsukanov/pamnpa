package com.vt.pamnpa.room;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "nazwa_tabeli")
public class Element {
    //pierwsza adnotacja oznacza, że pole jest kluczem głównym, wartości klucza będą generowane
    //automatycznie, druga adnotacja określa, jaka będzie nazwa kolumny w tabeli
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "nazwa_kolumny_w_tabeli")
    private long mKolumna;

    //pierwsza adnotacja oznacza, że pole nie może być puste
    //druga adnotacja określa, jaka będzie nazwa kolumny w tabeli
    @ColumnInfo(name = "nazwa_kolumny_2_w_tabeli")
    private String mKolumna2;

    //konstruktor wykorzystywany przez Room do tworzenia obiektów
    public Element(String kolumna2) {
        this.mKolumna2 = kolumna2;
    }
    //jeżeli konieczne są dodatkowe konstruktory należy je poprzedzić adnotacją @Ignore
    //żeby biblioteka Room z nich nie korzystała
    //Room może wymagać również getterów i setterów także warto je utworzyć
}