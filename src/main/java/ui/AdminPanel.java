package ui;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

import static database.Conexao.conectar;

public class AdminPanel extends JFrame {

    private JComboBox<String> tabelaComboBox;
    private JTable tabela;
    private DefaultTableModel tableModel;
    private JButton btnInserir, btnEditar, btnExcluir;

    public AdminPanel() {
        setTitle("Painel Admin");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        JTabbedPane abas = new JTabbedPane();

        JPanel crudPanel = new JPanel(new BorderLayout());

        tabelaComboBox = new JComboBox<>(new String[]{"usuarios", "urls", "acessos", "alertas"});
        crudPanel.add(tabelaComboBox, BorderLayout.NORTH);

        tabela = new JTable();
        JScrollPane scroll = new JScrollPane(tabela);
        crudPanel.add(scroll, BorderLayout.CENTER);

        JPanel botoes = new JPanel();
        btnInserir = new JButton("Inserir");
        btnEditar = new JButton("Editar");
        btnExcluir = new JButton("Excluir");
        botoes.add(btnInserir);
        botoes.add(btnEditar);
        botoes.add(btnExcluir);
        crudPanel.add(botoes, BorderLayout.SOUTH);

        abas.addTab("CRUD", crudPanel);
        abas.addTab("Gráficos", new GraficoUrls());

        add(abas, BorderLayout.CENTER);

        // Eventos
        tabelaComboBox.addActionListener(e -> carregarTabela());
        btnInserir.addActionListener(e -> inserirRegistro());
        btnEditar.addActionListener(e -> editarRegistro());
        btnExcluir.addActionListener(e -> excluirRegistro());

        carregarTabela();
    }

    private void carregarTabela() {
        String tabelaSelecionada = (String) tabelaComboBox.getSelectedItem();
        String sql = "SELECT * FROM " + tabelaSelecionada;

        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            tableModel = TabelaUtils.construirTabela(rs);
            tabela.setModel(tableModel);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar tabela: " + e.getMessage());
        }
    }

    private void inserirRegistro() {
        JOptionPane.showMessageDialog(this, "Função de inserção não implementada.");
        // Aqui você pode abrir um JDialog com campos para inserir dependendo da tabela
    }

    private void editarRegistro() {
        JOptionPane.showMessageDialog(this, "Função de edição não implementada.");
        // Similar ao inserir, mas preenchendo os campos com os dados da linha selecionada
    }

    private void excluirRegistro() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma linha para excluir.");
            return;
        }

        String tabelaSelecionada = (String) tabelaComboBox.getSelectedItem();
        Object id = tabela.getValueAt(linha, 0);

        String sql = "DELETE FROM " + tabelaSelecionada + " WHERE id = ?";

        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, id);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Registro excluído.");
            carregarTabela();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir: " + e.getMessage());
        }
    }
}
