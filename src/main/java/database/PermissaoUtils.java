package database;

import javax.swing.*;
import java.sql.*;

import static database.Conexao.conectar;
import ui.AdminPanel;

public class PermissaoUtils {

    public static void verificarPermissaoAdmin(String nomeUsuario, int idUsuario) {
        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement("SELECT tipo FROM usuarios WHERE id = ?")) {

            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String tipo = rs.getString("tipo");
                if ("admin".equalsIgnoreCase(tipo)) {
                    abrirPainelAdmin(nomeUsuario, idUsuario);
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Este usuário não é um Administrador, portanto, não pode acessar esta tela.",
                            "Acesso Negado",
                            JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null,
                        "Usuário não encontrado no banco de dados.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Erro ao verificar permissão: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void abrirPainelAdmin(String nomeUsuario, int idUsuario) {
        SwingUtilities.invokeLater(() -> {
            AdminPanel painel = new AdminPanel(nomeUsuario, idUsuario);
            painel.setVisible(true);
        });
    }
}
