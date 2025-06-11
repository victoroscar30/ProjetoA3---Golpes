package ui;

import com.formdev.flatlaf.FlatDarkLaf;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;
import models.Usuario;
import database.UsuarioDAO;
import java.net.InetAddress;

import static ui.UsuarioDashboardUI.mostrarTelaUsuario;


public class LoginUI {

    private static CardLayout cardLayout;
    private static JPanel cardsPanel;
    private static JToggleButton loginBtn;
    private static JToggleButton registerBtn;

    public static void TelaLogin() {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 600);
            frame.setLocationRelativeTo(null);

            // Main panel com 2 colunas (sidebar e cards)
            JPanel mainPanel = new JPanel(new MigLayout("fill, insets 0", "[30%][grow]", "fill"));

            // Sidebar com saudação no topo e botões centralizados verticalmente
            JPanel sidebar = new JPanel(new MigLayout(
                    "wrap 1, insets 10, gap 10, aligny center", // aligny center ajuda a centralizar verticalmente componentes dentro da célula
                    "[grow, fill]",
                    "[30px]10[50%]30" // 30px para a saudação, depois 50% altura para botões, 30px bottom gap
            ));
            sidebar.setBackground(new Color(25, 25, 25));

            // Label com saudação dinâmica no canto superior esquerdo
            JLabel greetingLabel = new JLabel(getGreeting());
            greetingLabel.setForeground(Color.WHITE);
            greetingLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            greetingLabel.setHorizontalAlignment(SwingConstants.LEFT);
            sidebar.add(greetingLabel, "growx, align left, wrap");

            // Botões toggle
            loginBtn = new JToggleButton("Log In");
            registerBtn = new JToggleButton("Sou novo aqui");

            loginBtn.setFocusPainted(false);
            registerBtn.setFocusPainted(false);
            loginBtn.setHorizontalAlignment(SwingConstants.CENTER);
            registerBtn.setHorizontalAlignment(SwingConstants.CENTER);

            loginBtn.setForeground(Color.WHITE);
            registerBtn.setForeground(Color.WHITE);
            loginBtn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            registerBtn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            loginBtn.setBorderPainted(false);
            registerBtn.setBorderPainted(false);

            sidebar.add(loginBtn, "growx");
            sidebar.add(registerBtn, "growx");

            Color defaultBg = new Color(25, 25, 25);
            Color selectedBg = new Color(135, 206, 250); // azul claro

            Runnable updateButtonColors = () -> {
                loginBtn.setBackground(loginBtn.isSelected() ? selectedBg : defaultBg);
                registerBtn.setBackground(registerBtn.isSelected() ? selectedBg : defaultBg);
            };

            updateButtonColors.run();

            loginBtn.addChangeListener(e -> updateButtonColors.run());
            registerBtn.addChangeListener(e -> updateButtonColors.run());

            ButtonGroup group = new ButtonGroup();
            group.add(loginBtn);
            group.add(registerBtn);
            loginBtn.setSelected(true);

            // Criar painel para os botões para facilitar centralização vertical
            JPanel toggleButtonsPanel = new JPanel(new MigLayout("wrap 1, gap 15, aligny center", "[grow, fill]", "[]20[]"));
            toggleButtonsPanel.setOpaque(false); // fundo transparente
            toggleButtonsPanel.add(loginBtn, "growx");
            toggleButtonsPanel.add(registerBtn, "growx");

            sidebar.add(toggleButtonsPanel, "growy, align center, pushy");

            // Cards
            cardLayout = new CardLayout();
            cardsPanel = new JPanel(cardLayout);

            cardsPanel.add(createLoginPanel(), "LOGIN");
            cardsPanel.add(createRegisterPanel(), "REGISTER");

            loginBtn.addActionListener(e -> cardLayout.show(cardsPanel, "LOGIN"));
            registerBtn.addActionListener(e -> cardLayout.show(cardsPanel, "REGISTER"));

            mainPanel.add(sidebar, "growy");
            mainPanel.add(cardsPanel, "grow");

