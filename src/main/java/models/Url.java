package models;

import java.time.LocalDateTime;

public class Url {
    private int id;
    private String url;
    private String dominio;
    private String classificacao; // segura, suspeita, phishing, desconhecida
    private LocalDateTime dataCadastro;

    public Url() {}

    public Url(String url, String dominio, String classificacao) {
        this.url = url;
        this.dominio = dominio;
        this.classificacao = classificacao;
        this.dataCadastro = LocalDateTime.now();
    }

    // Getters e setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getDominio() { return dominio; }
    public void setDominio(String dominio) { this.dominio = dominio; }

    public String getClassificacao() { return classificacao; }
    public void setClassificacao(String classificacao) { this.classificacao = classificacao; }

    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }
}
