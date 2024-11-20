package com.example.gestionempleados;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CrudUsuariosActivity extends AppCompatActivity {
    private ListView usuariosListView;
    private Button addUsuarioButton;
    private DatabaseHelper db;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> usuariosList;
    private ArrayList<Integer> usuariosIdList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_usuarios);

        usuariosListView = findViewById(R.id.usuariosListView);
        addUsuarioButton = findViewById(R.id.addUsuarioButton);
        db = new DatabaseHelper(this);

        cargarUsuarios();

        addUsuarioButton.setOnClickListener(view -> mostrarDialogoAgregarUsuario());

        usuariosListView.setOnItemClickListener((adapterView, view, position, id) -> {
            int usuarioId = usuariosIdList.get(position);
            mostrarDialogoEditarUsuario(usuarioId);
        });

        usuariosListView.setOnItemLongClickListener((adapterView, view, position, id) -> {
            int usuarioId = usuariosIdList.get(position);
            mostrarDialogoEliminarUsuario(usuarioId);
            return true;
        });
    }

    private void cargarUsuarios() {
        usuariosList = new ArrayList<>();
        usuariosIdList = new ArrayList<>();
        Cursor cursor = db.obtenerUsuarios();

        while (cursor.moveToNext()) {
            usuariosIdList.add(cursor.getInt(0));
            usuariosList.add(cursor.getString(1) + " (" + cursor.getString(2) + ")");
        }
        cursor.close();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, usuariosList);
        usuariosListView.setAdapter(adapter);
    }

    private void mostrarDialogoAgregarUsuario() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Agregar Usuario");

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_usuario, null);
        EditText nombreInput = view.findViewById(R.id.nombreInput);
        EditText emailInput = view.findViewById(R.id.emailInput);
        EditText passwordInput = view.findViewById(R.id.passwordInput);

        builder.setView(view);
        builder.setPositiveButton("Guardar", (dialogInterface, i) -> {
            String nombre = nombreInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (nombre.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            } else if (db.registrarUsuario(nombre, email, password)) {
                Toast.makeText(this, "Usuario agregado", Toast.LENGTH_SHORT).show();
                cargarUsuarios();
            } else {
                Toast.makeText(this, "Error al agregar usuario", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void mostrarDialogoEditarUsuario(int usuarioId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Editar Usuario");

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_usuario, null);
        EditText nombreInput = view.findViewById(R.id.nombreInput);
        EditText emailInput = view.findViewById(R.id.emailInput);
        EditText passwordInput = view.findViewById(R.id.passwordInput);

        Cursor cursor = db.obtenerUsuarios();
        while (cursor.moveToNext()) {
            if (cursor.getInt(0) == usuarioId) {
                nombreInput.setText(cursor.getString(1));
                emailInput.setText(cursor.getString(2));
                passwordInput.setText(cursor.getString(3));
                break;
            }
        }
        cursor.close();

        builder.setView(view);
        builder.setPositiveButton("Actualizar", (dialogInterface, i) -> {
            String nombre = nombreInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (nombre.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            } else if (db.actualizarUsuario(usuarioId, nombre, email, password)) {
                Toast.makeText(this, "Usuario actualizado", Toast.LENGTH_SHORT).show();
                cargarUsuarios();
            } else {
                Toast.makeText(this, "Error al actualizar usuario", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void mostrarDialogoEliminarUsuario(int usuarioId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar Usuario");
        builder.setMessage("¿Estás seguro de eliminar este usuario?");
        builder.setPositiveButton("Sí", (dialogInterface, i) -> {
            if (db.eliminarUsuario(usuarioId)) {
                Toast.makeText(this, "Usuario eliminado", Toast.LENGTH_SHORT).show();
                cargarUsuarios();
            } else {
                Toast.makeText(this, "Error al eliminar usuario", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }
}
