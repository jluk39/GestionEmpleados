package com.example.gestionempleados;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EmployeeDetailsActivity extends AppCompatActivity {
    private TextView welcomeText, employeeDniText, employeeSalaryText, nextPayDateText, employeePositionText, employeeShiftText;
    private ImageView appLogo;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_details);

        welcomeText = findViewById(R.id.welcomeText);
        employeeDniText = findViewById(R.id.employeeDniText);
        employeeSalaryText = findViewById(R.id.employeeSalaryText);
        nextPayDateText = findViewById(R.id.nextPayDateText);
        employeePositionText = findViewById(R.id.employeePositionText);
        employeeShiftText = findViewById(R.id.employeeShiftText);
        appLogo = findViewById(R.id.appLogo);
        logoutButton = findViewById(R.id.logoutButton);

        String employeeName = getIntent().getStringExtra("EMPLOYEE_NAME");
        double employeeSalary = getIntent().getDoubleExtra("EMPLOYEE_SALARY", 0);
        String employeePosition = getIntent().getStringExtra("EMPLOYEE_POSITION"); // Especialidad
        String employeeShift = getIntent().getStringExtra("EMPLOYEE_SHIFT"); // Turno
        int employeeDni = getIntent().getIntExtra("EMPLOYEE_DNI", 0); // DNI

        welcomeText.setText("Bienvenido, " + employeeName);
        employeeDniText.setText("DNI: " + employeeDni);
        employeeSalaryText.setText("Salario mensual: $" + String.format(Locale.getDefault(), "%.2f", employeeSalary));
        employeePositionText.setText("Puesto: " + employeePosition);
        employeeShiftText.setText("Turno: " + employeeShift);
        nextPayDateText.setText("Próxima fecha de cobro: " + getNextPayDate());

        logoutButton.setOnClickListener(view -> {
            Intent intent = new Intent(EmployeeDetailsActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    private String getNextPayDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        if (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) > 5) {
            calendar.add(Calendar.MONTH, 1);
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }
}