            frame.setContentPane(mainPanel);
            frame.setVisible(true);
        });
    }

    // Método para retornar saudação de acordo com o horário local
    private static String getGreeting() {
        int hour = LocalTime.now().getHour();
        if (hour >= 5 && hour < 12) {
            return "Olá, bom dia";
        } else if (hour >= 12 && hour < 18) {
            return "Olá, boa tarde";
        } else {
            return "Olá, boa noite";
        }
    }

    private static JPanel createLoginPanel() {
        JPanel panel = new JPanel(new MigLayout("wrap 1", "[grow,fill]", "[]10[]10[]20[]"));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JTextField emailField = new JTextField(20);
        JPasswordField senhaField = new JPasswordField(20);

        panel.add(new JLabel("Email:"));
        panel.add(emailField);

        panel.add(new JLabel("Senha:"));
        panel.add(senhaField);

        // Label de erro visível no painel
        JLabel erroLoginLabel = new JLabel(" ");
        erroLoginLabel.setForeground(Color.RED);
        erroLoginLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panel.add(erroLoginLabel);

        JButton loginButton = new JButton("Log In");
        loginButton.setBackground(new Color(255, 140, 0));
        loginButton.setForeground(Color.WHITE);

        loginButton.addActionListener(e -> {
            String email = emailField.getText();
            String senha = new String(senhaField.getPassword());

            UsuarioDAO dao = new UsuarioDAO();
            Usuario usuario = dao.buscarPorEmailESenha(email, senha);

            if (usuario != null) {
                //JOptionPane.showMessageDialog(panel, "Bem-vindo, " + usuario.getNome() + "!", "Login bem-sucedido", JOptionPane.INFORMATION_MESSAGE);
                // Aqui você pode abrir a próxima tela, dashboard, etc.

                Window window = SwingUtilities.getWindowAncestor(panel);
                if (window != null) {
                    window.dispose(); // fecha a janela de login
                }

                if ("admin".equalsIgnoreCase(usuario.getTipo())) {
                    new AdminPanel();
                } else {
                    UsuarioDashboardUI.mostrarTelaUsuario(usuario.getNome(), usuario.getId());
                }
            } else {
                erroLoginLabel.setText("Email ou senha incorretos.");
            }
        });

        panel.add(loginButton, "growx");

        return panel;
    }

    private static JPanel createRegisterPanel() {
        JPanel panel = new JPanel(new MigLayout("wrap 1", "[grow,fill]", "[]10[]10[]10[]10[]10[]10[]20[]10[]"));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        panel.add(new JLabel("Nome:"));
        panel.add(new JTextField(20));

        panel.add(new JLabel("Email:"));
        panel.add(new JTextField(20));

        panel.add(new JLabel("Senha:"));
        JPasswordField senhaField = new JPasswordField(20);
        panel.add(senhaField);

        panel.add(new JLabel("Confirmar Senha:"));
        JPasswordField confirmarSenhaField = new JPasswordField(20);
        panel.add(confirmarSenhaField);

        JLabel senhaErroLabel = new JLabel(" ");
        senhaErroLabel.setForeground(Color.RED);
        senhaErroLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panel.add(senhaErroLabel, "skip, growx, wrap");

        panel.add(new JLabel("Gênero:"));
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        genderPanel.setOpaque(false);
        JRadioButton masc = new JRadioButton("Masculino");
        JRadioButton fem = new JRadioButton("Feminino");
        masc.setOpaque(false);
        fem.setOpaque(false);
        ButtonGroup bg = new ButtonGroup();
        bg.add(masc);
        bg.add(fem);
        genderPanel.add(masc);
        genderPanel.add(fem);
        panel.add(genderPanel);

        JButton registerButton = new JButton("Registrar");
        registerButton.setBackground(new Color(255, 140, 0));
        registerButton.setForeground(Color.WHITE);
        panel.add(registerButton, "growx");

        // Listener para validar senhas
        registerButton.addActionListener(e -> {
            String nome = ((JTextField) panel.getComponent(1)).getText();
            String email = ((JTextField) panel.getComponent(3)).getText();
            String senha = new String(senhaField.getPassword());
            String confirmarSenha = new String(confirmarSenhaField.getPassword());
            String genero = "";

            if (masc.isSelected()) {
                genero = "m";
            } else if (fem.isSelected()) {
                genero = "f";
            }

            if (!senha.equals(confirmarSenha)) {
                //JOptionPane.showMessageDialog(panel, "As senhas não conferem. Por favor, tente novamente.", "Erro", JOptionPane.ERROR_MESSAGE);
                senhaErroLabel.setText("As senhas não conferem. Por favor, tente novamente.");
            }else if(senha.isBlank()){
                senhaErroLabel.setText("É necessário preencher o campo senha");
            } else {
                try {
                    String ip = InetAddress.getLocalHost().getHostAddress();
                    Usuario usuario = new Usuario(nome, email, senha, ip, genero);
                    UsuarioDAO dao = new UsuarioDAO();
                    boolean sucesso = dao.salvarUsuario(usuario);

                    if (sucesso) {
                        //JOptionPane.showMessageDialog(panel, "Cadastro realizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        cardLayout.show(cardsPanel, "LOGIN");
                        loginBtn.setSelected(true);
                    } else {
                        senhaErroLabel.setText("Erro ao salvar usuário. Email pode já estar em uso.");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(panel, "Erro ao salvar usuário: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Mensagem abaixo do botão
        JButton switchToLogin = new JButton("Já tem uma conta? Faça login");
        switchToLogin.setContentAreaFilled(false);
        switchToLogin.setBorderPainted(false);
        switchToLogin.setForeground(new Color(180, 180, 255));
        switchToLogin.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        switchToLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        switchToLogin.addActionListener(e -> {
            cardLayout.show(cardsPanel, "LOGIN");
            loginBtn.setSelected(true);
        });

        panel.add(switchToLogin, "center");

        return panel;
    }
}
