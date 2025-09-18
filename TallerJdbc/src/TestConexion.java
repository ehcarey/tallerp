import db.Database;
import java.sql.Connection;

public class TestConexion {
    public static void main(String[] args) {
        try (Connection conn = Database.getConnection()) {
            if (conn != null) {
                System.out.println("✅ Conexión exitosa a la base de datos");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
