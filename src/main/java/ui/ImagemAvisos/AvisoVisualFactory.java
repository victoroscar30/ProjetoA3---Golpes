package ui.ImagemAvisos;

import ADT.ListaDuplamenteLigadaAvisos;

import java.util.List;

public class AvisoVisualFactory {
    public static ListaDuplamenteLigadaAvisos carregarAvisosVisuais() {
        ListaDuplamenteLigadaAvisos lista = new ListaDuplamenteLigadaAvisos();

        lista.adicionar(new ImagemAviso(
                "https://raw.githubusercontent.com/victoroscar30/avisos-imagens-publicas/main/Phishing_1.png",
                "https://www.kaspersky.com.br/resource-center/preemptive-safety/phishing-prevention-tips",
                true
        ));

        lista.adicionar(new ImagemAviso(
                "https://raw.githubusercontent.com/victoroscar30/avisos-imagens-publicas/main/Phishing_2.png",
                "https://wise.com/pt/help/articles/2960083/o-que-e-a-verificacao-em-duas-etapas",
                true
        ));

        lista.adicionar(new ImagemAviso(
                "https://raw.githubusercontent.com/victoroscar30/avisos-imagens-publicas/main/SitesDesconhecidos_1.png",
                "https://www.gov.br/pt-br/noticias/justica-e-seguranca/2021/08/fique-de-olho-para-nao-cair-em-golpes-virtuais",
                true
        ));

        lista.adicionar(new ImagemAviso(
                "https://raw.githubusercontent.com/victoroscar30/avisos-imagens-publicas/main/SitesDesconhecidos_2.png",
                "https://transparencyreport.google.com/safe-browsing/search",
                true
        ));

        lista.adicionar(new ImagemAviso(
                "https://raw.githubusercontent.com/victoroscar30/avisos-imagens-publicas/main/SitesSuspeitos_1.png",
                "https://www.spcbrasil.org.br/blog/como-saber-se-um-site-e-seguro",
                true
        ));

        lista.adicionar(new ImagemAviso(
                "https://raw.githubusercontent.com/victoroscar30/avisos-imagens-publicas/main/SitesSuspeitos_2.png",
                "https://www.serasa.com.br/premium/blog/pharming/",
                true
        ));

        return lista;
    }
}
