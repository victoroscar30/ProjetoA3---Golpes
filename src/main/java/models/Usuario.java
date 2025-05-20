package models;

public class Usuario {
    private int id;
    private String nome;
    private String email;
    private String senha;
    private String genero;
    private String ipOrigem;
    private String tipo;

    public Usuario() {}

    public Usuario(String nome, String email, String senha, String ipOrigem,String genero) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.ipOrigem = ipOrigem;
        this.genero = genero;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
    public String getIpOrigem() { return ipOrigem; }
    public void setIpOrigem(String ipOrigem) { this.ipOrigem = ipOrigem; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
}
