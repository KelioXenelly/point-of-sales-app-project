import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class koneksi {
    private String url = "jdbc:mysql://localhost:3306/pboposdb";
    private String user = "root";
    private String password = "";
    private Connection conn;

    public void connect() {
        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Koneksi berhasil");
        } catch (SQLException ex) {
            conn = null; // Ensure conn is null on failure
            Logger.getLogger(koneksi.class.getName()).log(Level.SEVERE, "Database connection failed!", ex);
        }
    }

    public Connection getConnection() {
        return conn;
    }
}
