package ui;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

import static database.Conexao.conectar;

public class UsuarioDialog extends JDialog {
    private JTextField nomeField, emailField, senhaField, ipField;
    private JComboBox<String> generoCombo, tipoCombo;
    private JButton salvarBtn;

    private Integer idUsuario;

    public UsuarioDialog(JFrame parent, Integer idUsuario) {
        super(parent, true);
        this.idUsuario = idUsuario;
        setTitle(idUsuario == null ? "Inserir Usuário" : "Editar Usuário");
        setSize(400, 300);
        setLayout(new GridLayout(7, 2));

        add(new JLabel("Nome:"));
        nomeField = new JTextField();
        add(nomeField);

        add(new JLabel("Email:"));
        emailField = new JTextField();
        add(emailField);

        add(new JLabel("Senha:"));
        senhaField = new JTextField();
        add(senhaField);

        add(new JLabel("Gênero:"));
        generoCombo = new JComboBox<>(new String[]{"", "m", "f"});
        add(generoCombo);

        add(new JLabel("Tipo:"));
        tipoCombo = new JComboBox<>(new String[]{"comum", "admin"});
        add(tipoCombo);

        add(new JLabel("IP Origem:"));
        ipField = new JTextField();
        add(ipField);

        salvarBtn = new JButton("Salvar");
        add(salvarBtn);

        salvarBtn.addActionListener(e -> salvar());

        if (idUsuario != null) carregarDados();
    }

    private void carregarDados() {
        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM usuarios WHERE id = ?")) {
            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                nomeField.setText(rs.getString("nome"));
                emailField.setText(rs.getString("email"));
                senhaField.setText(rs.getString("senha"));
                generoCombo.setSelectedItem(rs.getString("genero"));
                tipoCombo.setSelectedItem(rs.getString("tipo"));
                ipField.setText(rs.getString("ip_origem"));
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void salvar() {
        String sql = (idUsuario == null) ?
                "INSERT INTO usuarios (nome, email, senha, genero, tipo, ip_origem) VALUES (?, ?, ?, ?, ?, ?)" :
                "UPDATE usuarios SET nome=?, email=?, senha=?, genero=?, tipo=?, ip_origem=? WHERE id=?";
        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nomeField.getText());
            stmt.setString(2, emailField.getText());
            stmt.setString(3, senhaField.getText());
            stmt.setString(4, (String) generoCombo.getSelectedItem());
            stmt.setString(5, (String) tipoCombo.getSelectedItem());
            stmt.setString(6, ipField.getText());
            if (idUsuario != null) stmt.setInt(7, idUsuario);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Usuário salvo com sucesso!");
            dispose();
        } catch (Exception e) { JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage()); }
    }
}
