import database.Conexao;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        try {
            Connection con = Conexao.conectar();
            System.out.println("Conexão com MySQL realizada com sucesso!");
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new AdminPanel().setVisible(true));
    }
}
//teste commit