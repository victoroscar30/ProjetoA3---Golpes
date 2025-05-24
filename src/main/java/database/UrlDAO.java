package database;
// 123
import models.Url;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UrlDAO {

    public boolean salvarUrl(Url url) {
        String sql = "INSERT INTO urls (url, dominio, classificacao) VALUES (?, ?, ?)";
        try (Connection con = Conexao.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, url.getUrl());
            stmt.setString(2, url.getDominio());
            stmt.setString(3, url.getClassificacao());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Url buscarPorUrl(String urlTexto) {
        String sql = "SELECT * FROM urls WHERE url = ?";
        try (Connection con = Conexao.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, urlTexto);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Url url = new Url();
                url.setId(rs.getInt("id"));
                url.setUrl(rs.getString("url"));
                url.setDominio(rs.getString("dominio"));
                url.setClassificacao(rs.getString("classificacao"));
                url.setDataCadastro(rs.getTimestamp("data_cadastro").toLocalDateTime());
                return url;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Url> listarUrls() {
        List<Url> lista = new ArrayList<>();
        String sql = "SELECT * FROM urls ORDER BY data_cadastro DESC";

        try (Connection con = Conexao.conectar();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Url url = new Url();
                url.setId(rs.getInt("id"));
                url.setUrl(rs.getString("url"));
                url.setDominio(rs.getString("dominio"));
                url.setClassificacao(rs.getString("classificacao"));
                url.setDataCadastro(rs.getTimestamp("data_cadastro").toLocalDateTime());

                lista.add(url);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static int garantirUrlRegistrada(String urlTexto) {
        UrlDAO dao = new UrlDAO();
        Url url = dao.buscarPorUrl(urlTexto);

        if (url != null) {
            return url.getId();
        }

        // Inserir nova URL
        Url nova = new Url();
        nova.setUrl(urlTexto);
        nova.setDominio(extrairDominio(urlTexto));
        nova.setClassificacao("desconhecida");

        boolean sucesso = dao.salvarUrl(nova);
        if (sucesso) {
            Url inserida = dao.buscarPorUrl(urlTexto);
            return inserida != null ? inserida.getId() : -1;
        }

        return -1;
    }

    private static String extrairDominio(String urlTexto) {
        try {
            return new java.net.URL(urlTexto).getHost();
        } catch (Exception e) {
            return urlTexto;
        }
    }
}
