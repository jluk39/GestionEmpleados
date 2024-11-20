package com.example.gestionempleados;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "empleadosApp.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tabla de Usuarios
        String createUsuariosTable = "CREATE TABLE usuarios (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT, " +
                "email TEXT UNIQUE, " +
                "password TEXT)";
        db.execSQL(createUsuariosTable);

        // Tabla de Empleados
        String createEmpleadosTable = "CREATE TABLE empleados (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT, " +
                "apellido TEXT, " +
                "cargo TEXT, " +
                "salario REAL)";
        db.execSQL(createEmpleadosTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS usuarios");
        db.execSQL("DROP TABLE IF EXISTS empleados");
        onCreate(db);
    }

    // Métodos CRUD Usuarios
    public boolean registrarUsuario(String nombre, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("email", email);
        values.put("password", password);

        long result = db.insert("usuarios", null, values);
        return result != -1;
    }

    public Cursor obtenerUsuarios() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM usuarios", null);
    }

    public boolean actualizarUsuario(int id, String nombre, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("email", email);
        values.put("password", password);

        int result = db.update("usuarios", values, "id = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    public boolean eliminarUsuario(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("usuarios", "id = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    // Métodos CRUD Empleados
    public boolean agregarEmpleado(String nombre, String apellido, String cargo, double salario) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("apellido", apellido);
        values.put("cargo", cargo);
        values.put("salario", salario);

        long result = db.insert("empleados", null, values);
        return result != -1;
    }

    public Cursor obtenerEmpleados() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM empleados", null);
    }

    public boolean actualizarEmpleado(int id, String nombre, String apellido, String cargo, double salario) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("apellido", apellido);
        values.put("cargo", cargo);
        values.put("salario", salario);

        int result = db.update("empleados", values, "id = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    public boolean eliminarEmpleado(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("empleados", "id = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }
}
