package ui;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

import static database.Conexao.conectar;

public class UrlDialog extends JDialog {
    private JTextField urlField, dominioField;
    private JComboBox<String> classificacaoCombo;
    private JButton salvarBtn;
    private JButton cancelarBtn;

    private Integer idUrl;

    public UrlDialog(JFrame parent, Integer idUrl) {
        super(parent, true);
        this.idUrl = idUrl;
        setTitle(idUrl == null ? "Inserir URL" : "Editar URL");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 2));

        add(new JLabel("URL:"));
        urlField = new JTextField();
        add(urlField);

        add(new JLabel("Domínio:"));
        dominioField = new JTextField();
        add(dominioField);

        add(new JLabel("Classificação:"));
        classificacaoCombo = new JComboBox<>(new String[]{"segura", "suspeita", "phishing", "desconhecida"});
        add(classificacaoCombo);

        salvarBtn = new JButton("Salvar");
        add(salvarBtn);
        salvarBtn.addActionListener(e -> salvar());

        cancelarBtn = new JButton("Cancelar");
        cancelarBtn.addActionListener(e -> dispose());
        add(cancelarBtn);


        if (idUrl != null) carregarDados();
    }

    private void carregarDados() {
        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM urls WHERE id = ?")) {
            stmt.setInt(1, idUrl);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                urlField.setText(rs.getString("url"));
                dominioField.setText(rs.getString("dominio"));
                classificacaoCombo.setSelectedItem(rs.getString("classificacao"));
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void salvar() {
        String sql = (idUrl == null) ?
                "INSERT INTO urls (url, dominio, classificacao) VALUES (?, ?, ?)" :
                "UPDATE urls SET url=?, dominio=?, classificacao=? WHERE id=?";
        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, urlField.getText());
            stmt.setString(2, dominioField.getText());
            stmt.setString(3, (String) classificacaoCombo.getSelectedItem());
            if (idUrl != null) stmt.setInt(4, idUrl);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "URL salva com sucesso!");
            dispose();
        } catch (Exception e) { JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage()); }
    }
}
