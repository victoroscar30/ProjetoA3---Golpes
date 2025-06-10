package ADT;

import ui.ImagemAvisos.ImagemAviso;

public class ListaDuplamenteLigadaAvisos {
    private NoAviso primeiro;
    private NoAviso ultimo;

    public void adicionar(ImagemAviso aviso) {
        NoAviso novo = new NoAviso(aviso);
        if (primeiro == null) {
            primeiro = ultimo = novo;
            novo.proximo = novo.anterior = novo; // circular
        } else {
            novo.anterior = ultimo;
            novo.proximo = primeiro;
            ultimo.proximo = novo;
            primeiro.anterior = novo;
            ultimo = novo;
        }
    }

    public NoAviso getPrimeiro() {
        return primeiro;
    }

    public boolean estaVazia() {
        return primeiro == null;
    }
}


