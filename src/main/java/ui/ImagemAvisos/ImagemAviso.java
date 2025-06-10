package ui.ImagemAvisos;

import javax.swing.*;
import java.net.URL;

public class ImagemAviso {
    private ImageIcon imagem;
    private String link;

    public ImagemAviso(String caminhoImagem, String link, boolean daWeb) {
        try {
            if (daWeb) {
                URL urlImagem = new URL(caminhoImagem);
                this.imagem = new ImageIcon(urlImagem);
            } else {
                this.imagem = new ImageIcon(caminhoImagem);
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.imagem = new ImageIcon(); // fallback
        }
        this.link = link;
    }

    public ImageIcon getImagem() {
        return imagem;
    }

    public String getLink() {
        return link;
    }
}

