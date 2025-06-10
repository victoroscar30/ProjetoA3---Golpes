package ADT;

import ui.ImagemAvisos.ImagemAviso;

public class NoAviso {
    public ImagemAviso dado;
    public NoAviso anterior, proximo;

    public NoAviso(ImagemAviso dado) {
        this.dado = dado;
    }
}

