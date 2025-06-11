package ui;

import database.UsuarioDAO;
import models.Usuario;

import javax.swing.*;
import java.awt.*;

public class CadastroTela extends JFrame {
    private JTextField campoNome;
    private JTextField campoEmail;
    private JPasswordField campoSenha;
    private UsuarioDAO usuarioDAO;

    public CadastroTela(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;

        setTitle("Cadastro de Usuário");
        setSize(300, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        campoNome = new JTextField(20);
        campoEmail = new JTextField(20);
        campoSenha = new JPasswordField(20);
        JButton botaoCadastrar = new JButton("Cadastrar");

        add(new JLabel("Nome:"));
        add(campoNome);
        add(new JLabel("Email:"));
        add(campoEmail);
        add(new JLabel("Senha:"));
        add(campoSenha);
        add(botaoCadastrar);

        setVisible(true);

        botaoCadastrar.addActionListener(e -> cadastrarUsuario());
    }

    private void cadastrarUsuario() {
        String nome = campoNome.getText();
        String email = campoEmail.getText();
        String senha = new String(campoSenha.getPassword());
        String ipOrigem = "0.0.0.0"; // Pode pegar IP real depois
        String genero = "";

        Usuario usuario = new Usuario(nome, email, senha, ipOrigem,genero);

        boolean sucesso = usuarioDAO.salvarUsuario(usuario);

        if (sucesso) {
            JOptionPane.showMessageDialog(this, "Usuário cadastrado com sucesso!");
            new LoginUI();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar usuário. Tente outro email.");
        }
    }
}
