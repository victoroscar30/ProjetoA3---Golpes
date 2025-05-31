package ui;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

import static database.Conexao.conectar;

public class AlertaDialog extends JDialog {
    private JTextField idAcessoField;
    private JTextArea mensagemArea;
    private JButton salvarBtn;

    private Integer idAlerta;

    public AlertaDialog(JFrame parent, Integer idAlerta) {
        super(parent, true);
        this.idAlerta = idAlerta;
        setTitle(idAlerta == null ? "Inserir Alerta" : "Editar Alerta");
        setSize(400, 250);
        setLayout(new GridLayout(3, 2));

        add(new JLabel("ID Acesso:"));
        idAcessoField = new JTextField();
        add(idAcessoField);

        add(new JLabel("Mensagem:"));
        mensagemArea = new JTextArea(5, 20);
        add(new JScrollPane(mensagemArea));

        salvarBtn = new JButton("Salvar");
        add(salvarBtn);

        salvarBtn.addActionListener(e -> salvar());

        if (idAlerta != null) carregarDados();
    }

    private void carregarDados() {
        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM alertas WHERE id = ?")) {
            stmt.setInt(1, idAlerta);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                idAcessoField.setText(rs.getString("id_acesso"));
                mensagemArea.setText(rs.getString("mensagem"));
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void salvar() {
        String sql = (idAlerta == null) ?
                "INSERT INTO alertas (id_acesso, mensagem) VALUES (?, ?)" :
                "UPDATE alertas SET id_acesso=?, mensagem=? WHERE id=?";
        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(idAcessoField.getText()));
            stmt.setString(2, mensagemArea.getText());
            if (idAlerta != null) stmt.setInt(3, idAlerta);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Alerta salvo com sucesso!");
            dispose();
        } catch (Exception e) { JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage()); }
    }
}
