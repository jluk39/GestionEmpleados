package com.example.gestionempleados;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EditUsuarioActivity extends AppCompatActivity {
    private EditText nombreInput, usuarioInput, emailInput, passwordInput;
    private Button updateButton, cancelButton, deleteButton;
    private DatabaseHelper db;
    private int usuarioId;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_usuario);

        nombreInput = findViewById(R.id.nombreInput);
        usuarioInput = findViewById(R.id.usuarioInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        updateButton = findViewById(R.id.saveUsuarioButton);
        cancelButton = findViewById(R.id.cancelUsuarioButton);
        deleteButton = findViewById(R.id.deleteUsuarioButton);

        updateButton.setText("Actualizar");

        db = new DatabaseHelper(this);
        executorService = Executors.newSingleThreadExecutor();

        // obtiene el ID del usuario desde el intent
        usuarioId = getIntent().getIntExtra("usuarioId", -1);

        cargarUsuario();

        updateButton.setOnClickListener(view -> {
            String nombre = nombreInput.getText().toString().trim();
            String usuario = usuarioInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (nombre.isEmpty() || usuario.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            executorService.execute(() -> {
                boolean success = db.actualizarUsuario(usuarioId, nombre, usuario, email, password);
                runOnUiThread(() -> {
                    if (success) {
                        Toast.makeText(this, "Usuario actualizado", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, "Error al actualizar usuario", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        });

        cancelButton.setOnClickListener(view -> finish());

        deleteButton.setOnClickListener(view -> mostrarConfirmacionEliminacion());
    }

    private void cargarUsuario() {
        executorService.execute(() -> {
            Cursor cursor = db.obtenerUsuarios();
            while (cursor.moveToNext()) {
                if (cursor.getInt(0) == usuarioId) {
                    String nombre = cursor.getString(1);
                    String usuario = cursor.getString(2);
                    String email = cursor.getString(3);
                    String password = cursor.getString(4);

                    runOnUiThread(() -> {
                        nombreInput.setText(nombre);
                        usuarioInput.setText(usuario);
                        emailInput.setText(email);
                        passwordInput.setText(password);
                    });
                    break;
                }
            }
            cursor.close();
        });
    }

    private void mostrarConfirmacionEliminacion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmar Eliminación");
        builder.setMessage("¿Estás seguro de que deseas eliminar este usuario?");
        builder.setPositiveButton("Sí", (dialog, which) -> {
            executorService.execute(() -> {
                boolean success = db.eliminarUsuario(usuarioId);
                runOnUiThread(() -> {
                    if (success) {
                        Toast.makeText(this, "Usuario eliminado", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, "Error al eliminar usuario", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        });
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}
