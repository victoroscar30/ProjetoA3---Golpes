package ui;

import com.formdev.flatlaf.FlatDarkLaf;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import ADT.*;

import static database.UrlDAO.garantirUrlRegistrada;

public class UsuarioDashboardUI {

    public static void main(String[] args) {
        mostrarTelaUsuario("Fulano", 1);
    }

    public static void mostrarTelaUsuario(String nomeUsuario, int idUsuario) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            TrieUrls trie = new TrieUrls(true);
            JTable tabela = new JTable();
            atualizarTabelaAcessos(tabela, idUsuario);

            JFrame frame = new JFrame("Dashboard - " + nomeUsuario);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 700);
            frame.setLocationRelativeTo(null);

            JPanel mainPanel = new JPanel(new BorderLayout());

            // Sidebar
            JPanel sidebar = new JPanel(new MigLayout("wrap 1, insets 20 10 20 10, gap 20, aligny center", "[grow, fill]"));
            sidebar.setPreferredSize(new Dimension(200, 0));
            sidebar.setBackground(new Color(30, 30, 30));

            JLabel appLabel = new JLabel("MyApp");
            appLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
            appLabel.setForeground(Color.WHITE);
            sidebar.add(appLabel);

            // Painel de conteúdo com CardLayout
            CardLayout cardLayout = new CardLayout();
            JPanel contentPanel = new JPanel(cardLayout);
            contentPanel.setBackground(new Color(45, 45, 45));

            // === TELA BUSCAR ===
            JPanel buscarPanel = new JPanel(new MigLayout("wrap 2, insets 20, gap 20", "[grow][grow]", "[]20[]20[]"));
            buscarPanel.setBackground(new Color(45, 45, 45));

            buscarPanel.add(createDashboardCard("Acessos Suspeitos", "5", new Color(204, 0, 0)), "growx");
            buscarPanel.add(createDashboardCard("Último Acesso", "2025-05-20", new Color(0, 102, 204)), "growx");

            JPanel buscaPanel = new JPanel(new MigLayout("", "[grow][100]", ""));
            buscaPanel.setOpaque(false);
            JTextField urlField = new JTextField();
            JButton buscarBtn = new JButton("Buscar");
            buscaPanel.add(urlField, "growx");
            buscaPanel.add(buscarBtn);

            buscarPanel.add(createSectionPanel("Buscar URL", buscaPanel), "span 2, growx");

            JScrollPane tabelaScroll = new JScrollPane(tabela);
            tabelaScroll.setPreferredSize(new Dimension(500, 150));

            JTextArea avisosArea = new JTextArea("Nenhum aviso encontrado.");
            avisosArea.setEditable(false);
            avisosArea.setLineWrap(true);
            avisosArea.setWrapStyleWord(true);
            avisosArea.setBackground(new Color(60, 60, 60));
            avisosArea.setForeground(Color.WHITE);
            JScrollPane avisoScroll = new JScrollPane(avisosArea);
            avisoScroll.setPreferredSize(new Dimension(500, 150));

            buscarPanel.add(createSectionPanel("Histórico de Acessos", tabelaScroll), "growx");
            buscarPanel.add(createSectionPanel("Avisos Dinâmicos", avisoScroll), "growx, wrap");

            // === TELA CONTA ===
            JPanel atualizarPanel = new JPanel(new MigLayout("wrap 2", "[][grow]", "[]10[]10[]"));
            atualizarPanel.setOpaque(false);
            atualizarPanel.setBackground(new Color(45, 45, 45));
            atualizarPanel.add(new JLabel("Novo Email:"));
            atualizarPanel.add(new JTextField());
            atualizarPanel.add(new JLabel("Nova Senha:"));
            atualizarPanel.add(new JPasswordField());
            atualizarPanel.add(new JLabel());
            atualizarPanel.add(new JButton("Atualizar"));
            JPanel contaPanel = new JPanel(new BorderLayout());
            contaPanel.setBackground(new Color(45, 45, 45));
            contaPanel.add(createSectionPanel("Atualizar Conta", atualizarPanel), BorderLayout.CENTER);

            // === Adicionar ao CardLayout ===
            contentPanel.add(buscarPanel, "BUSCAR");
            contentPanel.add(contaPanel, "CONTA");

            // === Botões de navegação ===
            JButton btnBuscar = new JButton("Buscar");
            JButton btnConta = new JButton("Conta");
            JButton btnSair = new JButton("Sair");

            JButton[] navButtons = {btnBuscar, btnConta};
            for (JButton btn : navButtons) {
                btn.setFocusPainted(false);
                btn.setBackground(new Color(60, 60, 60));
                btn.setForeground(Color.WHITE);
                btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

                btn.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered(java.awt.event.MouseEvent evt) {
                        btn.setBackground(new Color(80, 80, 80));
                    }
                    public void mouseExited(java.awt.event.MouseEvent evt) {
                        btn.setBackground(new Color(60, 60, 60));
                    }
                });

                sidebar.add(btn, "align center, growx");
            }

            btnSair.setFocusPainted(false);
            btnSair.setBackground(new Color(150, 30, 30));
            btnSair.setForeground(Color.WHITE);
            btnSair.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            btnSair.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            sidebar.add(Box.createVerticalGlue(), "growy, pushy");
            sidebar.add(btnSair, "align center, growx");

            btnBuscar.addActionListener(e -> cardLayout.show(contentPanel, "BUSCAR"));
            btnConta.addActionListener(e -> cardLayout.show(contentPanel, "CONTA"));
            btnSair.addActionListener(e -> {
                frame.dispose();
                System.exit(0);
            });

            // === Autocomplete
            JPopupMenu sugestoesPopup = new JPopupMenu();
            sugestoesPopup.setFocusable(false);
            urlField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    String texto = urlField.getText();
                    sugestoesPopup.setVisible(false);
                    sugestoesPopup.removeAll();
                    if (!texto.isEmpty()) {
                        List<String> sugestoes = trie.sugerir(texto);
                        if (!sugestoes.isEmpty()) {
                            for (String sugestao : sugestoes) {
                                JMenuItem item = new JMenuItem(sugestao);
                                item.addActionListener(ae -> {
                                    urlField.setText(sugestao);
                                    sugestoesPopup.setVisible(false);
                                });
                                sugestoesPopup.add(item);
                            }
                            sugestoesPopup.show(urlField, 0, urlField.getHeight());
                        }
                    }
                }
            });

            buscarBtn.addActionListener(e -> {
                String urlDigitada = urlField.getText().trim();
                if (!urlDigitada.isEmpty()) {
                    try {
                        int idUrl = garantirUrlRegistrada(urlDigitada);

                        if (idUrl > 0) {
                            dao.AcessoDAO acessoDAO = new dao.AcessoDAO();
                            acessoDAO.registrarAcesso(idUsuario, idUrl, false);
                            atualizarTabelaAcessos(tabela, idUsuario);
                        } else {
                            JOptionPane.showMessageDialog(frame, "Erro ao registrar URL.", "Erro", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(frame, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            mainPanel.add(sidebar, BorderLayout.WEST);
            mainPanel.add(contentPanel, BorderLayout.CENTER);

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
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setBackground(new Color(50, 50, 50));

        JLabel label = new JLabel(titulo);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setForeground(Color.WHITE);

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.add(label, BorderLayout.WEST);

        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(header, BorderLayout.NORTH);
        panel.add(conteudo, BorderLayout.CENTER);

        return panel;
    }

    private static void atualizarTabelaAcessos(JTable tabela, int idUsuario) {
        dao.AcessoDAO acessoDAO = new dao.AcessoDAO();
        List<model.Acesso> acessos = acessoDAO.listarAcessosPorUsuario(idUsuario);

        String[] colunas = {"URL", "Data", "Suspeito"};
        Object[][] dados = new Object[acessos.size()][3];

        for (int i = 0; i < acessos.size(); i++) {
            model.Acesso ac = acessos.get(i);
            dados[i][0] = ac.getUrl();
            dados[i][1] = ac.getData();
            dados[i][2] = ac.isSuspeito();
        }

        tabela.setModel(new javax.swing.table.DefaultTableModel(dados, colunas));
    }
}
