package com.example.basededatos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText edcodigo, ednombre, edprecio;
    private Spinner sp1;
    ArrayList<String> listaProductos;
    ArrayList<Producto> productosList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edcodigo = findViewById(R.id.edcodigo);
        ednombre = findViewById(R.id.ednombre);
        edprecio = findViewById(R.id.edprecio);
        sp1 = findViewById(R.id.spinner);

        ArrayAdapter<String> adaptador = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listaProductos);
        sp1.setAdapter(adaptador);
        consultarListaProductos();

    }


    public void consultarListaProductos() {
        adminbd Admin = new adminbd(this, "Productos", null, 1);
        SQLiteDatabase base = Admin.getReadableDatabase();
        Producto p1 = new Producto();
        productosList = new ArrayList<Producto>();

        Cursor fila = base.rawQuery("select * from producto", null);
        while (fila.moveToNext()) {
            p1.setCodigo(fila.getInt(0));
            p1.setNombre(fila.getString(1));
            p1.setPrecio(fila.getInt(2));
            productosList.add(p1);
        }

    }

    public void obtenerProducto () {
        listaProductos = new ArrayList<String>();
        for (int i = 0; i < productosList.size(); i++ ) {
            listaProductos.add(productosList.get(i).getCodigo() + " - "
                    + productosList.get(i).getNombre() + " - " + productosList.get(i).getPrecio());

        }
    }



    public void crear(View v) {
        adminbd admin = new adminbd(this, "Productos", null, 1);
        SQLiteDatabase base = admin.getWritableDatabase();

        String codigo = edcodigo.getText().toString();
        String nombre = ednombre.getText().toString();
        String precio = edprecio.getText().toString();

        if (!codigo.isEmpty() && !nombre.isEmpty() && !precio.isEmpty()) {

            ContentValues crear = new ContentValues();
            crear.put("codigo", codigo);
            crear.put("nombre", nombre);
            crear.put("precio", precio);
            base.insert("producto", null, crear);
            base.close();
            edcodigo.setText("");
            ednombre.setText("");
            edprecio.setText("");
            Toast.makeText(this, "Producto creado", Toast.LENGTH_LONG).show();

        } else {

            Toast.makeText(this, "Debes llenar todos los campos", Toast.LENGTH_LONG).show();
        }

    }


        public void buscar(View v) {

            adminbd Admin = new adminbd(this, "Productos", null, 1);
            SQLiteDatabase Base = Admin.getWritableDatabase();

            String codigo = edcodigo.getText().toString();

            if (!codigo.isEmpty()) {

                Cursor fila = Base.rawQuery("select nombre, precio from producto where codigo=" + codigo, null);
                if (fila.moveToFirst()) {
                    ednombre.setText(fila.getString(0));
                    edprecio.setText(fila.getString(1));
                    Base.close();
                }else{
                    Toast.makeText(this, "No existe el producto", Toast.LENGTH_LONG).show();
                }

            }

        }
        public void modificar (View v){
            adminbd Admin = new adminbd(this, "Productos", null, 1);
             SQLiteDatabase Base = Admin.getWritableDatabase();

                String Codigo = edcodigo.getText().toString();
                String Nombre = ednombre.getText().toString();
                String Precio = edprecio.getText().toString();

            if (!Codigo.isEmpty() && !Precio.isEmpty()) {

                ContentValues Modificar = new ContentValues();
                Modificar.put("codigo", Codigo);
                Modificar.put("nombre", Nombre);
                Modificar.put("precio", Precio);
                Base.update("producto", Modificar, "codigo=" + Codigo, null);
                Base.close();
                Toast.makeText(this, "El registro fue modificado", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(this, "Debes llenar los campos", Toast.LENGTH_LONG).show();
            }

        }
        public void eliminar(View v){

            adminbd Admin= new adminbd(this, "Productos", null, 1);
            SQLiteDatabase Base=Admin.getWritableDatabase();

            String codigo=edcodigo.getText().toString();

            if(!codigo.isEmpty()) {
                Base.delete("producto", "codigo=" + codigo, null);
                Base.close();
                Toast.makeText(this, "Se elimino el registro", Toast.LENGTH_LONG).show();
                edcodigo.setText("");
                ednombre.setText("");
                edprecio.setText("");

            }else{
                Toast.makeText(this, "Debes ingresar un codigo de producto", Toast.LENGTH_LONG).show();

            }

        }

    }
