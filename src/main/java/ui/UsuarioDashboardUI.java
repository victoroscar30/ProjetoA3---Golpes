package ui;

import com.formdev.flatlaf.FlatDarkLaf;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class UsuarioDashboardUI {

    public static void main(String[] args) {
        mostrarTelaUsuario("Fulano");
    }

    public static void mostrarTelaUsuario(String nomeUsuario) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Dashboard- " + nomeUsuario);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 700);
            frame.setLocationRelativeTo(null);

            JPanel mainPanel = new JPanel(new BorderLayout());

            // Sidebar
            JPanel sidebar = new JPanel(new MigLayout("wrap 1, insets 20 10 20 10, gap 20", "[fill]"));
            sidebar.setPreferredSize(new Dimension(200, 0));
            sidebar.setBackground(new Color(30, 30, 30));

            JLabel appLabel = new JLabel("MyApp");
            appLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
            appLabel.setForeground(Color.WHITE);
            sidebar.add(appLabel);

            String[] botoes = {"Dashboard", "Histórico", "Avisos", "Conta", "Sair"};
            for (String nome : botoes) {
                JButton btn = new JButton(nome);
                btn.setFocusPainted(false);
                btn.setBackground(new Color(60, 60, 60));
                btn.setForeground(Color.WHITE);
                btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                sidebar.add(btn, "growx");
            }

            // Painel central
            JPanel contentPanel = new JPanel(new MigLayout("wrap 2, insets 20, gap 20", "[grow][grow]", "[]20[]20[]"));
            contentPanel.setBackground(new Color(45, 45, 45));

            // Cards dashboard
            contentPanel.add(createDashboardCard("Acessos Suspeitos", "5", new Color(204, 0, 0)), "growx");
            contentPanel.add(createDashboardCard("Último Acesso", "2025-05-20", new Color(0, 102, 204)), "growx");

            // Busca de URL - movido abaixo dos cards
            JPanel buscaPanel = new JPanel(new MigLayout("", "[grow][100]", ""));
            buscaPanel.setOpaque(false);
            JTextField urlField = new JTextField();
            JButton buscarBtn = new JButton("Buscar");
            buscaPanel.add(urlField, "growx");
            buscaPanel.add(buscarBtn);
            contentPanel.add(createSectionPanel("Buscar URL", buscaPanel), "span 2, growx");

            // Tabela Histórico de Acessos
            JTable tabela = new JTable(new Object[][]{
                    {"google.com", "2025-05-19", false},
                    {"phishing-site.com", "2025-05-18", true}
            }, new String[]{"URL", "Data", "Suspeito"});
            JScrollPane tabelaScroll = new JScrollPane(tabela);
            tabelaScroll.setPreferredSize(new Dimension(500, 200));
            contentPanel.add(createSectionPanel("Histórico de Acessos", tabelaScroll), "span 2, growx");

            // Avisos
            JTextArea avisosArea = new JTextArea("Nenhum aviso encontrado.");
            avisosArea.setEditable(false);
            avisosArea.setLineWrap(true);
            avisosArea.setWrapStyleWord(true);
            avisosArea.setBackground(new Color(60, 60, 60));
            avisosArea.setForeground(Color.WHITE);
            JScrollPane avisoScroll = new JScrollPane(avisosArea);
            avisoScroll.setPreferredSize(new Dimension(500, 150));
            contentPanel.add(createSectionPanel("Avisos Dinâmicos", avisoScroll), "span 2, growx");

            // Atualizar Conta
            JPanel atualizarPanel = new JPanel(new MigLayout("wrap 2", "[][grow]", "[]10[]10[]"));
            atualizarPanel.setOpaque(false);
            atualizarPanel.add(new JLabel("Novo Email:"));
            atualizarPanel.add(new JTextField());
            atualizarPanel.add(new JLabel("Nova Senha:"));
            atualizarPanel.add(new JPasswordField());
            atualizarPanel.add(new JLabel());
            atualizarPanel.add(new JButton("Atualizar"));
            contentPanel.add(createSectionPanel("Atualizar Conta", atualizarPanel), "span 2, growx");

            mainPanel.add(sidebar, BorderLayout.WEST);
            mainPanel.add(new JScrollPane(contentPanel), BorderLayout.CENTER);

            frame.setContentPane(mainPanel);
            frame.setVisible(true);
        });
    }

    private static JPanel createDashboardCard(String title, String value, Color color) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(color);
        panel.setPreferredSize(new Dimension(200, 100));
        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JLabel valueLabel = new JLabel(value);
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(valueLabel, BorderLayout.CENTER);
        return panel;
    }

    private static JPanel createSectionPanel(String titulo, JComponent conteudo) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(50, 50, 50));
        JLabel label = new JLabel(titulo);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setForeground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(label, BorderLayout.NORTH);
        panel.add(conteudo, BorderLayout.CENTER);
        return panel;
    }
}
