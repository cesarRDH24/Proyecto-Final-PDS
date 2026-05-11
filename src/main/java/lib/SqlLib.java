package lib;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqlLib {

    private static SqlLib instance;
    private Connection con;

    private final String url = "jdbc:mysql://localhost:3306/pollosHermanos";
    private final String user = "root";
    private final String pass = "cesar1734";

    // 🔒 constructor privado (singleton)
    private SqlLib() throws SQLException {
        conectar();
    }

    // 🔁 instancia única
    public static SqlLib getInstance() throws SQLException {
        if (instance == null) {
            instance = new SqlLib();
        }
        return instance;
    }

    // 🔌 conexión
    private void conectar() throws SQLException {
        con = DriverManager.getConnection(url, user, pass);
        System.out.println("Conectado a Pollos Hermanos");
    }

    // 📡 obtener conexión
    public Connection getConnection() {
        return con;
    }

    // ❌ cerrar conexión (opcional)
    public void cerrar() throws SQLException {
        if (con != null && !con.isClosed()) {
            con.close();
        }
    }
}