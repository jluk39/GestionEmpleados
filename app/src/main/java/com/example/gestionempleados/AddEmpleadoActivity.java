package com.example.gestionempleados;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddEmpleadoActivity extends AppCompatActivity {
    private EditText nombreInput, apellidoInput, salarioInput;
    private Spinner especialidadSpinner, turnoSpinner;
    private Button saveButton, cancelButton, deleteButton;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_empleado);

        nombreInput = findViewById(R.id.nombreEmpleadoInput);
        apellidoInput = findViewById(R.id.apellidoEmpleadoInput);
        especialidadSpinner = findViewById(R.id.especialidadEmpleadoSpinner);
        turnoSpinner = findViewById(R.id.turnoEmpleadoSpinner);
        salarioInput = findViewById(R.id.salarioEmpleadoInput);
        saveButton = findViewById(R.id.saveEmpleadoButton);
        cancelButton = findViewById(R.id.cancelEmpleadoButton);
        deleteButton = findViewById(R.id.deleteEmpleadoButton);

        deleteButton.setVisibility(View.GONE);

        db = new DatabaseHelper(this);

        // spinner de especialidad
        ArrayAdapter<CharSequence> especialidadAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.especialidades_array,
                android.R.layout.simple_spinner_item
        );
        especialidadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        especialidadSpinner.setAdapter(especialidadAdapter);

        // spinner turno
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

            if (nombre.isEmpty() || apellido.isEmpty() || salarioStr.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                double salario = Double.parseDouble(salarioStr);
                if (db.agregarEmpleado(nombre, apellido, especialidad, turno, salario)) {
                    Toast.makeText(this, "Empleado agregado", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Error al agregar empleado", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelButton.setOnClickListener(view -> finish());
    }
}

