package com.example.gestionempleados;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddEmpleadoActivity extends AppCompatActivity {
    private EditText nombreInput, apellidoInput, salarioInput, dniInput;
    private Spinner especialidadSpinner, turnoSpinner;
    private Button saveButton, cancelButton, deleteButton;
    private DatabaseHelper db;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_empleado);

        nombreInput = findViewById(R.id.nombreEmpleadoInput);
        apellidoInput = findViewById(R.id.apellidoEmpleadoInput);
        especialidadSpinner = findViewById(R.id.especialidadEmpleadoSpinner);
        turnoSpinner = findViewById(R.id.turnoEmpleadoSpinner);
        salarioInput = findViewById(R.id.salarioEmpleadoInput);
        dniInput = findViewById(R.id.dniEmpleadoInput);
        saveButton = findViewById(R.id.saveEmpleadoButton);
        cancelButton = findViewById(R.id.cancelEmpleadoButton);
        deleteButton = findViewById(R.id.deleteEmpleadoButton);

        deleteButton.setVisibility(View.GONE);

        db = new DatabaseHelper(this);
        executorService = Executors.newSingleThreadExecutor();

        // spinner de especialidad
        ArrayAdapter<CharSequence> especialidadAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.especialidades_array,
                android.R.layout.simple_spinner_item
        );
        especialidadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        especialidadSpinner.setAdapter(especialidadAdapter);

        // spinner de turno
        ArrayAdapter<CharSequence> turnoAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.turnos_array,
                android.R.layout.simple_spinner_item
        );
        turnoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        turnoSpinner.setAdapter(turnoAdapter);

        saveButton.setOnClickListener(view -> {
            String nombre = nombreInput.getText().toString().trim();
            String apellido = apellidoInput.getText().toString().trim();
            String especialidad = especialidadSpinner.getSelectedItem().toString();
            String turno = turnoSpinner.getSelectedItem().toString();
            String salarioStr = salarioInput.getText().toString().trim();
            String dniStr = dniInput.getText().toString().trim();

            if (nombre.isEmpty() || apellido.isEmpty() || salarioStr.isEmpty() || dniStr.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double salario = Double.parseDouble(salarioStr);
                int dni = Integer.parseInt(dniStr);

                executorService.execute(() -> {
                    if (db.verificarDniUnico(dni)) {
                        runOnUiThread(() -> Toast.makeText(this, "Ya existe un empleado con ese DNI", Toast.LENGTH_SHORT).show());
                    } else {
                        boolean success = db.agregarEmpleado(nombre, apellido, especialidad, turno, salario, dni);
                        runOnUiThread(() -> {
                            if (success) {
                                Toast.makeText(this, "Empleado agregado", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(this, "Error al agregar empleado", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            } catch (NumberFormatException e) {
                Toast.makeText(this, "DNI y salario deben ser números válidos", Toast.LENGTH_SHORT).show();
            }
        });

        cancelButton.setOnClickListener(view -> finish());
    }
}
