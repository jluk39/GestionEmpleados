package com.example.gestionempleados;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameInput, passwordInput;
    private Button loginButton, registerButton;
    private DatabaseHelper db;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        db = new DatabaseHelper(this);

        // inicio ExecutorService con un solo hilo
        executorService = Executors.newSingleThreadExecutor();

        loginButton.setOnClickListener(view -> {
            String username = usernameInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor ingrese usuario y contraseña", Toast.LENGTH_SHORT).show();
                return;
            }

            // tarea de login en un hilo secundario
            executorService.execute(() -> performLogin(username, password));
        });

        registerButton.setOnClickListener(view -> startActivity(new Intent(this, RegisterActivity.class)));
    }

    private void performLogin(String username, String password) {
        boolean isAuthenticated = false;
        boolean isAdmin = false;
        Intent intent = null;

        // intentar login como admin
        Cursor adminCursor = db.obtenerUsuarios();
        while (adminCursor.moveToNext()) {
            if (username.equals(adminCursor.getString(2)) && password.equals(adminCursor.getString(4))) {
                isAuthenticated = true;
                isAdmin = true;
                intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("USER_NAME", adminCursor.getString(1));
                break;
            }
        }
        adminCursor.close();

        if (!isAuthenticated) {
            // intentar login como empleado
            Cursor employeeCursor = db.obtenerEmpleados();
            while (employeeCursor.moveToNext()) {
                if (username.equals(employeeCursor.getString(6)) && password.equals(employeeCursor.getString(6))) { // DNI como usuario y contraseña
                    isAuthenticated = true;
                    intent = new Intent(LoginActivity.this, EmployeeDetailsActivity.class);
                    intent.putExtra("EMPLOYEE_NAME", employeeCursor.getString(1) + " " + employeeCursor.getString(2));
                    intent.putExtra("EMPLOYEE_SALARY", employeeCursor.getDouble(5) * 160); // salario mensual basado en 160 horas/mes
                    intent.putExtra("EMPLOYEE_POSITION", employeeCursor.getString(3));
                    intent.putExtra("EMPLOYEE_SHIFT", employeeCursor.getString(4));
                    intent.putExtra("EMPLOYEE_DNI", employeeCursor.getInt(6)); // DNI
                    break;
                }
            }
            employeeCursor.close();
        }

        boolean finalIsAuthenticated = isAuthenticated;
        boolean finalIsAdmin = isAdmin;
        Intent finalIntent = intent;

        // vuelve al hilo principal para actualizar la UI
        runOnUiThread(() -> {
            if (finalIsAuthenticated) {
                if (finalIsAdmin) {
                    Toast.makeText(LoginActivity.this, "Login como Administrador exitoso", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Login como Empleado exitoso", Toast.LENGTH_SHORT).show();
                }
                startActivity(finalIntent);
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // apaga el executorService para evitar fugas de memoria
        executorService.shutdown();
    }
}
