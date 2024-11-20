package com.example.gestionempleados;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {
    private EditText emailInput, passwordInput;
    private Button loginButton, registerButton;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        db = new DatabaseHelper(this);

        loginButton.setOnClickListener(view -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            Cursor cursor = db.obtenerUsuarios();
            boolean isAuthenticated = false;
            String userName = null;

            while (cursor.moveToNext()) {
                if (email.equals(cursor.getString(2)) && password.equals(cursor.getString(3))) {
                    isAuthenticated = true;
                    userName = cursor.getString(1); // Obtener el nombre del usuario
                    break;
                }
            }

            cursor.close();

            if (isAuthenticated) {
                Toast.makeText(this, "Login exitoso", Toast.LENGTH_SHORT).show();

                // Pasar el nombre del usuario logueado a MainActivity
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("USER_NAME", userName);
                startActivity(intent);
                finish(); // Cerrar la pantalla de login
            } else {
                Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
            }
        });

        registerButton.setOnClickListener(view -> startActivity(new Intent(this, RegisterActivity.class)));
    }
}
