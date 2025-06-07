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

        // Configuração para exibir os valores no topo das barras
        CategoryPlot plot = chart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();

        // Habilita a exibição dos valores
        renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setDefaultItemLabelsVisible(true);

        // Configura a posição do rótulo (TOP, OUTSIDE, etc.)
        renderer.setDefaultPositiveItemLabelPosition(new ItemLabelPosition());

        // Melhora a legibilidade dos rótulos
        renderer.setDefaultItemLabelFont(new Font("SansSerif", Font.BOLD, 15));
        renderer.setDefaultItemLabelPaint(Color.BLACK);

        // 2. Configura o ChartPanel corretamente
        ChartPanel chartPanel = new ChartPanel(chart) {
            @Override
            public Dimension getPreferredSize() {
                int width = Math.max(800, dataset.getColumnCount() * 100);
                return new Dimension(width, 500);
            }
        };

        chartPanel.setFillZoomRectangle(true);
        chartPanel.setMouseWheelEnabled(true);

        // 3. ScrollPane configurado para não quebrar o gráfico
        JScrollPane scrollPane = new JScrollPane(chartPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.getViewport().setBackground(Color.WHITE);

        // 4. Adiciona ao painel principal
        add(scrollPane, BorderLayout.CENTER);
    }


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
                int total = rs.getInt("qtd_acessos");
                dataset.addValue(total, "Acessos", url);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados:\n" + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }

        return dataset;
    }
}