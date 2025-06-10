package ui;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

import static database.Conexao.conectar;

public class AcessoDialog extends JDialog {
    private JTextField idUsuarioField, idUrlField;
    private JCheckBox suspeitoCheck;
    private JButton salvarBtn;
    private JButton cancelarBtn;

    private Integer idAcesso;

    public AcessoDialog(JFrame parent, Integer idAcesso) {
        super(parent, true);
        this.idAcesso = idAcesso;
        setTitle(idAcesso == null ? "Inserir Acesso" : "Editar Acesso");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 2));

        add(new JLabel("ID Usuário:"));
        idUsuarioField = new JTextField();
        add(idUsuarioField);

        add(new JLabel("ID URL:"));
        idUrlField = new JTextField();
        add(idUrlField);

        add(new JLabel("Suspeito:"));
        suspeitoCheck = new JCheckBox();
        add(suspeitoCheck);

        salvarBtn = new JButton("Salvar");
        add(salvarBtn);
        salvarBtn.addActionListener(e -> salvar());

        cancelarBtn = new JButton("Cancelar");
        cancelarBtn.addActionListener(e -> dispose());
        add(cancelarBtn);

        if (idAcesso != null) carregarDados();
    }

    private void carregarDados() {
        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM acessos WHERE id = ?")) {
            stmt.setInt(1, idAcesso);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                idUsuarioField.setText(rs.getString("id_usuario"));
                idUrlField.setText(rs.getString("id_url"));
                suspeitoCheck.setSelected(rs.getBoolean("suspeito"));
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void salvar() {
        String sql = (idAcesso == null) ?
                "INSERT INTO acessos (id_usuario, id_url, suspeito) VALUES (?, ?, ?)" :
                "UPDATE acessos SET id_usuario=?, id_url=?, suspeito=? WHERE id=?";
        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(idUsuarioField.getText()));
            stmt.setInt(2, Integer.parseInt(idUrlField.getText()));
            stmt.setBoolean(3, suspeitoCheck.isSelected());
            if (idAcesso != null) stmt.setInt(4, idAcesso);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Acesso salvo com sucesso!");
            dispose();
        } catch (Exception e) { JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage()); }
    }
}
