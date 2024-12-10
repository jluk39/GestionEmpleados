package com.example.gestionempleados;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CrudEmpleadosActivity extends AppCompatActivity {
    private ListView empleadosListView;
    private TextView noEmpleadosTextView;
    private DatabaseHelper db;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> empleadosList;
    private ArrayList<Integer> empleadosIdList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_empleados);

        empleadosListView = findViewById(R.id.empleadosListView);
        noEmpleadosTextView = findViewById(R.id.noEmpleadosTextView);
        db = new DatabaseHelper(this);

        findViewById(R.id.backToDashboardButton).setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        findViewById(R.id.addEmpleadoButton).setOnClickListener(view -> {
            Intent intent = new Intent(this, AddEmpleadoActivity.class);
            startActivity(intent);
        });

        empleadosListView.setOnItemClickListener((adapterView, view, position, id) -> {
            int empleadoId = empleadosIdList.get(position);
            Intent intent = new Intent(this, EditEmpleadoActivity.class);
            intent.putExtra("empleadoId", empleadoId);
            startActivity(intent);
        });

        empleadosListView.setOnItemLongClickListener((adapterView, view, position, id) -> {
            int empleadoId = empleadosIdList.get(position);
            eliminarEmpleado(empleadoId);
            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarEmpleados();
    }

    private void cargarEmpleados() {
        empleadosList = new ArrayList<>();
        empleadosIdList = new ArrayList<>();
        Cursor cursor = db.obtenerEmpleados();

        if (cursor.moveToFirst()) {
            do {
                empleadosIdList.add(cursor.getInt(0));
                empleadosList.add(
                        cursor.getString(1) + " " + cursor.getString(2) +
                                " - " + cursor.getString(3) + " (" + cursor.getString(4) + ")"
                );
            } while (cursor.moveToNext());
        }

        cursor.close();

        if (empleadosList.isEmpty()) {
            noEmpleadosTextView.setVisibility(TextView.VISIBLE);
            empleadosListView.setVisibility(ListView.GONE);
        } else {
            noEmpleadosTextView.setVisibility(TextView.GONE);
            empleadosListView.setVisibility(ListView.VISIBLE);
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, empleadosList);
            empleadosListView.setAdapter(adapter);
        }
    }

    private void eliminarEmpleado(int empleadoId) {
        if (db.eliminarEmpleado(empleadoId)) {
            Toast.makeText(this, "Personal eliminado", Toast.LENGTH_SHORT).show();
            cargarEmpleados();
        } else {
            Toast.makeText(this, "Error al eliminar personal", Toast.LENGTH_SHORT).show();
        }
    }
}
