package com.example.gestionempleados;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameInput, passwordInput;
    private Button loginButton, registerButton;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        db = new DatabaseHelper(this);

        loginButton.setOnClickListener(view -> {
            String username = usernameInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            Cursor cursor = db.obtenerUsuarios();
            boolean isAuthenticated = false;
            String userName = null;

            while (cursor.moveToNext()) {
                if (username.equals(cursor.getString(2)) && password.equals(cursor.getString(4))) {
                    isAuthenticated = true;
                    userName = cursor.getString(1);
                    break;
                }
            }

            cursor.close();

            if (isAuthenticated) {
                Toast.makeText(this, "Login exitoso", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("USER_NAME", userName);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
            }
        });

        registerButton.setOnClickListener(view -> startActivity(new Intent(this, RegisterActivity.class)));
    }
}


