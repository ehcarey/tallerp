package Service;

import db.Database;
import model.Estudiante;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EstudianteService {

    // ✅ Verificar si un correo ya existe en la BD
    public boolean correoExiste(String correo) throws SQLException {
        String sql = "SELECT 1 FROM public.estudiantes WHERE correo = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, correo);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // true si encontró un registro
            }
        }
    }

    // ✅ Insertar estudiante (solo si el correo no existe)
    public void insertar(Estudiante estudiante) throws SQLException {
        if (correoExiste(estudiante.getCorreo())) {
            throw new SQLException("❌ El correo electrónico ya existe: " + estudiante.getCorreo());
        }

        String sql = "INSERT INTO public.estudiantes(nombre, apellido, correo, edad, estado_civil) VALUES (?, ?, ?, ?, ?::estado_civil_enum)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, estudiante.getNombre());
            stmt.setString(2, estudiante.getApellido());
            stmt.setString(3, estudiante.getCorreo());
            stmt.setInt(4, estudiante.getEdad());
            stmt.setString(5, estudiante.getEstadoCivil()); // debe coincidir con el ENUM en la BD
            stmt.executeUpdate();
        }
    }

    // ✅ Actualizar estudiante
    public void actualizar(Estudiante estudiante) throws SQLException {
        String sql = "UPDATE public.estudiantes SET nombre=?, apellido=?, edad=?, estado_civil=?::estado_civil_enum WHERE correo=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, estudiante.getNombre());
            stmt.setString(2, estudiante.getApellido());
            stmt.setInt(3, estudiante.getEdad());
            stmt.setString(4, estudiante.getEstadoCivil());
            stmt.setString(5, estudiante.getCorreo());
            stmt.executeUpdate();
        }
    }

    // ✅ Eliminar estudiante por correo
    public void eliminar(String correo) throws SQLException {
        String sql = "DELETE FROM public.estudiantes WHERE correo=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, correo);
            stmt.executeUpdate();
        }
    }

    // ✅ Listar todos los estudiantes
    public List<Estudiante> listar() throws SQLException {
        List<Estudiante> lista = new ArrayList<>();
        String sql = "SELECT * FROM public.estudiantes";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Estudiante(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("correo"),
                        rs.getInt("edad"),
                        rs.getString("estado_civil")
                ));
            }
        }
        return lista;
    }

    // ✅ Buscar estudiante por correo
    public Estudiante buscarPorCorreo(String correo) throws SQLException {
        String sql = "SELECT * FROM public.estudiantes WHERE correo=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, correo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Estudiante(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("apellido"),
                            rs.getString("correo"),
                            rs.getInt("edad"),
                            rs.getString("estado_civil")
                    );
                }
            }
        }
        return null;
    }
}
