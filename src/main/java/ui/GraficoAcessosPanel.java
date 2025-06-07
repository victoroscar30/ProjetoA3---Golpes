package ui;

import database.Conexao;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
                    SELECT u.url, COUNT(*) as total
                    FROM acessos a
                    JOIN urls u ON a.id_url = u.id
                    WHERE a.data_acesso >= NOW() - INTERVAL ? DAY
                    GROUP BY u.url
                    ORDER BY total DESC
                    LIMIT 10;
                """;

                try (PreparedStatement ps = conn.prepareStatement(query)) {
                    ps.setInt(1, diasAtual);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        String url = rs.getString("url");
                        int total = rs.getInt("total");
                        dataset.addValue(total, labelAtual, url);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao gerar gráfico de acessos: " + e.getMessage());
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Top URLs por Período",
                "URL",
                "Quantidade de Acessos",
                dataset
        );

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setRenderer(new BarRenderer());

        ChartPanel chartPanel = new ChartPanel(chart);
        add(chartPanel, BorderLayout.CENTER);
    }
}
