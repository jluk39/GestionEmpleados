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

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor ingrese usuario y contraseña", Toast.LENGTH_SHORT).show();
                return;
            }

            // Intentar login como Administrador
            Cursor adminCursor = db.obtenerUsuarios();
            boolean isAdminAuthenticated = false;
            String adminName = null;

            while (adminCursor.moveToNext()) {
                if (username.equals(adminCursor.getString(2)) && password.equals(adminCursor.getString(4))) {
                    isAdminAuthenticated = true;
                    adminName = adminCursor.getString(1);
                    break;
                }
            }
            adminCursor.close();

            if (isAdminAuthenticated) {
                Toast.makeText(this, "Login como Administrador exitoso", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("USER_NAME", adminName);
                startActivity(intent);
                finish();
                return;
            }

            // Intentar login como Empleado
            Cursor employeeCursor = db.obtenerEmpleados();
            boolean isEmployeeAuthenticated = false;
            String employeeName = null;
            double employeeSalary = 0;
            String employeePosition = null;
            String employeeShift = null;
            int employeeDni = 0;

            while (employeeCursor.moveToNext()) {
                if (username.equals(employeeCursor.getString(6)) && password.equals(employeeCursor.getString(6))) { // DNI como usuario y contraseña
                    isEmployeeAuthenticated = true;
                    employeeName = employeeCursor.getString(1) + " " + employeeCursor.getString(2);
                    employeeSalary = employeeCursor.getDouble(5) * 160;
                    employeePosition = employeeCursor.getString(3);
                    employeeShift = employeeCursor.getString(4);
                    employeeDni = employeeCursor.getInt(6);
                    break;
                }
            }
            employeeCursor.close();

            if (isEmployeeAuthenticated) {
                Toast.makeText(this, "Login como Empleado exitoso", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, EmployeeDetailsActivity.class);
                intent.putExtra("EMPLOYEE_NAME", employeeName);
                intent.putExtra("EMPLOYEE_SALARY", employeeSalary);
                intent.putExtra("EMPLOYEE_POSITION", employeePosition);
                intent.putExtra("EMPLOYEE_SHIFT", employeeShift);
                intent.putExtra("EMPLOYEE_DNI", employeeDni);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
            }
        });

        registerButton.setOnClickListener(view -> startActivity(new Intent(this, RegisterActivity.class)));
    }
}
