package com.vt.pamnpa.room;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "phones")
public class Element {
    //pierwsza adnotacja oznacza, że pole jest kluczem głównym, wartości klucza będą generowane
    //automatycznie, druga adnotacja określa, jaka będzie nazwa kolumny w tabeli
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    //pierwsza adnotacja oznacza, że pole nie może być puste
    //druga adnotacja określa, jaka będzie nazwa kolumny w tabeli
    @ColumnInfo(name = "manufacturer")
    private String manufacturer;
    @ColumnInfo(name = "model")
    private String model;

    //konstruktor wykorzystywany przez Room do tworzenia obiektów
    public Element(String manufacturer, String model) {
        this.manufacturer = manufacturer;
        this.model = model;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getManufacturer() {
        return manufacturer;
    }
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }

    //jeżeli konieczne są dodatkowe konstruktory należy je poprzedzić adnotacją @Ignore
    //żeby biblioteka Room z nich nie korzystała
    //Room może wymagać również getterów i setterów także warto je utworzyć
}