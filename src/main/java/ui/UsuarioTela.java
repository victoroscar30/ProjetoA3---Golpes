package ui;

import javax.swing.*;
import java.awt.*;

public class UsuarioTela extends JFrame {
    public UsuarioTela() {
        super("Tela Usuário");
        setSize(300, 150);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel label = new JLabel("Tela Usuário");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label);

        setVisible(true);
    }
}
