package ui;

import database.UsuarioDAO;
import models.Usuario;

import javax.swing.*;
import java.awt.*;

public class LoginTela extends JFrame {
    private JTextField campoEmail;
    private JPasswordField campoSenha;
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public LoginTela() {
        setTitle("Login");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        campoEmail = new JTextField(20);
        campoSenha = new JPasswordField(20);
        JButton botaoLogin = new JButton("Entrar");
        JButton botaoCadastro = new JButton("Sou novo aqui");

        add(new JLabel("Email:"));
        add(campoEmail);
        add(new JLabel("Senha:"));
        add(campoSenha);
        add(botaoLogin);
        add(botaoCadastro);

        setVisible(true);

        botaoLogin.addActionListener(e -> fazerLogin());

        botaoCadastro.addActionListener(e -> {
            new CadastroTela(usuarioDAO); // Abrir tela cadastro
            dispose();
        });
    }

    private void fazerLogin() {
        String email = campoEmail.getText();
        String senha = new String(campoSenha.getPassword());

        Usuario usuario = usuarioDAO.buscarPorEmailESenha(email, senha);

        if (usuario != null) {
            JOptionPane.showMessageDialog(this, "Bem-vindo, " + usuario.getNome());

            if ("admin".equalsIgnoreCase(usuario.getTipo())) {
                new AdminTela();
            } else {
                new UsuarioTela();
            }
            this.dispose(); // fecha a tela de login
        } else {
            JOptionPane.showMessageDialog(this, "Email ou senha inválidos");
        }
    }

    public static void main(String[] args) {
        new LoginTela();
    }
}


