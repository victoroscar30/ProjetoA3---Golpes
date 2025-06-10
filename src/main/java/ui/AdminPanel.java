package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;


import static database.Conexao.conectar;
import static ui.UsuarioDashboardUI.mostrarTelaUsuario;

public class AdminPanel extends JFrame {
    private JComboBox<String> tabelaComboBox;
    private JTable dadosTable;
    private DefaultTableModel tableModel;

    private JButton inserirBtn, editarBtn, deletarBtn, atualizarBtn, trocarBtn, sairBtn;

    public AdminPanel() {
        setTitle("Painel Admin");
        setSize(1000, 700); // tamanho aumentado
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane abas = new JTabbedPane();

        // ---------- Aba de gerenciamento ----------
        JPanel gerenciamentoPanel = new JPanel(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());

        JPanel leftPanel = new JPanel();
        JLabel labelTabela = new JLabel("Tabela: ");
        leftPanel.add(labelTabela);
        tabelaComboBox = new JComboBox<>(new String[]{"usuarios", "urls", "acessos", "alertas"});
        leftPanel.add(tabelaComboBox);

        JPanel rightPanel = new JPanel();
        inserirBtn = new JButton("Inserir");
        editarBtn = new JButton("Editar");
        deletarBtn = new JButton("Deletar");
        atualizarBtn = new JButton("Atualizar");
        leftPanel.add(inserirBtn);
        leftPanel.add(editarBtn);
        leftPanel.add(deletarBtn);
        leftPanel.add(atualizarBtn);

        trocarBtn = new JButton("Alterar Visão - Comum");
        trocarBtn.setBackground(Color.GREEN);
        trocarBtn.setForeground(Color.BLACK);
        sairBtn = new JButton("Sair");
        sairBtn.setBackground(Color.RED);
        sairBtn.setForeground(Color.BLACK);
        rightPanel.add(sairBtn);
        rightPanel.add(trocarBtn);

        topPanel.add(leftPanel, BorderLayout.WEST);
        topPanel.add(rightPanel, BorderLayout.EAST);

        gerenciamentoPanel.add(topPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel();
        dadosTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(dadosTable);
        gerenciamentoPanel.add(scrollPane, BorderLayout.CENTER);

        atualizarTabela();

        tabelaComboBox.addActionListener(e -> atualizarTabela());
        atualizarBtn.addActionListener(e -> atualizarTabela());
        inserirBtn.addActionListener(e -> inserirRegistro());
        editarBtn.addActionListener(e -> editarRegistro());
        deletarBtn.addActionListener(e -> deletarRegistro());
        sairBtn.addActionListener(e -> dispose());
        trocarBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {  // Correct spelling
                dispose();  // Close current window
                mostrarTelaUsuario("Fulano", 1);  // Open new window
            }
        });

        abas.addTab("Gerenciamento", gerenciamentoPanel);

        // ---------- Aba de gráficos ----------
        JTabbedPane graficosTabs = new JTabbedPane();
        graficosTabs.addTab("Acessos por URL", new GraficoAcessosPanel());
        graficosTabs.addTab("Cadastros de Usuários", new GraficoUsuariosPanel());
        graficosTabs.addTab("Classificações", new GraficoUrls());
        graficosTabs.addTab("Top Acessos", new GraficoTopAcessos());
        abas.addTab("Gráficos", graficosTabs);

        add(abas);
    }

    private void atualizarTabela() {
        String tabela = (String) tabelaComboBox.getSelectedItem();
        try (Connection conn = conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM " + tabela)) {

            ResultSetMetaData metaData = rs.getMetaData();
            int colCount = metaData.getColumnCount();

            tableModel.setRowCount(0);
            tableModel.setColumnCount(0);

            for (int i = 1; i <= colCount; i++) {
                tableModel.addColumn(metaData.getColumnName(i));
            }

            while (rs.next()) {
                Object[] row = new Object[colCount];
                for (int i = 0; i < colCount; i++) {
                    row[i] = rs.getObject(i + 1);
                }
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
        }
    }

    private void inserirRegistro() {
        String tabela = (String) tabelaComboBox.getSelectedItem();
        switch (tabela) {
            case "usuarios":
                new UsuarioDialog(this, null).setVisible(true);
                break;
            case "urls":
                new UrlDialog(this, null).setVisible(true);
                break;
            case "acessos":
                new AcessoDialog(this, null).setVisible(true);
                break;
            case "alertas":
                new AlertaDialog(this, null).setVisible(true);
                break;
        }
        atualizarTabela();
    }

    private void editarRegistro() {
        int selectedRow = dadosTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um registro para editar.");
            return;
        }
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String tabela = (String) tabelaComboBox.getSelectedItem();

        switch (tabela) {
            case "usuarios":
                new UsuarioDialog(this, id).setVisible(true);
                break;
            case "urls":
                new UrlDialog(this, id).setVisible(true);
                break;
            case "acessos":
                new AcessoDialog(this, id).setVisible(true);
                break;
            case "alertas":
                new AlertaDialog(this, id).setVisible(true);
                break;
        }
        atualizarTabela();
    }

    private void deletarRegistro() {
        int selectedRow = dadosTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um registro para deletar.");
            return;
        }
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String tabela = (String) tabelaComboBox.getSelectedItem();

        int confirm = JOptionPane.showConfirmDialog(this, "Deseja realmente deletar o registro?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM " + tabela + " WHERE id = ?")) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Registro deletado com sucesso.");
            atualizarTabela();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarkLaf());
        } catch (Exception e) {
            System.err.println("Falha ao aplicar FlatLaf: " + e.getMessage());
        }
        SwingUtilities.invokeLater(() -> new AdminPanel().setVisible(true));
    }
}
