package database;

import models.Usuario;
import java.sql.*;

public class UsuarioDAO {

    public boolean salvarUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nome, email, senha, ip_origem, genero) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = Conexao.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha()); // Em produção, use hash!
            stmt.setString(4, usuario.getIpOrigem());
            stmt.setString(5, usuario.getGenero());

            int linhas = stmt.executeUpdate();
            return linhas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Usuario buscarPorEmailESenha(String email, String senha) {
        String sql = "SELECT * FROM usuarios WHERE email = ? AND senha = ?";
        try (Connection con = Conexao.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, senha); // Em produção, use hash!

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setNome(rs.getString("nome"));
                u.setEmail(rs.getString("email"));
                u.setSenha(rs.getString("senha"));
                u.setIpOrigem(rs.getString("ip_origem"));
                u.setTipo(rs.getString("tipo"));
                u.setTipo(rs.getString("genero"));
                return u;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Não encontrado
    }

    public boolean atualizarTipoUsuario(int idUsuario, String novoTipo) {
        String sql = "UPDATE usuarios SET tipo = ? WHERE id = ?";
        try (Connection con = Conexao.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, novoTipo);
            stmt.setInt(2, idUsuario);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static int acessosSuspeitosSeteDias(int id_usuario){
        String query = "SELECT COUNT(*) \n" +
                        "  FROM ACESSOS A \n" +
                        "  INNER JOIN USUARIOS B ON A.ID_USUARIO = B.ID \n" +
                        "  INNER JOIN URLS C ON A.ID_URL = C.ID\n" +
                        "  WHERE DATA_ACESSO >= NOW() - INTERVAL 7 DAY AND A.SUSPEITO = 1 \n" +
                        "    AND A.ID_USUARIO = ?";

        try (Connection con = Conexao.conectar();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setInt(1, id_usuario);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static String ultimoAcesso (int id_usuario){
        String query = "SELECT (DATE_FORMAT(MAX(DATA_ACESSO), '%d/%m/%Y')) AS DATA\n" +
                "  FROM ACESSOS A \n" +
                "  WHERE A.ID_USUARIO = ?";

        try (Connection con = Conexao.conectar();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setInt(1, id_usuario);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString(1);
            }
            return "";

        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
    }

    public boolean atualizarSenhaUsuario(int idUsuario, String novaSenha) {
        String sql = "UPDATE usuarios SET senha = ? WHERE id = ?";
        try (Connection con = Conexao.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, novaSenha);
            stmt.setInt(2, idUsuario);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isUsuarioAdmin(int idUsuario) {
        String sql = "SELECT tipo FROM usuarios WHERE id = ?";
        try (Connection con = Conexao.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String tipo = rs.getString("tipo");
                return tipo != null && tipo.equalsIgnoreCase("admin");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
