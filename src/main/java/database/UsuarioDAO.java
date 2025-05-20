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
}
