package com.example.gestionempleados;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddUsuarioActivity extends AppCompatActivity {
    private EditText nombreInput, usuarioInput, emailInput, passwordInput;
    private Button saveButton, cancelButton, deleteButton;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_usuario);

        nombreInput = findViewById(R.id.nombreInput);
        usuarioInput = findViewById(R.id.usuarioInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        saveButton = findViewById(R.id.saveUsuarioButton);
        cancelButton = findViewById(R.id.cancelUsuarioButton);
        deleteButton = findViewById(R.id.deleteUsuarioButton);

        // oculta btn eliminar
        deleteButton.setVisibility(Button.GONE);

        db = new DatabaseHelper(this);

        saveButton.setOnClickListener(view -> {
            String nombre = nombreInput.getText().toString().trim();
            String usuario = usuarioInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (nombre.isEmpty() || usuario.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            } else if (db.registrarUsuario(nombre, usuario, email, password)) {
                Toast.makeText(this, "Usuario agregado", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error al agregar usuario", Toast.LENGTH_SHORT).show();
            }
        });

        cancelButton.setOnClickListener(view -> finish());
    }
}


