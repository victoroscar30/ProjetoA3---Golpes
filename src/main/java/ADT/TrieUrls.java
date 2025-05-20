package ADT;

import database.UrlDAO;
import models.Url;

import java.util.*;

public class TrieUrls {
    private final TrieNo raiz = new TrieNo();

    public TrieUrls() {}

    public TrieUrls(boolean carregarDoBanco)
    {
        if (carregarDoBanco) {
            UrlDAO dao = new UrlDAO();
            List<Url> urls = dao.listarUrls();
            for (Url u : urls) {
                this.inserir(u.getDominio());
            }
        }
    }

    public void inserir(String palavra) {
        TrieNo atual = raiz;
        for (char c : palavra.toCharArray()) {
            atual = atual.filhos.computeIfAbsent(c, k -> new TrieNo());
        }
        atual.ehFim = true;
    }

    public List<String> sugerir(String prefixo) {
        List<String> sugestoes = new ArrayList<>();
        TrieNo atual = raiz;
        for (char c : prefixo.toCharArray()) {
            if (!atual.filhos.containsKey(c)) {
                return sugestoes; // vazio
            }
            atual = atual.filhos.get(c);
        }
        buscarPalavras(prefixo, atual, sugestoes);
        return sugestoes;
    }

    private void buscarPalavras(String prefixo, TrieNo nodo, List<String> resultado) {
        if (nodo.ehFim) resultado.add(prefixo);
        for (Map.Entry<Character, TrieNo> entry : nodo.filhos.entrySet()) {
            buscarPalavras(prefixo + entry.getKey(), entry.getValue(), resultado);
        }
    }

}
