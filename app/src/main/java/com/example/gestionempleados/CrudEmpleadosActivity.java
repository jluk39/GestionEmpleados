package com.example.gestionempleados;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CrudEmpleadosActivity extends AppCompatActivity {
    private ListView empleadosListView;
    private DatabaseHelper db;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> empleadosList;
    private ArrayList<Integer> empleadosIdList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_empleados);

        empleadosListView = findViewById(R.id.empleadosListView);
        db = new DatabaseHelper(this);

        cargarEmpleados();

        findViewById(R.id.addEmpleadoButton).setOnClickListener(view -> mostrarDialogoAgregarEmpleado());

        empleadosListView.setOnItemClickListener((adapterView, view, position, id) -> {
            int empleadoId = empleadosIdList.get(position);
            mostrarDialogoEditarEmpleado(empleadoId);
        });

        empleadosListView.setOnItemLongClickListener((adapterView, view, position, id) -> {
            int empleadoId = empleadosIdList.get(position);
            mostrarDialogoEliminarEmpleado(empleadoId);
            return true;
        });
    }

    private void cargarEmpleados() {
        empleadosList = new ArrayList<>();
        empleadosIdList = new ArrayList<>();
        Cursor cursor = db.obtenerEmpleados();

        while (cursor.moveToNext()) {
            empleadosIdList.add(cursor.getInt(0));
            empleadosList.add(cursor.getString(1) + " " + cursor.getString(2) + " (" + cursor.getString(3) + ")");
        }
        cursor.close();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, empleadosList);
        empleadosListView.setAdapter(adapter);
    }

    private void mostrarDialogoAgregarEmpleado() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Agregar Empleado");

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_empleado, null);
        builder.setView(view);

        EditText nombreInput = view.findViewById(R.id.nombreEmpleadoInput);
        EditText apellidoInput = view.findViewById(R.id.apellidoEmpleadoInput);
        EditText cargoInput = view.findViewById(R.id.cargoEmpleadoInput);
        EditText salarioInput = view.findViewById(R.id.salarioEmpleadoInput);

        builder.setPositiveButton("Guardar", (dialogInterface, i) -> {
            String nombre = nombreInput.getText().toString().trim();
            String apellido = apellidoInput.getText().toString().trim();
            String cargo = cargoInput.getText().toString().trim();
            String salarioStr = salarioInput.getText().toString().trim();

            if (nombre.isEmpty() || apellido.isEmpty() || cargo.isEmpty() || salarioStr.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                double salario = Double.parseDouble(salarioStr);
                if (db.agregarEmpleado(nombre, apellido, cargo, salario)) {
                    Toast.makeText(this, "Empleado agregado", Toast.LENGTH_SHORT).show();
                    cargarEmpleados();
                } else {
                    Toast.makeText(this, "Error al agregar empleado", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void mostrarDialogoEditarEmpleado(int empleadoId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Editar Empleado");

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_empleado, null);
        builder.setView(view);

        EditText nombreInput = view.findViewById(R.id.nombreEmpleadoInput);
        EditText apellidoInput = view.findViewById(R.id.apellidoEmpleadoInput);
        EditText cargoInput = view.findViewById(R.id.cargoEmpleadoInput);
        EditText salarioInput = view.findViewById(R.id.salarioEmpleadoInput);

        Cursor cursor = db.obtenerEmpleados();
        while (cursor.moveToNext()) {
            if (cursor.getInt(0) == empleadoId) {
                nombreInput.setText(cursor.getString(1));
                apellidoInput.setText(cursor.getString(2));
                cargoInput.setText(cursor.getString(3));
                salarioInput.setText(String.valueOf(cursor.getDouble(4)));
                break;
            }
        }
        cursor.close();

        builder.setPositiveButton("Actualizar", (dialogInterface, i) -> {
            String nombre = nombreInput.getText().toString().trim();
            String apellido = apellidoInput.getText().toString().trim();
            String cargo = cargoInput.getText().toString().trim();
            String salarioStr = salarioInput.getText().toString().trim();

            if (nombre.isEmpty() || apellido.isEmpty() || cargo.isEmpty() || salarioStr.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                double salario = Double.parseDouble(salarioStr);
                if (db.actualizarEmpleado(empleadoId, nombre, apellido, cargo, salario)) {
                    Toast.makeText(this, "Empleado actualizado", Toast.LENGTH_SHORT).show();
                    cargarEmpleados();
                } else {
                    Toast.makeText(this, "Error al actualizar empleado", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void mostrarDialogoEliminarEmpleado(int empleadoId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar Empleado");
        builder.setMessage("¿Estás seguro de eliminar este empleado?");
        builder.setPositiveButton("Sí", (dialogInterface, i) -> {
            if (db.eliminarEmpleado(empleadoId)) {
                Toast.makeText(this, "Empleado eliminado", Toast.LENGTH_SHORT).show();
                cargarEmpleados();
            } else {
                Toast.makeText(this, "Error al eliminar empleado", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }
}
