package com.orbita.innovacion.swipelistpojo;

import android.app.Dialog;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class App extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipe=null;
    private ArrayAdapter<ListItem> adapter = null;
    private ArrayList<ListItem> names = new ArrayList<>();
    private ListView lista = null;

    private EditText nombre ,edad;
    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swipe);

        names.add(new ListItem("Ricar2", "20"));
        names.add(new ListItem("Kike", "31"));
        names.add(new ListItem("Yarely", "20"));
        names.add(new ListItem("Angela", "20"));
        names.add(new ListItem("Angelica", "20"));
        names.add(new ListItem("Chardo", "30"));
        names.add(new ListItem("Che Portillo", "20"));
        names.add(new ListItem("Gustavo San", "20"));
        names.add(new ListItem("Eduardo", "24"));

        swipe   = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipe.setOnRefreshListener(this);
        swipe.setColorSchemeResources(
                android.R.color.holo_green_dark,
                android.R.color.holo_blue_dark,
                android.R.color.holo_purple,
                android.R.color.holo_red_dark
        );

        lista = (ListView) findViewById(R.id.List);
        llenar();

        lista.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                int filasuperior = (lista == null || lista.getChildCount() == 0)? 0 :lista.getChildAt(0).getTop();
                swipe.setEnabled(filasuperior >= 0);
            }
        });

        lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Object fhater = (ListItem) parent.getItemAtPosition(position);

                String nombre = ((ListItem)fhater).getNombre();
                String edad = ((ListItem)fhater).getEdad();

                eliminar(position, nombre, edad);
                return true;
            }
        });

    }

    private void llenar() {

        adapter = new ArrayAdapter<ListItem>(this,android.R.layout.simple_list_item_1, names);
        lista.setAdapter(adapter);

    }

    private void eliminar(final int position, final String nombre, final String edad){

        //tmp = names;

        if(lista.getChildCount() > 0) {
            names.remove(position);
            adapter.notifyDataSetChanged();
            Snackbar.make(swipe, "Eliminado",
                    Snackbar.LENGTH_LONG).setAction("Deshacer", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    deshacer(position, nombre, edad);
                }
            }).show();
        }

    }

    private void deshacer(int position, String nombre, String edad){
        names.add(position, new ListItem(nombre, edad));
        llenar();
    }

    @Override
    public void onRefresh() {
        swipe.setRefreshing(true);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                llenar();
                Snackbar.make(swipe, "Lista Actualizada",
                        Snackbar.LENGTH_LONG).setAction("", null).show();
                swipe.setRefreshing(false);
            }
        }, 500);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.action_add:
                displayDialog();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void displayDialog()
    {
        Dialog d = new Dialog(this);
        d.setContentView(R.layout.dialog);
        nombre = (EditText)d.findViewById(R.id.nameEditText);
        edad = (EditText)d.findViewById(R.id.edadEditText);
        save = (Button)d.findViewById(R.id.saveBtn);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nombre.getText().toString().equals("")){
                    Snackbar.make(swipe, "Ingrese datos",
                            Snackbar.LENGTH_LONG).setAction("", null).show();
                    return;
                }else if(edad.getText().toString().equals("")){
                    Snackbar.make(swipe, "Ingrese datos",
                            Snackbar.LENGTH_LONG).setAction("", null).show();
                    return;
                }else {
                    names.add(new ListItem(nombre.getText().toString(), edad.getText().toString()));

                    nombre.setText("");
                    edad.setText("");

                    Snackbar.make(swipe, "Agregado",
                            Snackbar.LENGTH_LONG).setAction("", null).show();
                }
            }
        });
        d.show();
    }

}
