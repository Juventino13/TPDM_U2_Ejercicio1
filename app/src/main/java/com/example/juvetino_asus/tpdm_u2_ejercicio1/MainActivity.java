package com.example.juvetino_asus.tpdm_u2_ejercicio1;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;


public class MainActivity extends AppCompatActivity {
        BasePrimera base;
        EditText nombre,domicilio;
        Button actualizar ,insertar,eliminar,consultar;
        TextView resultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        base = new BasePrimera(this,"basesita",null,1);

        nombre = findViewById(R.id.nombre);
        domicilio= findViewById(R.id.domicilio);
        actualizar = findViewById(R.id.actualizar);
        insertar=findViewById( R.id.insertar);
        eliminar = findViewById(R.id.eliminar);
        consultar=findViewById(R.id.consultar);
        resultado = findViewById(R.id.resultado);


        insertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insercion();
            }
        });

        consultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consultar();
            }
        });
    }
    private void consultar(){
        try{
            SQLiteDatabase selec = base.getReadableDatabase();
           Cursor c= selec.rawQuery("SELECT * FROM PERSONA", null);
            if(c.moveToFirst()==true){
                String cadena ="";
                do{
                    String id = c.getString(0);
                    String nom = c.getString(1):
                    String dom = c.getString(2);

                    cadena += id + " , "+ nom +","+ dom +"\n";
                }while

                    (c.moveToNext()==true);
                resultado.setText(cadena);
                selec.close();

            }else{
                mensaje("ERROR","AUN NO HAY DATOS PARA MOSTRAR");
            }
        }catch (SQLiteException e ){
            mensaje ("Error consulta",e.getMessage());
        }
    }
  private void insercion(){
        try{
            SQLiteDatabase inser = base.getWritableDatabase();
            String SQL = "INSERT INTO PERSONA VALUES (NULL,'%1','%2')";
            SQL = SQL.replace("%1",nombre.getText().toString());
            SQL = SQL.replace("%2",domicilio.getText().toString());

            inser.execSQL(SQL);

            inser.close();
            nombre.setText("");
            domicilio.setText("");
            mensaje ("Exito!", "Se SE PUDO INSERTAR");


        }catch (SQLiteException e){
            mensaje ("Error de insercion", e.getMessage());
        }
  }


    private void mensaje(String titulo, String Mensaje) {
        AlertDialog.Builder a = new AlertDialog.Builder(this);
        a.setTitle(titulo)
                    .setMessage(Mensaje)
                .setPositiveButton("Aceptar",null)
                .show();
    }

}

