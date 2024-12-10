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

public class CrudUsuariosActivity extends AppCompatActivity {
    private ListView usuariosListView;
    private TextView noUsuariosTextView;
    private DatabaseHelper db;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> usuariosList;
    private ArrayList<Integer> usuariosIdList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_usuarios);

        usuariosListView = findViewById(R.id.usuariosListView);
        noUsuariosTextView = findViewById(R.id.noUsuariosTextView);
        db = new DatabaseHelper(this);

        findViewById(R.id.backToDashboardButton).setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        findViewById(R.id.addUsuarioButton).setOnClickListener(view -> {
            Intent intent = new Intent(this, AddUsuarioActivity.class);
            startActivity(intent);
        });

        usuariosListView.setOnItemClickListener((adapterView, view, position, id) -> {
            int usuarioId = usuariosIdList.get(position);
            Intent intent = new Intent(this, EditUsuarioActivity.class);
            intent.putExtra("usuarioId", usuarioId);
            startActivity(intent);
        });

        usuariosListView.setOnItemLongClickListener((adapterView, view, position, id) -> {
            int usuarioId = usuariosIdList.get(position);
            eliminarUsuario(usuarioId);
            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarUsuarios(); // recarga la lista al volver a esta actividad
    }

    private void cargarUsuarios() {
        usuariosList = new ArrayList<>();
        usuariosIdList = new ArrayList<>();
        Cursor cursor = db.obtenerUsuarios();

        if (cursor.moveToFirst()) {
            do {
                usuariosIdList.add(cursor.getInt(0));
                usuariosList.add(cursor.getString(1) + " (" + cursor.getString(2) + ")");
            } while (cursor.moveToNext());
        }

        cursor.close();

        if (usuariosList.isEmpty()) {
            noUsuariosTextView.setVisibility(TextView.VISIBLE);
            usuariosListView.setVisibility(ListView.GONE);
        } else {
            noUsuariosTextView.setVisibility(TextView.GONE);
            usuariosListView.setVisibility(ListView.VISIBLE);
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, usuariosList);
            usuariosListView.setAdapter(adapter);
        }
    }

    private void eliminarUsuario(int usuarioId) {
        if (db.eliminarUsuario(usuarioId)) {
            Toast.makeText(this, "Usuario eliminado", Toast.LENGTH_SHORT).show();
            cargarUsuarios();
        } else {
            Toast.makeText(this, "Error al eliminar usuario", Toast.LENGTH_SHORT).show();
        }
    }
}



