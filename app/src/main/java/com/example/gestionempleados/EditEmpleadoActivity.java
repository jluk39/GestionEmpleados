package com.example.gestionempleados;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class EditEmpleadoActivity extends AppCompatActivity {
    private EditText nombreInput, apellidoInput, salarioInput, dniInput;
    private Spinner especialidadSpinner, turnoSpinner;
    private Button updateButton, cancelButton, deleteButton;
    private DatabaseHelper db;
    private int empleadoId;

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
        updateButton = findViewById(R.id.saveEmpleadoButton);
        cancelButton = findViewById(R.id.cancelEmpleadoButton);
        deleteButton = findViewById(R.id.deleteEmpleadoButton);

        updateButton.setText("Actualizar");
        deleteButton.setVisibility(View.VISIBLE);

        db = new DatabaseHelper(this);
        empleadoId = getIntent().getIntExtra("empleadoId", -1);

        // Spinner de especialidad
        ArrayAdapter<CharSequence> especialidadAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.especialidades_array,
                android.R.layout.simple_spinner_item
        );
        especialidadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        especialidadSpinner.setAdapter(especialidadAdapter);

        // Spinner de turno
        ArrayAdapter<CharSequence> turnoAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.turnos_array,
                android.R.layout.simple_spinner_item
        );
        turnoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        turnoSpinner.setAdapter(turnoAdapter);

        cargarEmpleado();

        updateButton.setOnClickListener(view -> {
            String nombre = nombreInput.getText().toString().trim();
            String apellido = apellidoInput.getText().toString().trim();
            String especialidad = especialidadSpinner.getSelectedItem().toString();
            String turno = turnoSpinner.getSelectedItem().toString();
            String salarioStr = salarioInput.getText().toString().trim();
            String dniStr = dniInput.getText().toString().trim();

            if (nombre.isEmpty() || apellido.isEmpty() || salarioStr.isEmpty() || dniStr.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    double salario = Double.parseDouble(salarioStr);
                    int dni = Integer.parseInt(dniStr);
                    if (db.actualizarEmpleado(empleadoId, nombre, apellido, especialidad, turno, salario, dni)) {
                        Toast.makeText(this, "Empleado actualizado", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, "Error al actualizar empleado", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "DNI y salario deben ser números válidos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelButton.setOnClickListener(view -> finish());

        deleteButton.setOnClickListener(view -> mostrarConfirmacionEliminacion());
    }

    private void cargarEmpleado() {
        Cursor cursor = db.obtenerEmpleados();
        while (cursor.moveToNext()) {
            if (cursor.getInt(0) == empleadoId) {
                nombreInput.setText(cursor.getString(1));
                apellidoInput.setText(cursor.getString(2));

                String especialidad = cursor.getString(3);
                ArrayAdapter<CharSequence> especialidadAdapter = (ArrayAdapter<CharSequence>) especialidadSpinner.getAdapter();
                especialidadSpinner.setSelection(especialidadAdapter.getPosition(especialidad));

                String turno = cursor.getString(4);
                ArrayAdapter<CharSequence> turnoAdapter = (ArrayAdapter<CharSequence>) turnoSpinner.getAdapter();
                turnoSpinner.setSelection(turnoAdapter.getPosition(turno));

                salarioInput.setText(String.valueOf(cursor.getDouble(5)));
                dniInput.setText(String.valueOf(cursor.getInt(6))); // Carga el DNI
                break;
            }
        }
        cursor.close();
    }

    private void mostrarConfirmacionEliminacion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmar Eliminación");
        builder.setMessage("¿Estás seguro de que deseas eliminar este empleado?");
        builder.setPositiveButton("Sí", (dialog, which) -> {
            if (db.eliminarEmpleado(empleadoId)) {
                Toast.makeText(this, "Empleado eliminado", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error al eliminar empleado", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}
