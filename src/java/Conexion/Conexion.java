package conexion;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Conexion {
    private static final Logger LOGGER = Logger.getLogger(Conexion.class.getName());

    public Connection connectToDatabase() {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ morganastore", "root", "123456789");
            System.out.println("Conexión exitosa a la base de datos!");
            return con;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al conectar a la base de datos", e);
            return null;
        }
    }

    public static void main(String[] args) {
        Conexion example = new Conexion();
        example.connectToDatabase();
    }
}
