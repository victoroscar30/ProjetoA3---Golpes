package database;

import model.Acesso;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AcessoDAO {

    public List<Acesso> listarAcessosPorUsuario(int idUsuario) {
        List<Acesso> lista = new ArrayList<>();
        String sql = """
        SELECT u.url, a.data_acesso, a.suspeito
        FROM acessos a
        JOIN urls u ON a.id_url = u.id
        WHERE a.id_usuario = ?
        ORDER BY a.data_acesso DESC
    """;

        try (Connection con = Conexao.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String url = rs.getString("url");
                Timestamp data = rs.getTimestamp("data_acesso");
                boolean suspeito = rs.getBoolean("suspeito");

                lista.add(new Acesso(url, data.toLocalDateTime(), suspeito));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }


    public void registrarAcesso(int idUsuario, int idUrl, boolean suspeito) {
        String sql = "INSERT INTO acessos (id_usuario, id_url, suspeito) VALUES (?, ?, ?)";

        try (Connection con = Conexao.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idUrl);
            stmt.setBoolean(3, suspeito); // se quiser assumir false por padrão: false

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
