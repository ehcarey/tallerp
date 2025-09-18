package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    // valores según configuración de PostgreSQL
    private static final String URL = "jdbc:postgresql://localhost:5432/taller";
    private static final String USER = "postgres"; // usuario de postgres
    private static final String PASSWORD = "yunde2609"; //  contraseña de postgres

    // Método para obtener la conexión
     public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver"); // <- importante
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver PostgreSQL no encontrado", e);
        }
    }
}
