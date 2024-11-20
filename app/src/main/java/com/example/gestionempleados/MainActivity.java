package com.example.gestionempleados;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Button crudUsuariosButton, crudEmpleadosButton;
    private TextView welcomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        crudUsuariosButton = findViewById(R.id.crudUsuariosButton);
        crudEmpleadosButton = findViewById(R.id.crudEmpleadosButton);
        welcomeText = findViewById(R.id.welcomeText);

        // Obtener el nombre del usuario desde el Intent
        String userName = getIntent().getStringExtra("USER_NAME");
        if (userName != null) {
            welcomeText.setText("Bienvenido, " + userName);
        } else {
            welcomeText.setText("Bienvenido");
        }

        crudUsuariosButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, CrudUsuariosActivity.class);
            startActivity(intent);
        });

        crudEmpleadosButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, CrudEmpleadosActivity.class);
            startActivity(intent);
        });
    }
}

