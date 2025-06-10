package ui.ImagemAvisos;

import ADT.ListaDuplamenteLigadaAvisos;

import java.util.List;

public class AvisoVisualFactory {
    public static ListaDuplamenteLigadaAvisos carregarAvisosVisuais() {
        ListaDuplamenteLigadaAvisos lista = new ListaDuplamenteLigadaAvisos();

        lista.adicionar(new ImagemAviso(
                "https://raw.githubusercontent.com/victoroscar30/avisos-imagens-publicas/main/Phishing_1.png",
                "https://www.google.com/",
                true
        ));

        lista.adicionar(new ImagemAviso(
                "https://raw.githubusercontent.com/victoroscar30/avisos-imagens-publicas/main/Phishing_2.png",
                "https://www.google.com/",
                true
        ));

        lista.adicionar(new ImagemAviso(
                "https://raw.githubusercontent.com/victoroscar30/avisos-imagens-publicas/main/SitesDesconhecidos_1.png",
                "https://www.google.com/",
                true
        ));

        lista.adicionar(new ImagemAviso(
                "https://raw.githubusercontent.com/victoroscar30/avisos-imagens-publicas/main/SitesDesconhecidos_2.png",
                "https://www.google.com/",
                true
        ));

        lista.adicionar(new ImagemAviso(
                "https://raw.githubusercontent.com/victoroscar30/avisos-imagens-publicas/main/SitesSuspeitos_1.png",
                "https://www.google.com/",
                true
        ));

        lista.adicionar(new ImagemAviso(
                "https://raw.githubusercontent.com/victoroscar30/avisos-imagens-publicas/main/SitesSuspeitos_2.png",
                "https://www.google.com/",
                true
        ));

        return lista;
    }
}
