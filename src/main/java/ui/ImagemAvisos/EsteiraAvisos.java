package ui.ImagemAvisos;

import ADT.ListaDuplamenteLigadaAvisos;
import ADT.NoAviso;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;

public class EsteiraAvisos extends JPanel {
    private NoAviso atual;
    private JLabel imagemLabel;
    private Timer timer;

    public EsteiraAvisos(ListaDuplamenteLigadaAvisos lista) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(500, 120));
        setOpaque(false);

        atual = lista.getPrimeiro();

        imagemLabel = new JLabel();
        imagemLabel.setHorizontalAlignment(JLabel.CENTER);
        imagemLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        imagemLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (atual != null && atual.dado != null) {
                    try {
                        Desktop.getDesktop().browse(new URI(atual.dado.getLink()));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        add(imagemLabel, BorderLayout.CENTER);
        iniciarEsteira();
    }

    private void iniciarEsteira() {
        timer = new Timer(5000, e -> {
            if (atual != null) {
                imagemLabel.setIcon(atual.dado.getImagem());
                atual = atual.proximo;
            }
        });
        timer.start();
    }
}

