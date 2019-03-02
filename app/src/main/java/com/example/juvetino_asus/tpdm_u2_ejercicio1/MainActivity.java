package com.example.juvetino_asus.tpdm_u2_ejercicio1;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;
/*
1 CODIGO DE ELIMINIAR

Un alertDialog con editView para poder preguntar le ID que quieres el iminar, para depues buscarolo
y ya que se busca e otro alertDialog preguntar si se eliminara mostrando datos encontrados (nombre, domicilio)
y confirmar para elimnar o negar, para no eliminar

        a.Crear un alertDialog con EditView pa preguntar el ID a eliminar


        b.Buscar y en otro AlertDialog preguntar si se elimina mostrando datos encontrados del punto a.

        Estas seguro que quieres eliminar a juan perez y si
        si se invoca el try y cactch y ahce el delete
        que vienes arrastrando de de que lo pediste del editText

        c.tras confirmar eliminar el registro /tras negar no eliminar
*/


/*
2 -Actualizar persona
    	a)Buscar x id si lo encuentra abrir un alertDialog
	        	nombre:____________________________
	        	Domicilio:_________________________
                            |Cancelar|              |Actualizar|
	(Con un dialog con un EditText Como con eliminar)

	sino se encuentra un Toast "No existe ID"

	b)Ir a metodo actualizar y aplicar los cambios en la base deatos con un update

 */
