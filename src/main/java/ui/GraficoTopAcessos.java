package ui;

import com.orsonpdf.util.TextAnchor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.HashMap;
import java.util.Map;

import static database.Conexao.conectar;

public class GraficoTopAcessos extends JPanel {

    public GraficoTopAcessos() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        DefaultCategoryDataset dataset = carregarDados();

        JFreeChart chart = ChartFactory.createBarChart(
                "Sites Mais Acessados",
                "URL",
                "Quantidade",
                dataset
        );

        CategoryPlot plot = chart.getCategoryPlot();

        // Substitua o renderer padrão por um customizado
        BarRenderer renderer = new BarRenderer() {
            @Override
            public Paint getItemPaint(int row, int column) {
                String url = (String) dataset.getColumnKey(column);
                String classificacao = classificacoes.get(url);
                return getColorForClassification(classificacao);
            }
        };

        plot.setRenderer(renderer);

        // Configuração para exibir os valores no topo das barras
        renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setDefaultItemLabelsVisible(true);
        renderer.setDefaultItemLabelFont(new Font("SansSerif", Font.BOLD, 15));
        renderer.setDefaultItemLabelPaint(Color.BLACK);

        ChartPanel chartPanel = new ChartPanel(chart) {
            @Override
            public Dimension getPreferredSize() {
                int width = Math.max(800, dataset.getColumnCount() * 100);
                return new Dimension(width, 500);
            }
        };

        chartPanel.setFillZoomRectangle(true);
        chartPanel.setMouseWheelEnabled(true);

        JScrollPane scrollPane = new JScrollPane(chartPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.getViewport().setBackground(Color.WHITE);

        add(scrollPane, BorderLayout.CENTER);
    }

    private Color getColorForClassification(String classificacao) {
        if (classificacao == null) {
            return Color.GRAY;
        }

        // Defina suas cores conforme as classificações
        switch (classificacao.toLowerCase()) {
            case "segura":
                return new Color(0, 100, 0); // Verde escuro
            case "phishing":
                return new Color(178, 34, 34); // Vermelho fogo
            case "desconhecida":
                return new Color(0, 0, 0); // preto
            case "suspeita":
                return new Color(218, 165, 32); // Dourado
            default:
                return new Color(169, 169, 169); // Cinza
        }
    }

    private Map<String, String> classificacoes = new HashMap<>();
    private DefaultCategoryDataset carregarDados() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String sql = "SELECT g1.*,urls.dominio,urls.classificacao\n" +
                "FROM (\n" +
                "    SELECT *,\n" +
                "           DENSE_RANK() OVER(ORDER BY qtd_acessos DESC) AS ranking\n" +
                "    FROM grafico_top_acessos\n" +
                ") g1\n" +
                "JOIN (\n" +
                "    SELECT MIN(id_url) AS min_id, ranking\n" +
                "    FROM (\n" +
                "        SELECT id_url,\n" +
                "               DENSE_RANK() OVER(ORDER BY qtd_acessos DESC) AS ranking\n" +
                "        FROM grafico_top_acessos\n" +
                "    ) ranked\n" +
                "    GROUP BY ranking\n" +
                ") g2 ON g1.id_url = g2.min_id AND g1.ranking = g2.ranking\n" +
                "left join urls on urls.id= g1.id_url\n" +
                "ORDER BY g1.ranking;";

        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String url = rs.getString("dominio");
                String classificacao = rs.getString("classificacao");
                int total = rs.getInt("qtd_acessos");
                dataset.addValue(total, "Acessos", url);
                classificacoes.put(url, classificacao);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados:\n" + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }

        return dataset;
    }
}