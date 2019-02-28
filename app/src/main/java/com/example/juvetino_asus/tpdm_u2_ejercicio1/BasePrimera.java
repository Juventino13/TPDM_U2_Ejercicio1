package com.example.juvetino_asus.tpdm_u2_ejercicio1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BasePrimera  extends SQLiteOpenHelper {

    public BasePrimera(Context context,  String name,
                       SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE GALLETAS (IDPERSONA INTEGER PRIMARY KEY AUTOINCREMENT,NOMBRE VARCHAR(200),DOMICILIO VARCHAR(200) )");

        //Se ejecuta cuando se instala y se ejecuta x 1ra vez la APP

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Se ejecuta si y SOLO SI la version tiene un cambio



    }
}
