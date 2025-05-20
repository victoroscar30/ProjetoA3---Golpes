package ui;

import javax.swing.*;
import java.awt.*;

public class AdminTela extends JFrame {
    public AdminTela() {
        super("Tela Admin");
        setSize(300, 150);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel label = new JLabel("Tela Admin");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label);

        setVisible(true);
    }
}
