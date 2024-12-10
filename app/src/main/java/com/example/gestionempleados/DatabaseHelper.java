package com.example.gestionempleados;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "empleadosApp.db";
    private static final int DATABASE_VERSION = 11;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsuariosTable = "CREATE TABLE usuarios (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT, " +
                "usuario TEXT UNIQUE, " +
                "email TEXT, " +
                "password TEXT)";
        db.execSQL(createUsuariosTable);

        String createEmpleadosTable = "CREATE TABLE empleados (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT, " +
                "apellido TEXT, " +
                "especialidad TEXT, " +
                "turno TEXT, " +
                "salario REAL, " +
                "dni INTEGER UNIQUE)";
        db.execSQL(createEmpleadosTable);

        ContentValues adminUser = new ContentValues();
        adminUser.put("nombre", "Gerente");
        adminUser.put("usuario", "admin");
        adminUser.put("email", "admin@restaurante.com");
        adminUser.put("password", "admin");
        db.insert("usuarios", null, adminUser);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 11) {
            // Crear una tabla nueva con el campo dni como INTEGER UNIQUE
            String createEmpleadosTempTable = "CREATE TABLE empleados_new (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nombre TEXT, " +
                    "apellido TEXT, " +
                    "especialidad TEXT, " +
                    "turno TEXT, " +
                    "salario REAL, " +
                    "dni INTEGER UNIQUE)";
            db.execSQL(createEmpleadosTempTable);

            // Copiar datos de la tabla antigua
            db.execSQL("INSERT INTO empleados_new (id, nombre, apellido, especialidad, turno, salario) " +
                    "SELECT id, nombre, apellido, especialidad, turno, salario FROM empleados");

            // Eliminar la tabla antigua
            db.execSQL("DROP TABLE empleados");

            // Renombrar la tabla nueva
            db.execSQL("ALTER TABLE empleados_new RENAME TO empleados");
        }
    }

    public boolean registrarUsuario(String nombre, String usuario, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("usuario", usuario);
        values.put("email", email);
        values.put("password", password);
        long result = db.insert("usuarios", null, values);
        return result != -1;
    }

    public Cursor obtenerUsuarios() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM usuarios", null);
    }

    public boolean actualizarUsuario(int id, String nombre, String usuario, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("usuario", usuario);
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

    public boolean agregarEmpleado(String nombre, String apellido, String especialidad, String turno, double salario, int dni) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("apellido", apellido);
        values.put("especialidad", especialidad);
        values.put("turno", turno);
        values.put("salario", salario);
        values.put("dni", dni);
        long result = db.insert("empleados", null, values);
        return result != -1;
    }

    public Cursor obtenerEmpleados() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM empleados", null);
    }

    public boolean actualizarEmpleado(int id, String nombre, String apellido, String especialidad, String turno, double salario, int dni) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("apellido", apellido);
        values.put("especialidad", especialidad);
        values.put("turno", turno);
        values.put("salario", salario);
        values.put("dni", dni);
        int result = db.update("empleados", values, "id = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    public boolean eliminarEmpleado(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("empleados", "id = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    public Cursor buscarEmpleadoPorDni(int dni) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM empleados WHERE dni = ?", new String[]{String.valueOf(dni)});
    }
}
