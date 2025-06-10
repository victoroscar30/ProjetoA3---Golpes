package ui;

import database.Conexao;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class GraficoAcessosPanel extends JPanel {

    public GraficoAcessosPanel() {
        setLayout(new BorderLayout());

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Períodos a serem exibidos
        int[] dias = {7, 15, 30, 365};
        String[] labels = {"Últimos 7 dias", "Últimos 15 dias", "Últimos 30 dias", "Último ano"};

        try (Connection conn = Conexao.conectar()) {
            for (int i = 0; i < dias.length; i++) {
                int diasAtual = dias[i];
                String labelAtual = labels[i];

                String query = """
                    SELECT u.classificacao, COUNT(*) as total
                    FROM acessos a
                    JOIN urls u ON a.id_url = u.id
                    WHERE a.data_acesso >= NOW() - INTERVAL ? DAY
                    GROUP BY u.classificacao
                    ORDER BY total DESC
                    LIMIT 10;
                """;

                try (PreparedStatement ps = conn.prepareStatement(query)) {
                    ps.setInt(1, diasAtual);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        String classificacao = rs.getString("classificacao");
                        int total = rs.getInt("total");
                        dataset.addValue(total, labelAtual, classificacao);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao gerar gráfico de acessos: " + e.getMessage());
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Tipos de Site por Período",
                "Classificação",
                "Quantidade de Acessos",
                dataset
        );

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setRenderer(new BarRenderer());

        BarRenderer renderer = new BarRenderer();


        renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setDefaultItemLabelsVisible(true);


        renderer.setDefaultPositiveItemLabelPosition(new ItemLabelPosition(
                ItemLabelAnchor.OUTSIDE12,
                TextAnchor.BOTTOM_CENTER
        ));

        renderer.setDefaultItemLabelFont(new Font("SansSerif", Font.PLAIN, 10));
        renderer.setDefaultItemLabelPaint(Color.BLACK);

        plot.setRenderer(renderer);

        ChartPanel chartPanel = new ChartPanel(chart);
        add(chartPanel, BorderLayout.CENTER);
    }
}
