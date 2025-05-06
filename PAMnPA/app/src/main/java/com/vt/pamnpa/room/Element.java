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
    @ColumnInfo(name = "version")
    private int version;
    @ColumnInfo(name = "site")
    private String site;

    //konstruktor wykorzystywany przez Room do tworzenia obiektów
    public Element(String manufacturer, String model, int version, String site) {
        this.manufacturer = manufacturer;
        this.model = model;
        this.version = version;
        this.site = site;
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
    public int getVersion() {
        return version;
    }
    public void setVersion(int version) {
        this.version = version;
    }
    public String getSite() {
        return site;
    }
    public void setSite(String site) {
        this.site = site;
    }
    //jeżeli konieczne są dodatkowe konstruktory należy je poprzedzić adnotacją @Ignore
    //żeby biblioteka Room z nich nie korzystała
    //Room może wymagać również getterów i setterów także warto je utworzyć
}