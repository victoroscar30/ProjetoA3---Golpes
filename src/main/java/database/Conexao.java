package database;
import java.sql.*;

public class Conexao {
    private static final String URL = "jdbc:mysql://localhost:3306/projeto_golpes";
    private static final String USER = "gustavinho";
    private static final String PASSWORD = "123456";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
