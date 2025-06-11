package ui;

import com.formdev.flatlaf.FlatDarkLaf;
import database.AcessoDAO;
import database.UrlDAO;
import database.UsuarioDAO;
import net.miginfocom.swing.MigLayout;
import database.UsuarioDAO.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import ADT.*;
import ui.ImagemAvisos.AvisoVisualFactory;
import ui.ImagemAvisos.EsteiraAvisos;

import static database.UrlDAO.garantirUrlRegistrada;
import static database.UsuarioDAO.acessosSuspeitosSeteDias;
import static database.UsuarioDAO.ultimoAcesso;

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

            JLabel appLabel = new JLabel("Teste");
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

            buscarPanel.add(DashboardCard.createDashboardCard("Acessos Suspeitos - Últimos sete dias", String.valueOf(acessosSuspeitosSeteDias(idUsuario)), new Color(204, 0, 0)), "growx");
            buscarPanel.add(DashboardCard.createDashboardCard("Último Acesso", ultimoAcesso(idUsuario), new Color(0, 102, 204)), "growx");


            JPanel buscaPanel = new JPanel(new MigLayout("", "[grow][100][100]", ""));
            buscaPanel.setOpaque(false);
            JTextField urlField = new JTextField();
            JButton buscarBtn = new JButton("Buscar");
            JButton limparBtn = new JButton("Limpar");
            buscaPanel.add(urlField, "growx");
            buscaPanel.add(buscarBtn);
            buscaPanel.add(limparBtn);

            // Painel de avisos com bordas arredondadas
            JPanel avisoBuscaPanel = new JPanel(new BorderLayout()) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getBackground());
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                    g2.dispose();
                }

                @Override
                protected void paintBorder(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(80, 80, 80));
                    g2.setStroke(new BasicStroke(1.5f));
                    g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
                    g2.dispose();
                }
            };

            buscarPanel.add(avisoBuscaPanel, "span 2, growx");
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



            //buscarPanel.add(avisoBuscaPanel, "span 2, growx");

            avisoBuscaPanel.setOpaque(false);
            avisoBuscaPanel.setBackground(new Color(60, 60, 60));
            avisoBuscaPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

            // Área de texto com bordas internas arredondadas
            JTextArea avisoBuscaArea = new JTextArea("Insira uma URL e clique em Buscar para verificar.") {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getBackground());
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                    g2.dispose();
                    super.paintComponent(g);
                }

                @Override
                protected void paintBorder(Graphics g) {
                    // Remove a borda padrão
                }
            };

            avisoBuscaArea.setEditable(false);
            avisoBuscaArea.setLineWrap(true);
            avisoBuscaArea.setWrapStyleWord(true);
            avisoBuscaArea.setBackground(new Color(60, 60, 60));
            avisoBuscaArea.setForeground(Color.WHITE);
            avisoBuscaArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            avisoBuscaArea.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

            JScrollPane avisoBuscaScroll = new JScrollPane(avisoBuscaArea) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getBackground());
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            avisoBuscaScroll.setBorder(null);
            avisoBuscaScroll.setOpaque(false);
            avisoBuscaScroll.getViewport().setOpaque(false);

            // Título com bordas arredondadas apenas na parte superior
            JLabel avisoBuscaTitulo = new JLabel(" VERIFICAÇÃO DE URL ") {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getBackground());
                    g2.fillRoundRect(0, 0, getWidth(), getHeight()+5, 20, 20); // +5 para cobrir melhor
                    g2.dispose();
                    super.paintComponent(g);
                }
            };

            avisoBuscaTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
            avisoBuscaTitulo.setForeground(new Color(220, 220, 220));
            avisoBuscaTitulo.setBackground(new Color(70, 70, 70));
            avisoBuscaTitulo.setOpaque(false); // Já estamos pintando o fundo manualmente
            avisoBuscaTitulo.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

            // Adiciona componentes ao painel
            avisoBuscaPanel.add(avisoBuscaTitulo, BorderLayout.NORTH);
            avisoBuscaPanel.add(avisoBuscaScroll, BorderLayout.CENTER);

            // Adiciona à interface (acima do campo Buscar URL)
            // Re-adiciona os componentes na ordem correta

            //buscarPanel.add(avisoBuscaPanel, "span 2, growx");
            buscarPanel.add(createSectionPanel("Histórico de Acessos", tabelaScroll), "growx");
            //buscarPanel.add(createSectionPanel("Avisos Dinâmicos", avisoScroll), "growx, wrap");
            //todo: adicionar rotina de carrossel com imagens e links
            //https://raw.githubusercontent.com/victoroscar30/avisos-imagens-publicas/main/Phishing_1.png

            // Carrega os avisos visuais com imagem e link
            ListaDuplamenteLigadaAvisos lista = AvisoVisualFactory.carregarAvisosVisuais();
            EsteiraAvisos esteira = new EsteiraAvisos(lista);

            // Adiciona no painel de busca
            buscarPanel.add(createSectionPanel("Avisos Dinâmicos", esteira), "growx, wrap");



            // === TELA CONTA ===
            /*JPanel atualizarPanel = new JPanel(new MigLayout("wrap 2", "[][grow]", "[]10[]10[]"));
            atualizarPanel.setOpaque(false);
            atualizarPanel.setBackground(new Color(45, 45, 45));
            atualizarPanel.add(new JLabel("Novo Email:"));
            atualizarPanel.add(new JTextField());
            atualizarPanel.add(new JLabel("Nova Senha:"));
            atualizarPanel.add(new JPasswordField());
            atualizarPanel.add(new JLabel("Confirmar Nova Senha:"));
            atualizarPanel.add(new JPasswordField());
            atualizarPanel.add(new JLabel());
            atualizarPanel.add(new JButton("Atualizar"));
            JPanel contaPanel = new JPanel(new BorderLayout());
            contaPanel.setBackground(new Color(45, 45, 45));
            contaPanel.add(createSectionPanel("Atualizar Conta", atualizarPanel), BorderLayout.CENTER);*/

            //int idUsuario = /* ID do usuário logado */;

            // Criar componentes
            JPanel atualizarPanel = new JPanel(new MigLayout("wrap 2", "[][grow]", "[]10[]10[]10[]10[]10"));
            atualizarPanel.setOpaque(false);
            atualizarPanel.setBackground(new Color(45, 45, 45));

            atualizarPanel.add(new JLabel("Nova Senha:"));
            JPasswordField novaSenhaField = new JPasswordField();
            novaSenhaField.setPreferredSize(new Dimension(200, 25));
            atualizarPanel.add(novaSenhaField);

            atualizarPanel.add(new JLabel("Confirmar Nova Senha:"));
            JPasswordField confirmarSenhaField = new JPasswordField();
            confirmarSenhaField.setPreferredSize(new Dimension(200, 25));
            atualizarPanel.add(confirmarSenhaField);

            // Label de erro
            JLabel senhaErroLabel = new JLabel(" ");
            senhaErroLabel.setForeground(Color.RED);
            senhaErroLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            atualizarPanel.add(senhaErroLabel, "skip, growx, wrap");

            // Label de sucesso
            JLabel sucessoLabel = new JLabel(" ");
            sucessoLabel.setForeground(new Color(0, 180, 0));
            sucessoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            atualizarPanel.add(sucessoLabel, "skip, growx, wrap");

            // Botão
            atualizarPanel.add(new JLabel());
            JButton atualizarButton = new JButton("Atualizar Senha");
            atualizarPanel.add(atualizarButton);

            // Painel principal
            JPanel contaPanel = new JPanel(new BorderLayout());
            contaPanel.setBackground(new Color(45, 45, 45));
            contaPanel.add(createSectionPanel("Atualizar Senha", atualizarPanel), BorderLayout.CENTER);

            // Lógica do botão
            atualizarButton.addActionListener(e -> {
                String novaSenha = new String(novaSenhaField.getPassword());
                String confirmarSenha = new String(confirmarSenhaField.getPassword());

                if (novaSenha.isBlank() || confirmarSenha.isBlank()) {
                    senhaErroLabel.setText("Todos os campos devem ser preenchidos.");
                    sucessoLabel.setText(" ");
                } else if (!novaSenha.equals(confirmarSenha)) {
                    senhaErroLabel.setText("As senhas não conferem. Por favor, tente novamente.");
                    sucessoLabel.setText(" ");
                } else {
                    UsuarioDAO dao = new UsuarioDAO();
                    boolean atualizado = dao.atualizarSenhaUsuario(idUsuario, novaSenha);

                    if (atualizado) {
                        senhaErroLabel.setText(" ");
                        sucessoLabel.setText("Senha atualizada com sucesso!");
                        novaSenhaField.setText("");
                        confirmarSenhaField.setText("");
                    } else {
                        senhaErroLabel.setText("Erro ao atualizar senha. Tente novamente.");
                        sucessoLabel.setText(" ");
                    }
                }
            });




            // === Adicionar ao CardLayout ===
            contentPanel.add(buscarPanel, "BUSCAR");
            contentPanel.add(contaPanel, "CONTA");

            // === Botões de navegação ===
            JButton btnBuscar = new JButton("Buscar");
            JButton btnConta = new JButton("Conta");
            JButton btnAdmin = new JButton("Modo Admin");
            JButton btnSair = new JButton("Sair");

            // Verifica se o usuário é admin
            UsuarioDAO dao = new UsuarioDAO();
            boolean isAdmin = dao.isUsuarioAdmin(idUsuario); // idUsuario vem do login

            // Se não for admin, o botão será invisível
            btnAdmin.setVisible(isAdmin);

            JButton[] navButtons = {btnBuscar, btnConta,btnAdmin};
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
            //TODO: ALGUMA COISA
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

                            //TODO: colocar função que vai considerar as setas do teclado para busca
                            if (sugestoes.size() > 8) {
                                MenuScroller.setScrollerFor(sugestoesPopup, 8, 150);
                            }

                            sugestoesPopup.show(urlField, 0, urlField.getHeight());
                        }
                    }
                }
            });

            btnAdmin.addActionListener(e -> {
                new AdminPanel(); // Abre a nova tela
            });

            buscarBtn.addActionListener(e -> {
                String urlDigitada = urlField.getText().trim();
                if (!urlDigitada.isEmpty()) {
                    try {
                        int idUrl = garantirUrlRegistrada(urlDigitada);

                        if (idUrl > 0) {
                            AcessoDAO acessoDAO = new AcessoDAO();
                            //boolean isMaliciosa = acessoDAO.registrarAcesso(idUsuario, idUrl, false);


                            // Verifica se é maliciosa (ajuste conforme seu DAO)
                            UrlDAO urlDAO = new UrlDAO();
                            String tipoAmeaca = urlDAO.getTipoAmeaca(urlDigitada);
                            String descricaoRisco = UrlDAO.getMensagemAviso(tipoAmeaca);
                            acessoDAO.registrarAcesso(idUsuario, idUrl, !tipoAmeaca.equals("segura"));
                            atualizarTabelaAcessos(tabela, idUsuario);

                            // Atualiza o aviso
                            if (!tipoAmeaca.equals("segura")) {
                                avisoBuscaArea.setText(" ALERTA: URL MALICIOSA DETECTADA!\n\n" +
                                        "• Tipo: " + tipoAmeaca + "\n" +
                                        "• Riscos: " + descricaoRisco + "\n\n" +
                                        "Recomendação: Não acesse este site!");
                                avisoBuscaArea.setBackground(new Color(80, 40, 40));
                                avisoBuscaArea.setForeground(new Color(255, 180, 180));
                                avisoBuscaTitulo.setText(" ATENÇÃO - URL PERIGOSA ");
                                avisoBuscaTitulo.setBackground(new Color(150, 40, 40));
                            } else {
                                avisoBuscaArea.setText("URL SEGURA\n\n" +
                                        "A URL '" + urlDigitada + "' foi verificada e não apresenta " +
                                        "ameaças conhecidas em nosso banco de dados.");
                                avisoBuscaArea.setBackground(new Color(40, 60, 40));
                                avisoBuscaArea.setForeground(new Color(180, 255, 180));
                                avisoBuscaTitulo.setText(" VERIFICAÇÃO CONCLUÍDA ");
                                avisoBuscaTitulo.setBackground(new Color(40, 80, 40));
                            }

                            // Atualiza dashboard
                            buscarPanel.removeAll();
                            buscarPanel.add(DashboardCard.createDashboardCard("Acessos Suspeitos - Últimos sete dias",
                                    String.valueOf(acessosSuspeitosSeteDias(idUsuario)), new Color(204, 0, 0)), "growx");
                            buscarPanel.add(DashboardCard.createDashboardCard("Último Acesso",
                                    ultimoAcesso(idUsuario), new Color(0, 102, 204)), "growx");

                            // Re-adiciona os componentes na ordem correta
                            buscarPanel.add(avisoBuscaPanel, "span 2, growx");
                            buscarPanel.add(createSectionPanel("Buscar URL", buscaPanel), "span 2, growx");
                            buscarPanel.add(createSectionPanel("Histórico de Acessos", tabelaScroll), "growx");
                            buscarPanel.add(createSectionPanel("Avisos Dinâmicos", esteira), "growx, wrap");

                            buscarPanel.revalidate();
                            buscarPanel.repaint();
                        }
                    } catch (Exception ex) {
                        avisoBuscaArea.setText("ERRO NA VERIFICAÇÃO\n\n" +
                                "Ocorreu um erro ao verificar a URL: " +
                                ex.getMessage());
                        avisoBuscaArea.setBackground(new Color(60, 50, 40));
                        avisoBuscaArea.setForeground(new Color(255, 200, 150));
                        avisoBuscaTitulo.setText(" ERRO NA VERIFICAÇÃO ");
                        avisoBuscaTitulo.setBackground(new Color(100, 60, 0));
                    }
                } else {
                    avisoBuscaArea.setText("ℹ️ Por favor, insira uma URL no campo acima e clique em Buscar " +
                            "para verificar sua segurança.");
                    avisoBuscaArea.setBackground(new Color(60, 60, 60));
                    avisoBuscaArea.setForeground(Color.WHITE);
                    avisoBuscaTitulo.setText(" INFORMAÇÃO ");
                    avisoBuscaTitulo.setBackground(new Color(70, 70, 70));
                }
            });

            /*limparBtn.setFocusPainted(false);
            limparBtn.setBackground(new Color(80, 80, 80));
            limparBtn.setForeground(Color.WHITE);
            limparBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            limparBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));*/

            limparBtn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    limparBtn.setBackground(new Color(100, 100, 100));
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    limparBtn.setBackground(new Color(80, 80, 80));
                }
            });

            limparBtn.addActionListener(e -> {
                // Limpa o campo de URL
                urlField.setText("");

                // Reseta a área de avisos para o estado inicial
                avisoBuscaArea.setText("Insira uma URL e clique em Buscar para verificar.");
                avisoBuscaArea.setBackground(new Color(60, 60, 60));
                avisoBuscaArea.setForeground(Color.WHITE);
                avisoBuscaTitulo.setText(" ATENÇÃO - VERIFICAÇÃO DE URL ");
                avisoBuscaTitulo.setBackground(new Color(70, 70, 70));

                // Atualiza a tabela para mostrar apenas o histórico
                atualizarTabelaAcessos(tabela, idUsuario);

                // Foca no campo de URL para nova digitação
                urlField.requestFocus();
            });



            /*buscarBtn.addActionListener(e -> {
                String urlDigitada = urlField.getText().trim();
                if (!urlDigitada.isEmpty()) {
                    try {
                        int idUrl = garantirUrlRegistrada(urlDigitada);

                        if (idUrl > 0) {
                            AcessoDAO acessoDAO = new AcessoDAO();
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
            });*/

            mainPanel.add(sidebar, BorderLayout.WEST);
            mainPanel.add(contentPanel, BorderLayout.CENTER);

            frame.setContentPane(mainPanel);
            frame.setVisible(true);
        });
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
        label.setPreferredSize(new Dimension(200, 25)); // ou maior se necessário


        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.add(label, BorderLayout.WEST);

        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(header, BorderLayout.NORTH);
        panel.add(conteudo, BorderLayout.CENTER);

        return panel;
    }

    private static void atualizarTabelaAcessos(JTable tabela, int idUsuario) {
        AcessoDAO acessoDAO = new AcessoDAO();
        List<model.Acesso> acessos = acessoDAO.listarAcessosPorUsuario(idUsuario);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        String[] colunas = {"URL", "Data"};
        Object[][] dados = new Object[acessos.size()][3];

        for (int i = 0; i < acessos.size(); i++) {
            model.Acesso ac = acessos.get(i);
            dados[i][0] = ac.getUrl();

            if (ac.getData() instanceof LocalDateTime) {
                dados[i][1] = ((LocalDateTime) ac.getData()).format(formatter);
            } else {
                dados[i][1] = String.valueOf(ac.getData());
            }

            //dados[i][1] = ac.getData();
            //dados[i][2] = ac.isSuspeito();
        }

        tabela.setModel(new javax.swing.table.DefaultTableModel(dados, colunas));
    }

    public static class DashboardCard extends JPanel {
        private Color backgroundColor;

        public DashboardCard(String title, String value, Color color) {
            super(new BorderLayout());
            this.backgroundColor = color;
            setOpaque(false); // importante para desenhar o fundo customizado

            setPreferredSize(new Dimension(200, 100));
            setBorder(new EmptyBorder(10, 15, 10, 15)); // espaçamento interno

            JLabel titleLabel = new JLabel(title);
            titleLabel.setForeground(Color.WHITE);
            titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            titleLabel.setHorizontalAlignment(SwingConstants.CENTER);  // centralizar título

            JLabel valueLabel = new JLabel(value);
            valueLabel.setForeground(Color.WHITE);
            valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
            valueLabel.setHorizontalAlignment(SwingConstants.CENTER); // centralizar valor

            add(titleLabel, BorderLayout.NORTH);
            add(valueLabel, BorderLayout.CENTER);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();

            // Habilita antialiasing para suavizar bordas
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Preenche fundo arredondado com cor de fundo
            g2.setColor(backgroundColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

            // Opcional: desenha borda arredondada branca (espessura 2)
            g2.setStroke(new BasicStroke(2f));
            g2.setColor(Color.WHITE);
            g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 20, 20);

            g2.dispose();

            super.paintComponent(g);
        }

        // Método estático para facilitar criação do card
        public static JPanel createDashboardCard(String title, String value, Color color) {
            return new DashboardCard(title, value, color);
        }
    }
}
