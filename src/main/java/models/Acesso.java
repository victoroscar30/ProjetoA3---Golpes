package model;

import java.time.LocalDateTime;

public class Acesso {
    private String url;
    private LocalDateTime data;
    private boolean suspeito;

    public Acesso(String url, LocalDateTime data, boolean suspeito) {
        this.url = url;
        this.data = data;
        this.suspeito = suspeito;
    }

    public String getUrl() { return url; }
    public LocalDateTime getData() { return data; }
    public boolean isSuspeito() { return suspeito; }


}