public class MainActivity extends AppCompatActivity {
        BasePrimera base;
        EditText nombre,domicilio,idEliminar;
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

        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pedirID("actualizar"); //Éste método se moldea para que sirva tanto para eliminar como actualizar

            }
        });
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pedirID("eliminar");
            }
        });

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


    private void pedirID(final String tipoAccion){
        /*
            SI ANALIZAMOS, TANTO EN ELIMINAR COMO EN ACTUALIZAR SE PIDE PRIMERAMNETE BUSCAR EL ID, LUEGO SE ABRE OTRO DIALOG PARA
             -- O CONFIRMAR ELIMINAR
             -- O EDITAR DATOS PARA ACTUALIZAR
             Y ESTE SEGUNDO DIALOGO EN SU BOTON POSITIVO ES QUIEN DECIDE SI SE LLAMA A ELIMINAR O ACTUALIZAR, POR TANTO HASTA ESE
             MOMENTO ES DONDE AMBAS ACCIONES DEJAN DE TENER EL MISMO RUMBO Y SE SEPARAN.
         */
        AlertDialog.Builder dialogoPedirId = new AlertDialog.Builder(this); //Se construye
        final EditText idbuscar =new EditText(this);

        idbuscar.setInputType(InputType.TYPE_CLASS_NUMBER); //Se especializa para capturar numeros enteros
        idbuscar.setHint("Valor DEBE ser mayor de 0");

        dialogoPedirId.setTitle("Atención").setMessage("Escriba el ID a "+tipoAccion+": ")
                .setView(idbuscar).setNegativeButton("Cancelar",null)
                .setPositiveButton("Buscar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(idbuscar.getText().toString().isEmpty()){ //Si deja vacío el campo de texto
                            mensaje("Error", "No escribiste un ID a buscar"); //se reutiliza el método ID
                            return;
                        }
                        consultarID(tipoAccion, idbuscar.getText().toString()); //ID a buscar se lleva el tipoAccion (eliminar o actualizar) y el ID escrito para buscarlo
                        dialog.dismiss(); //para quitar el dialogo
                    }
                }).show();
    }

    private void  consultarID(String tipoAccion, String id){
        try{
            SQLiteDatabase consultar = base.getReadableDatabase();
            String SQL = "SELECT * FROM PERSONA WHERE IDPERSONA="+id;

            Cursor respuesta = consultar.rawQuery(SQL,null);
            if(respuesta.moveToFirst()){
                String nom = respuesta.getString(1);
                String dom = respuesta.getString(2);

                confirmarAccion(tipoAccion, id, nom, dom); //Se invoca al método para confirmar acción

            } else {
                mensaje("ERROR", "No se encontró el ID ["+id+"] buscado para "+tipoAccion );
            }
            consultar.close();
        }catch (SQLiteException e){
            mensaje("ERROR", e.getMessage());
        }
    }

    private void confirmarAccion(final String tipoAccion, final String id, String nom, String dom){
        AlertDialog.Builder confirmar = new AlertDialog.Builder(this);

        //SE CONSTRUYEN LOS OBJETOS PARA EN CASO DE ACTUALIZAR, SE HACE AQUI PORQUE DEBEN SER "INICIALIZADOS" PARA PODER SE ENVIADOS A METODO ACTUALIZAR
        View contenido = getLayoutInflater().inflate(R.layout.actualizar,null);
        final EditText nombreEditar = contenido.findViewById(R.id.editnombre);
        final EditText domicilioEditar = contenido.findViewById(R.id.editdomicilio);

        confirmar.setTitle("Proceso de "+tipoAccion);

        //AQUI ES DONDE TIPOACCION SE USA PARA DIFERENCIAR SI ES UNA ELIMINACION O SI ES ACTUALIZACION
        if(tipoAccion.startsWith("eliminar")){
            String mensaje = "¿Está seguro que desea eliminar a "+nom+"\nCon domicilio en: "+dom+"?";
            confirmar.setMessage(mensaje);
        } else {

            confirmar.setView(contenido); //NO DEBE LLEVAR SETMESSAGE SINO SETVIEW por tener muchos views
            nombreEditar.setText(nom); //SE ASINGA EL VALOR AL CAMPO NOMBRE
            domicilioEditar.setText(dom); //SE ASIGNA EL DOMICILIO AL CAMPO DOMICILIEDITAR
        }
        confirmar.setNegativeButton("Cancelar",null)
                .setPositiveButton("Aplicar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                          /*
                          IMPORTANTE:

                            AQUI ES DONDE SE HACE EFECTIVO EL TIPOACCION PARA INVOCAR EL METOOD ELIMINAR
                            O PARA INVOCAR EL METODO ACTUALIZAR SEGUN SE REQUIERA EN EL TEXTO ORIGINAL
                            QUE SE ENVIA DESDE EL ONCLICK DEL RESPECTIVO BOTON
                           */
                        if(tipoAccion.startsWith("eliminar")){
                            eliminar(id);
                            dialog.dismiss();
                        } else {

                            actualizar(id,nombreEditar.getText().toString(), domicilioEditar.getText().toString());
                            dialog.dismiss();
                        }
                    }
                }).show();
    }


     private void actualizar(String id, String nomb, String dom){
        try{
            SQLiteDatabase actualiz = base.getWritableDatabase();
            String SQL = "UPDATE PERSONA SET NOMBRE='"+nomb+"', DOMICILIO='"+dom+"' WHERE IDPERSONA="+id;
            actualiz.execSQL(SQL);
            actualiz.close();
            mensaje("EXITO!","SE ACTUALIZO!");
            consultar();
        }catch (SQLiteException e){
            mensaje("Error de insercion",e.getMessage());
        }
    }

    private void eliminar(String id){
        try{
            SQLiteDatabase elimina = base.getWritableDatabase();
            String SQL = "DELETE FROM PERSONA WHERE IDPERSONA="+id;
            elimina.execSQL(SQL);
            elimina.close();
            mensaje("EXITO!","SE PUDO ELIMINAR!");
            consultar();
        }catch (SQLiteException e){
            mensaje("Error de insercion",e.getMessage());
        }

    }


    private void consultar(){
        try{
            SQLiteDatabase selec = base.getReadableDatabase();
           Cursor c= selec.rawQuery("SELECT * FROM PERSONA", null);
            if(c.moveToFirst()==true){
                String cadena ="";
                do{
                    String id = c.getString(0);
                    String nom = c.getString(1);
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

